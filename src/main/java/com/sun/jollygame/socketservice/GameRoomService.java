package com.sun.jollygame.socketservice;

import com.sun.jollygame.entity.GameRoom;
import com.sun.jollygame.factory.GameRoomMapFactory;
import com.sun.jollygame.factory.HeartBeatMapFactory;
import com.sun.jollygame.singlesource.UserMapFactory;
import com.sun.jollygame.singlesource.UserRoomMapFactory;
import com.sun.jollygame.socket.WebSocket;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 房间相关service
 * @author sunkai
 * @since 2021/11/25 3:43 下午
 */
@Slf4j
@Service
public class GameRoomService {

    /**
     * 用户退出 游戏
     */
    public void deleteGame(String userId){
        GameRoomMapFactory mapFactory = GameRoomMapFactory.getInstance();
        GameRoom gameRoom = mapFactory.get(userId);
        if (gameRoom != null){
            //1.删除用户所对应map
            UserRoomMapFactory userRoomFactory = UserRoomMapFactory.getInstance();
            userRoomFactory.remove(gameRoom.getFirstUserId());
            userRoomFactory.remove(gameRoom.getSecondUserId());
            //2.删除房间
            mapFactory.remove(gameRoom.getRoomId());
        }
        //3.删除websocket链接
        WebSocket.webSocketMap.remove(userId);
        //4.删除用户map
        UserMapFactory.getInstance().remove(userId);
        //5.删除心跳map
        HeartBeatMapFactory.getInstance().remove(userId);

        log.info("[websocket] 连接断开，总数:{}", WebSocket.webSocketMap.size());
    }

    /**
     * 用户完成一局
     */
    public void gameOver(String userId){
        //1.删除心跳map
        HeartBeatMapFactory.getInstance().remove(userId);
        //2.删除用户所对应map
        UserRoomMapFactory userRoomFactory = UserRoomMapFactory.getInstance();
        userRoomFactory.remove(userId);

        UserRoomMapFactory userRoomMapFactory = UserRoomMapFactory.getInstance();
        GameRoomMapFactory mapFactory = GameRoomMapFactory.getInstance();

        String roomId = userRoomMapFactory.get(userId);
        if (StringUtils.isBlank(roomId)){
            return;
        }
        GameRoom gameRoom = mapFactory.get(roomId);
        //2.删除房间
        mapFactory.remove(gameRoom.getRoomId());
    }

    /**
     * 用户完成一局,删除两个用户的数据
     */
    public void gameOverTwo(String userId){
        UserRoomMapFactory userRoomMapFactory = UserRoomMapFactory.getInstance();
        GameRoomMapFactory mapFactory = GameRoomMapFactory.getInstance();

        String roomId = userRoomMapFactory.get(userId);
        if (StringUtils.isBlank(roomId)){
            return;
        }
        GameRoom gameRoom = mapFactory.get(roomId);
        //1.删除房间
        mapFactory.remove(gameRoom.getRoomId());

        //2.删除心跳map
        HeartBeatMapFactory.getInstance().remove(gameRoom.getFirstUserId());
        HeartBeatMapFactory.getInstance().remove(gameRoom.getSecondUserId());
        //3.删除用户所对应map
        UserRoomMapFactory userRoomFactory = UserRoomMapFactory.getInstance();
        userRoomFactory.remove(gameRoom.getFirstUserId());
        userRoomFactory.remove(gameRoom.getSecondUserId());
    }

}
