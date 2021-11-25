package com.sun.jollygame.socket;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author sunkai
 * @since 2021/11/4 7:39 下午
 */
public class SessionQueue {
    //队列大小
    static final int QUEUE_MAX_SIZE = 1000;

    static ArrayBlockingQueue<String> blockingQueue = new ArrayBlockingQueue<String>(QUEUE_MAX_SIZE);

    /**
     * 私有的默认构造子，保证外界无法直接实例化
     */
    private SessionQueue() {
    }

    /**
     * 类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例
     * 没有绑定关系，而且只有被调用到才会装载，从而实现了延迟加载
     */
    private static class SingletonHolder {
        /**
         * 静态初始化器，由JVM来保证线程安全
         */
        private static SessionQueue queue = new SessionQueue();
    }

    //单例队列
    public static SessionQueue getSessionQueue() {
        return SingletonHolder.queue;
    }

    //生产入队
    public void produce(String userId) throws InterruptedException {
        blockingQueue.put(userId);
    }

    //消费出队
    public String consume() {
        try {
            return blockingQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean contains(String userId) {
        return blockingQueue.contains(userId);
    }

    // 获取队列大小
    public int size() {
        return blockingQueue.size();
    }
}
