package com.sun.jollygame.entity.response;

import lombok.Data;

@Data
public class GameOverResponse extends BaseResponse{

    private static final long serialVersionUID = 7711641960361296231L;
    /**
     * 是否赢了  0 :平局   1：获胜 2：失败
     */
    private int isWin;
    /**
     * 成绩
     */
    private int grade;

    public GameOverResponse(int isWin, int grade) {
        this.isWin = isWin;
        this.grade = grade;
    }
}
