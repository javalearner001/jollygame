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
    MATCH_OPPONENT_SUCCESS(2002,"匹配成功"),
    MATCH_OPPONENT_FAIL(2002,"匹配失败，单人游戏"),
    GAME_READY_RESP(2003, "玩家都已准备好"),
    GRADE_ADD_RESP(2004, "分数增加返回对方"),
    ONLINE_OUT_RESP(2005,"游戏中途对手掉线，单人游戏"),
    GAME_OVER_RESP(2021,"游戏结束结果"),
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
