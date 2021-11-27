package com.sun.jollygame.socketservice;


import com.alibaba.fastjson.JSON;
import com.sun.jollygame.entity.GameRoom;
import com.sun.jollygame.entity.enumc.MessageTypeEnum;
import com.sun.jollygame.entity.response.MatchResponse;
import com.sun.jollygame.entity.response.MessageResponse;
import com.sun.jollygame.factory.GameRoomMapFactory;
import com.sun.jollygame.singlesource.ImgIdFactory;
import com.sun.jollygame.singlesource.UserMapFactory;
import com.sun.jollygame.singlesource.UserRoomMapFactory;
import com.sun.jollygame.socket.SessionQueue;
import com.sun.jollygame.socket.WebSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author sunkai
 * @since 2021/11/5 10:20 上午
 */
@Slf4j
@Service
public class MatchOpponentService {



    public void createGameRoom(SessionQueue sessionQueue,String userId){
        if (sessionQueue.size() < 2) {
            MessageResponse messageResponse = new MessageResponse("请稍等，正在为您匹配对手", MessageTypeEnum.BROAD_CAST.getCode());
            WebSocket.webSocketMap.get(userId).sendObjMessage(JSON.toJSONString(messageResponse));
            return;
        }
        String userIdOne = sessionQueue.consume();
        String userIdTwo = sessionQueue.consume();

        //1.创建房间，存放房间
        GameRoom gameRoom = new GameRoom(userIdOne,userIdTwo);
        GameRoomMapFactory mapFactory = GameRoomMapFactory.getInstance();
        mapFactory.put(gameRoom.getRoomId(),gameRoom);

        //2.保存用户和房间号
        UserRoomMapFactory userRoomFactory = UserRoomMapFactory.getInstance();
        userRoomFactory.put(userIdOne,gameRoom.getRoomId());
        userRoomFactory.put(userIdTwo,gameRoom.getRoomId());

        //3.给用户发送消息
        log.info("当前存在房间数:{}", mapFactory.getSize());
        ImgIdFactory imgIdFactory = ImgIdFactory.getInstance();
        UserMapFactory userListFactory = UserMapFactory.getInstance();
        //返回对手的头像信息
        MatchResponse matchResponseOne = new MatchResponse(imgIdFactory.getImgList(),userListFactory.get(userIdTwo).getHeadImgId());
        //返回对手的头像信息
        MatchResponse matchResponseTwo = new MatchResponse(imgIdFactory.getImgList(),userListFactory.get(userIdOne).getHeadImgId());
        WebSocket.webSocketMap.get(userIdOne).sendObjMessage(JSON.toJSONString(matchResponseOne));
        WebSocket.webSocketMap.get(userIdTwo).sendObjMessage(JSON.toJSONString(matchResponseTwo));
    }
}
