package com.sun.jollygame.socketservice;

import com.alibaba.fastjson.JSON;
import com.sun.jollygame.entity.GameRoom;
import com.sun.jollygame.entity.HeartBeat;
import com.sun.jollygame.entity.UserGameRecord;
import com.sun.jollygame.entity.request.SocketMessage;
import com.sun.jollygame.factory.GameRoomMapFactory;
import com.sun.jollygame.factory.HeartBeatMapFactory;
import com.sun.jollygame.singlesource.UserRoomMapFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author sunkai
 * @since 2021/11/25 4:33 下午
 */
@Slf4j
@Service
public class GameService {

    public void startGame(SocketMessage socketMessage){
        //1.开始加入心跳队列
        HeartBeatMapFactory heartBeatMapFactory = HeartBeatMapFactory.getInstance();
        HeartBeat heartBeat = new HeartBeat();
        heartBeat.setUserId(socketMessage.getUserId());

        heartBeatMapFactory.put(socketMessage.getUserId(),heartBeat);
    }

    public void receiveHeartMessage(SocketMessage socketMessage){
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
        UserRoomMapFactory userRoomMapFactory = UserRoomMapFactory.getInstance();
        String roomId = userRoomMapFactory.get(socketMessage.getUserId());

        GameRoomMapFactory gameRoomMapFactory = GameRoomMapFactory.getInstance();
        GameRoom gameRoom = gameRoomMapFactory.get(roomId);
        //2.给房间中的用户增加分数
        UserGameRecord userRecord = gameRoom.getUserRecord(socketMessage.getUserId());
        userRecord.setGrade(socketMessage.getGrade());
    }
}
