package com.sun.jollygame.entity.po;

import lombok.Data;

import java.io.Serializable;

@Data
public class DataUserPo implements Serializable {

    private static final long serialVersionUID = -2466628295029075731L;

    private int userId;

    private String username;

    private String userAddress;

    private long flagBin;

    private Boolean isFromCopy;
}
