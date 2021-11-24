package com.sun.jollygame.controller;


import com.sun.jollygame.service.IUserservice;
import com.sun.jollygame.entity.po.DataUserPo;
import com.sun.jollygame.entity.response.BaseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author sunkai
 * @since 2021/3/17 11:29 上午
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private IUserservice userservice;

    @GetMapping("/listUser")
    public BaseResult<List<DataUserPo>> listUser(){
        return userservice.getUserList();
    }
}
