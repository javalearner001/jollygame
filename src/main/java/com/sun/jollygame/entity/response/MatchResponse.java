package com.sun.jollygame.entity.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class MatchResponse extends BaseResponse implements Serializable {

    private static final long serialVersionUID = -5038176584990064751L;

    private List<Integer> imgIdlist;

    private int enemyHeadImgId;

    private Boolean single = false;

}
