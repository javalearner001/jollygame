package com.sun.jollygame.entity.response;

/**
 * @author sunkai
 * @since 2021/11/4 5:08 下午
 */
public class MessageResponse {

    private String message;

    /**
     * 消息类型  100：广播消息  1001：匹配开始
     */
    private int messageType;;

    public MessageResponse(String message, int messageType) {
        this.message = message;
        this.messageType = messageType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }
}
