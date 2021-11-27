package com.sun.jollygame.socketservice;


import com.sun.jollygame.entity.GameRoom;
import com.sun.jollygame.entity.enumc.MessageTypeEnum;
import com.sun.jollygame.entity.response.MessageResponse;
import com.sun.jollygame.factory.GameRoomMapFactory;
import com.sun.jollygame.singlesource.UserRoomMapFactory;
import com.sun.jollygame.socket.WebSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author sunkai
 * @since 2021/11/5 10:20 上午
 */
@Slf4j
@Service
public class MatchOpponentService {



    public void createGameRoom(String userIdOne,String userIdTwo){
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
        MessageResponse response = new MessageResponse("对战开始", MessageTypeEnum.BROAD_CAST.getCode());
        WebSocket.webSocketMap.get(userIdOne).sendObjMessage(response);
        WebSocket.webSocketMap.get(userIdTwo).sendObjMessage(response);
    }
}
