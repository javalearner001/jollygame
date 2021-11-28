package com.sun.jollygame.entity.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseResponse implements Serializable {
    private static final long serialVersionUID = -2155372755524163642L;

    private String userId;

    private int messageType;

    private String message;

}
