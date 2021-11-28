package com.sun.jollygame.util;

import com.sun.jollygame.entity.GameRoom;
import com.sun.jollygame.factory.GameRoomMapFactory;
import com.sun.jollygame.singlesource.UserRoomMapFactory;

public class GameRoomUtil {

    public static GameRoom getGameRoomByUserId(String userId){
        UserRoomMapFactory userRoomMapFactory = UserRoomMapFactory.getInstance();
        if (userRoomMapFactory.get(userId) == null) {
            return null;
        }
        String roomId = userRoomMapFactory.get(userId);
        return GameRoomMapFactory.getInstance().get(roomId);
    }
}
