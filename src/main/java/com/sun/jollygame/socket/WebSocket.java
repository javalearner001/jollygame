package com.sun.jollygame.socket;

import com.alibaba.fastjson.JSON;

import com.sun.jollygame.entity.UserGameRecord;
import com.sun.jollygame.entity.enumc.MessageTypeEnum;
import com.sun.jollygame.entity.request.SocketMessage;
import com.sun.jollygame.entity.response.MatchResponse;
import com.sun.jollygame.entity.response.MessageResponse;
import com.sun.jollygame.singlesource.ImgIdFactory;
import com.sun.jollygame.singlesource.UserMapFactory;
import com.sun.jollygame.socketservice.GameRoomService;
import com.sun.jollygame.socketservice.GameService;
import com.sun.jollygame.socketservice.MatchOpponentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.net.SocketException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author sunkai
 * @since 2021/11/3 3:35 下午
 */
@Service
@ServerEndpoint(value = "/message/{userId}")
@Slf4j
public class WebSocket implements ApplicationContextAware {

    public static final Map<String, WebSocket> webSocketMap = new ConcurrentHashMap<>();

    private static ApplicationContext applicationContext;
    private MatchOpponentService matchOpponentService;
    private GameService gameService;
    private GameRoomService gameRoomService;

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) throws SocketException {
        if (webSocketMap.containsKey(session.getId())) {
            log.info("此连接已存在！");
            return;
        }
        this.session = session;
        webSocketMap.put(session.getId(), this);
        log.info("[websocket] 有新的连接，总数:{}", webSocketMap.size());

        UserMapFactory userMapFactory = UserMapFactory.getInstance();
        UserGameRecord userGameRecord = new UserGameRecord(userId);
        userMapFactory.put(session.getId(),userGameRecord);

        this.session.getAsyncRemote().sendText("恭喜您成功连接上WebSocket-->当前在线人数为：" + webSocketMap.size());
        matchOpponentService = applicationContext.getBean(MatchOpponentService.class);
        gameService = applicationContext.getBean(GameService.class);
        gameRoomService = applicationContext.getBean(GameRoomService.class);
    }

    @OnClose
    public void onClose() {
        String userId = this.session.getId();
        if (userId != null) {
            gameRoomService.deleteGame(userId);
        }
    }

    @OnMessage
    public void onMessage(String message) {
        SocketMessage socketMessage = JSON.parseObject(message, SocketMessage.class);
        socketMessage.setUserId(this.session.getId());
        log.info("[webSocket]收到客户端发送的消息，socketMessage:{}", JSON.toJSONString(socketMessage));

        MessageTypeEnum messageTypeEnum = MessageTypeEnum.getFromKey(socketMessage.getMessageType());
        if (messageTypeEnum == null){
            log.error("枚举类缺少============");
            return;
        }

        switch (messageTypeEnum) {
            case BROAD_CAST:
                MessageResponse response = new MessageResponse(socketMessage.getText(), MessageTypeEnum.BROAD_CAST.getCode());
                sendMessage(response);
                break;
            case MATCH_OPPONENT:
                matchOpponent(socketMessage);
                break;
            case START_GAME:
                gameService.startGame(socketMessage);
                break;
            case ADD_GRADE:
                gameService.addGrade(socketMessage);
                break;
            case CHECK_BOARD_DATA:
                gameService.receiveHeartMessage(socketMessage);
                break;
        }

    }

    /**
     * 发送消息 广播
     *
     * @param messageResponse
     * @return 全部都发送一遍
     */
    public void sendMessage(MessageResponse messageResponse) {
        for (WebSocket webSocket : webSocketMap.values()) {
            webSocket.session.getAsyncRemote().sendText(JSON.toJSONString(messageResponse));
        }
        log.info("【wesocket】广播消息,message={}", JSON.toJSONString(messageResponse));
    }

    /**
     * 此为单点消息 (发送对象)
     */
    public void sendObjMessage(String message) {
        this.session.getAsyncRemote().sendText(message);
    }

    /**
     * 匹配对手 加入队列
     */
    public void matchOpponent(SocketMessage socketMessage) {
        SessionQueue sessionQueue = SessionQueue.getSessionQueue();
        if (sessionQueue.contains(socketMessage.getUserId())){
            log.error("您已经在队列中");
            return;
        }

        try {
            sessionQueue.produce(socketMessage.getUserId());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //保存用户匹配时间
        UserMapFactory userMapFactory = UserMapFactory.getInstance();
        UserGameRecord userGameRecord = userMapFactory.get(socketMessage.getUserId());
        userGameRecord.setMatchTime(new Date());
        userGameRecord.setHeadImgId(socketMessage.getHeadImgId());

        log.info("队列的数据为:{}", JSON.toJSONString(sessionQueue));
        matchOpponentService.createGameRoom(sessionQueue,socketMessage.getUserId());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        WebSocket.applicationContext = applicationContext;
    }
}
