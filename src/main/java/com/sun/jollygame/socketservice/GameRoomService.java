package com.sun.jollygame.socketservice;

import com.sun.jollygame.entity.GameRoom;
import com.sun.jollygame.factory.GameRoomMapFactory;
import com.sun.jollygame.singlesource.UserRoomMapFactory;
import com.sun.jollygame.socket.WebSocket;
import lombok.extern.slf4j.Slf4j;
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
     * 用户退出
     */
    public void deleteRoom(String userId){
        GameRoomMapFactory mapFactory = GameRoomMapFactory.getInstance();
        GameRoom gameRoom = mapFactory.get(userId);

        //1.删除用户所对应map
        UserRoomMapFactory userRoomFactory = UserRoomMapFactory.getInstance();
        userRoomFactory.remove(gameRoom.getFirstUserId());
        userRoomFactory.remove(gameRoom.getSecondUserId());
        //2.删除房间
        mapFactory.remove(gameRoom.getRoomId());

        //3.删除websocket链接
        WebSocket.webSocketMap.remove(userId);
    }

}
