package com.sun.jollygame.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author sunkai
 * @since 2021/11/25 5:39 下午
 */
@Data
public class HeartBeat {

    /**
     * 用户id
     */
    private String userId;
    /**
     * 首次发送心跳时间
     */
    private Date firstSendTime;
    /**
     * 返回时间
     */
    private Date heartTime;
}
