package com.sun.jollygame.util;

import com.sun.jollygame.entity.UserGameRecord;
import com.sun.jollygame.entity.enumc.MessageTypeEnum;
import com.sun.jollygame.entity.response.GameOverResponse;
import com.sun.jollygame.entity.response.GradeAddResponse;
import com.sun.jollygame.entity.response.MatchResponse;

public class GameUtil {

    public static GameOverResponse getGameOverResponse(UserGameRecord userGameRecord){

        GameOverResponse response = new GameOverResponse(0,userGameRecord.getGrade());
        response.setUserId(userGameRecord.getUserId());
        response.setMessageType(MessageTypeEnum.GAME_OVER_RESP.getCode());
        return response;
    }

    public static GradeAddResponse getGradeAddResponse(int grade,String userId){
        GradeAddResponse response = new GradeAddResponse(grade);
        response.setUserId(userId);
        response.setMessageType(MessageTypeEnum.GRADE_ADD_RESP.getCode());
        return response;
    }

    public static MatchResponse getMatchResponse(String userId, int messageType){
        MatchResponse response = new MatchResponse();
        response.setUserId(userId);
        response.setMessageType(messageType);

        return response;
    }
}
