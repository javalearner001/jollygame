package com.sun.jollygame.entity.enumc;

/**
 * @author sunkai
 * @since 2021/11/5 5:44 下午
 */
public enum MessageTypeEnum {
    BROAD_CAST(100, "广播消息"),
    MATCH_OPPONENT(1001, "匹配对手"),
    START_GAME(1002,"开始游戏"),
    ADD_GRADE(1003,"增加分数"),
    CHECK_BOARD_DATA(1100,"接收心跳信息"),

    MATCH_OPPONENT_RESP(2001, "匹配对手返回"),
    START_GAME_RESP(2002,"开始游戏"),
    ;

    private int code;
    private String msg;

    MessageTypeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static MessageTypeEnum getFromKey(int key) {
        if (key == 0){
            return null;
        }
        for (MessageTypeEnum typeEnum : MessageTypeEnum.values()){
            if (typeEnum.getCode() == key){
                return typeEnum;
            }
        }
        return null;
    }
}
