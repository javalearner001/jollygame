package com.sun.jollygame.service;



import com.sun.jollygame.entity.po.DataUserPo;
import com.sun.jollygame.entity.response.BaseResult;

import java.util.List;

/**
 * @author sunkai
 * @since 2021/3/17 11:13 上午
 */
public interface IUserservice {

    BaseResult<List<DataUserPo>> getUserList();
}
