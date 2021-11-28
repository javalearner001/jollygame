package com.sun.jollygame.entity;

import lombok.Data;

import java.util.Date;

/**
 * 用户游戏记录
 *
 * @author sunkai
 * @since 2021/11/26 3:03 下午
 */
@Data
public class UserGameRecord {

    /**
     * 用户id 此处为sessionId 存储
     */
    private String userId;
    /**
     * 用户分数
     */
    private int grade;
    /**
     * 用户头像id
     */
    private int headImgId;
    /**
     * 用户匹配时间
     */
    private Date matchTime;
    /**
     * 是否准备好
     */
    private Boolean ready = false;

    public UserGameRecord(String userId) {
        this.userId = userId;
    }
}
