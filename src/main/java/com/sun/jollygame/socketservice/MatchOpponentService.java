package com.sun.jollygame.socketservice;


import com.alibaba.fastjson.JSON;
import com.sun.jollygame.entity.GameRoom;
import com.sun.jollygame.entity.UserGameRecord;
import com.sun.jollygame.entity.enumc.MessageTypeEnum;
import com.sun.jollygame.entity.response.MatchResponse;
import com.sun.jollygame.entity.response.MessageResponse;
import com.sun.jollygame.factory.GameRoomMapFactory;
import com.sun.jollygame.singlesource.ImgIdFactory;
import com.sun.jollygame.singlesource.UserMapFactory;
import com.sun.jollygame.singlesource.UserRoomMapFactory;
import com.sun.jollygame.socket.SessionQueue;
import com.sun.jollygame.socket.WebSocket;
import com.sun.jollygame.util.GameUtil;
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
        ImgIdFactory imgIdFactory = ImgIdFactory.getInstance();
        if (sessionQueue.size() < 2) {
            MatchResponse matchResponse = new MatchResponse();
            matchResponse.setMessage("请稍等，正在为您匹配对手");
            matchResponse.setMessageType(MessageTypeEnum.MATCH_OPPONENT_RESP.getCode());
            matchResponse.setImgIdlist(imgIdFactory.getImgList());
            WebSocket.webSocketMap.get(userId).sendObjMessage(JSON.toJSONString(matchResponse));
            return;
        }
        String userIdOne = sessionQueue.consume();
        String userIdTwo = sessionQueue.consume();

        //1.创建房间，存放房间
        UserGameRecord recordOne = new UserGameRecord(userIdOne);
        UserGameRecord recordTwo = new UserGameRecord(userIdTwo);
        GameRoom gameRoom = new GameRoom(userIdOne,userIdTwo,recordOne,recordTwo);

        GameRoomMapFactory mapFactory = GameRoomMapFactory.getInstance();
        mapFactory.put(gameRoom.getRoomId(),gameRoom);

        //2.保存用户和房间号
        UserRoomMapFactory userRoomFactory = UserRoomMapFactory.getInstance();
        userRoomFactory.put(userIdOne,gameRoom.getRoomId());
        userRoomFactory.put(userIdTwo,gameRoom.getRoomId());

        //3.给用户发送消息
        log.info("当前存在房间数:{}", mapFactory.getSize());

        UserMapFactory userListFactory = UserMapFactory.getInstance();
        //返回对手的头像信息
        MatchResponse matchResponseOne = GameUtil.getMatchResponse(userIdOne,MessageTypeEnum.MATCH_OPPONENT_SUCCESS.getCode());
        matchResponseOne.setEnemyHeadImgId(userListFactory.get(userIdTwo).getHeadImgId());
        //返回对手的头像信息
        MatchResponse matchResponseTwo = GameUtil.getMatchResponse(userIdTwo,MessageTypeEnum.MATCH_OPPONENT_SUCCESS.getCode());
        matchResponseTwo.setEnemyHeadImgId(userListFactory.get(userIdOne).getHeadImgId());
        WebSocket.webSocketMap.get(userIdOne).sendObjMessage(JSON.toJSONString(matchResponseOne));
        WebSocket.webSocketMap.get(userIdTwo).sendObjMessage(JSON.toJSONString(matchResponseTwo));
    }
}
