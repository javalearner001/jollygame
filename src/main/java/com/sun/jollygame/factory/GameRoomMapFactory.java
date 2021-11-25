package com.sun.jollygame.factory;

import com.sun.jollygame.entity.GameRoom;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author sunkai
 * @since 2021/11/25 3:58 下午
 */
public class GameRoomMapFactory {

    private static final GameRoomMapFactory gameRoomMapFactory = new GameRoomMapFactory();
    private static Map<String, GameRoom> gameRoomMap = new ConcurrentHashMap<>();

    //单例模式的特点，构造必须私有
    private GameRoomMapFactory() {
    }

    public static GameRoomMapFactory getInstance() {
        return gameRoomMapFactory;
    }

    public void put(String key,GameRoom value){
        gameRoomMap.put(key, value);
    }

    public GameRoom get(String key){
        return gameRoomMap.get(key);
    }

    public int getSize(){
        return gameRoomMap.size();
    }

    public void remove(String key){
        gameRoomMap.remove(key);
    }
}
