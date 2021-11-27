package com.sun.jollygame.entity.response;

import lombok.Data;

import java.util.List;

@Data
public class MatchResponse {

    private List<Integer> imgIdlist;

    private int enemyHeadImgId;

    public MatchResponse(List<Integer> imgIdlist, int enemyHeadImgId) {
        this.imgIdlist = imgIdlist;
        this.enemyHeadImgId = enemyHeadImgId;
    }
}
