package com.sun.jollygame.entity.request;

import lombok.Data;

/**
 * @author sunkai
 * @since 2021/11/4 4:22 下午
 */
@Data
public class SocketMessage {

    private String userId;

    private String text;
    /**
     * 消息类型  100：广播消息  1001：匹配开始
     */
    private int messageType;
    /**
     * 分数
     */
    private int grade;
}
