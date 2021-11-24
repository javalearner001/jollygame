package com.sun.jollygame.singlesource;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 存放用户id和房间号资源
 * @author sunkai
 * @since 2021/11/9 3:47 下午
 */
public class UserRoomMapFactory {
    private static Map<String, String> chessRoomMap = new ConcurrentHashMap<>();

    private UserRoomMapFactory(){

    }

    public static Map<String, String> getUserRoomMap() {
        if (chessRoomMap == null) {
            chessRoomMap = new ConcurrentHashMap<>();
        }
        return chessRoomMap;
    }
}
