package com.sun.jollygame.entity.response;

import lombok.Data;

@Data
public class BoolResponse {

    private Boolean single;

    public BoolResponse(Boolean single) {
        this.single = single;
    }
}
