package com.sun.jollygame.entity.response;

import lombok.Data;

@Data
public class GradeAddResponse extends BaseResponse{
    /**
     * 分数
     */
    private int grade;

    public GradeAddResponse(int grade) {
        this.grade = grade;
    }
}
