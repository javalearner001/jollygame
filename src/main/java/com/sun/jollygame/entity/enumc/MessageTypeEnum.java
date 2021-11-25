package com.sun.jollygame.entity.enumc;

/**
 * @author sunkai
 * @since 2021/11/5 5:44 下午
 */
public enum MessageTypeEnum {
    BROAD_CAST(100, "广播消息"),
    MATCH_OPPONENT(1001, "匹配对手"),
    START_GAME(1002,"开始游戏"),
    CHECK_BOARD_DATA(2000,"接收心跳信息"),
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
}
