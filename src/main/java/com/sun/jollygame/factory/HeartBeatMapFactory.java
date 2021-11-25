package com.sun.jollygame.factory;

import com.sun.jollygame.entity.GameRoom;
import com.sun.jollygame.entity.HeartBeat;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author sunkai
 * @since 2021/11/25 5:48 下午
 */
public class HeartBeatMapFactory {
    private static final HeartBeatMapFactory heartBeatMapFactory = new HeartBeatMapFactory();
    private static Map<String, HeartBeat> heartBeatMap = new ConcurrentHashMap<>();

    //单例模式的特点，构造必须私有
    private HeartBeatMapFactory() {
    }

    public static HeartBeatMapFactory getInstance() {
        return heartBeatMapFactory;
    }

    public Map<String, HeartBeat> getHeartBeatMap(){
        return heartBeatMap;
    }

    public void put(String key,HeartBeat value){
        heartBeatMap.put(key, value);
    }

    public HeartBeat get(String key){
        return heartBeatMap.get(key);
    }

    public int getSize(){
        return heartBeatMap.size();
    }

    public void remove(String key){
        heartBeatMap.remove(key);
    }
}
