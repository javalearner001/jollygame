package com.sun.jollygame.entity;

import lombok.Data;

/**
 * @author sunkai
 * @since 2021/11/25 3:48 下午
 */
@Data
public class GameRoom {

    private String roomId;

    private String firstUserId;

    private String secondUserId;

    private UserGameRecord userGameRecordFirst;

    private UserGameRecord userGameRecordSecond;

    public GameRoom(String firstUserId, String secondUserId) {
        this.firstUserId = firstUserId;
        this.secondUserId = secondUserId;
        this.roomId = firstUserId + "-" + secondUserId;
    }

    public UserGameRecord getUserRecord(String userId){
        if (userId.equals(this.userGameRecordFirst.getUserId())){
            return this.userGameRecordFirst;
        }else {
            return this.userGameRecordSecond;
        }
    }
}
