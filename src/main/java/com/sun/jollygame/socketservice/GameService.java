package com.sun.jollygame.socketservice;

import com.alibaba.fastjson.JSON;
import com.sun.jollygame.entity.GameRoom;
import com.sun.jollygame.entity.HeartBeat;
import com.sun.jollygame.entity.UserGameRecord;
import com.sun.jollygame.entity.enumc.MessageTypeEnum;
import com.sun.jollygame.entity.request.SocketMessage;
import com.sun.jollygame.entity.response.BaseResponse;
import com.sun.jollygame.entity.response.GameOverResponse;
import com.sun.jollygame.entity.response.GradeAddResponse;
import com.sun.jollygame.factory.GameRoomMapFactory;
import com.sun.jollygame.factory.HeartBeatMapFactory;
import com.sun.jollygame.singlesource.UserRoomMapFactory;
import com.sun.jollygame.socket.WebSocket;
import com.sun.jollygame.util.GameRoomUtil;
import com.sun.jollygame.util.GameUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author sunkai
 * @since 2021/11/25 4:33 下午
 */
@Slf4j
@Service
public class GameService {

    private static final int winGrade = 13;

    @Autowired
    private GameRoomService gameRoomService;

    /**
     * 开始游戏 需要房间内的玩家都准备好才能开始
     *
     * @param socketMessage
     */
    public void startGame(SocketMessage socketMessage) {
        GameRoom gameRoom = GameRoomUtil.getGameRoomByUserId(socketMessage.getUserId());
        if (gameRoom == null) {
            log.error("游戏房间为空，userId:{}", socketMessage.getUserId());
        }
        UserGameRecord otherUserRecord = gameRoom.getAnotherUserRecord(socketMessage.getUserId());
        if (otherUserRecord.getReady()) {
            //此处玩家都准备好了，1.通知客户端两个玩家都准备好
            BaseResponse response = new BaseResponse();
            response.setMessageType(MessageTypeEnum.GAME_READY_RESP.getCode());

            WebSocket.webSocketMap.get(gameRoom.getFirstUserId()).sendObjMessage(JSON.toJSONString(response));
            WebSocket.webSocketMap.get(gameRoom.getSecondUserId()).sendObjMessage(JSON.toJSONString(response));

            //2.开始加入心跳队列
            HeartBeatMapFactory heartBeatMapFactory = HeartBeatMapFactory.getInstance();
            HeartBeat heartBeat1 = new HeartBeat(gameRoom.getFirstUserId(), new Date());
            heartBeatMapFactory.put(heartBeat1.getUserId(), heartBeat1);

            HeartBeat heartBeat2 = new HeartBeat(gameRoom.getSecondUserId(), new Date());
            heartBeatMapFactory.put(heartBeat2.getUserId(), heartBeat2);
        } else {
            UserGameRecord userRecord = gameRoom.getUserRecord(socketMessage.getUserId());
            userRecord.setReady(true);
        }
    }

    public void receiveHeartMessage(SocketMessage socketMessage) {
        log.info("接收到心跳消息：{}", JSON.toJSONString(socketMessage));
        //1.判断是否为心跳消息

        //2.将当前时间保存到heart中
        String userId = socketMessage.getUserId();
        HeartBeatMapFactory heartBeatMapFactory = HeartBeatMapFactory.getInstance();
        HeartBeat heartBeat = heartBeatMapFactory.get(userId);
        heartBeat.setHeartTime(new Date());
    }

    public void addGrade(SocketMessage socketMessage) {
        //1.找到房间
        GameRoom gameRoom = GameRoomUtil.getGameRoomByUserId(socketMessage.getUserId());
        if (gameRoom == null){
            log.error("增加分数 房间为空：{}",socketMessage.getUserId());
            return;
        }
        //2.给房间中的用户增加分数
        UserGameRecord userRecord = gameRoom.getUserRecord(socketMessage.getUserId());
        userRecord.setGrade(socketMessage.getGrade());

        //3.分数发给另一个玩家
        UserGameRecord anotherUserRecord = gameRoom.getAnotherUserRecord(socketMessage.getUserId());
        GradeAddResponse addResponse = GameUtil.getGradeAddResponse(userRecord.getGrade(),anotherUserRecord.getUserId());
        WebSocket.webSocketMap.get(addResponse.getUserId()).sendObjMessage(JSON.toJSONString(addResponse));

        //4.判断当前用户是否获胜
        if (userRecord.getGrade() >= winGrade){
            GameOverResponse responseOne = GameUtil.getGameOverResponse(gameRoom.getUserGameRecordFirst());
            GameOverResponse responseTwo = GameUtil.getGameOverResponse(gameRoom.getUserGameRecordSecond());
            if (responseOne.getUserId().equals(userRecord.getUserId())){
                responseOne.setIsWin(1);
                responseTwo.setIsWin(2);
            }else {
                responseOne.setIsWin(2);
                responseTwo.setIsWin(1);
            }
            WebSocket.webSocketMap.get(responseOne.getUserId()).sendObjMessage(JSON.toJSONString(responseOne));
            WebSocket.webSocketMap.get(responseTwo.getUserId()).sendObjMessage(JSON.toJSONString(responseTwo));

            //获胜之后结束房间
            gameRoomService.gameOverTwo(socketMessage.getUserId());
        }
    }
}
