package com.sun.jollygame.singlesource;


import com.sun.jollygame.entity.GameRoom;
import com.sun.jollygame.factory.GameRoomMapFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 存放用户id和房间号资源
 * @author sunkai
 * @since 2021/11/9 3:47 下午
 */
public class UserRoomMapFactory {

    private static final UserRoomMapFactory userRoomMapFactory = new UserRoomMapFactory();
    private static Map<String, String> userRoomKeyMap = new ConcurrentHashMap<>();

    private UserRoomMapFactory(){

    }

    public static UserRoomMapFactory getInstance() {
        return userRoomMapFactory;
    }

    public void put(String key, String value){
        userRoomKeyMap.put(key, value);
    }

    public String get(String key){
        return userRoomKeyMap.get(key);
    }

    public void remove(String key){
        userRoomKeyMap.remove(key);
    }
}
