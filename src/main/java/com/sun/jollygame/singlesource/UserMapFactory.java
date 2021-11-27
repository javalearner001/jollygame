package com.sun.jollygame.singlesource;

import com.sun.jollygame.entity.UserGameRecord;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserMapFactory {

    private static final UserMapFactory userListFactory = new UserMapFactory();
    private static Map<String, UserGameRecord> userGameRecordMap = new ConcurrentHashMap<>();

    private UserMapFactory(){

    }

    public static UserMapFactory getInstance() {
        return userListFactory;
    }

    public void put(String key, UserGameRecord value){
        userGameRecordMap.put(key, value);
    }

    public UserGameRecord get(String key){
        return userGameRecordMap.get(key);
    }

    public void remove(String key){
        userGameRecordMap.remove(key);
    }
}
