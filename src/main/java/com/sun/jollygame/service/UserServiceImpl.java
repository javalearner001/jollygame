package com.sun.jollygame.service;

import com.alibaba.fastjson.JSON;
import com.sun.jollygame.entity.po.DataUserPo;
import com.sun.jollygame.entity.response.BaseResult;
import com.sun.jollygame.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author sunkai
 * @since 2021/3/17 11:18 上午
 */
@Service
public class UserServiceImpl implements IUserservice{

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

   /* ThreadPoolExecutor executor = new ThreadPoolExecutor(2,5,1000, TimeUnit.SECONDS,new ArrayBlockingQueue<>(100));
*/

    @Override
    public BaseResult<List<DataUserPo>> getUserList() {
        BaseResult<List<DataUserPo>> result = new BaseResult();
        DataUserPo user = new DataUserPo();
        user.setFlagBin(4);
        List<DataUserPo> users = userMapper.listUser(user);

        logger.info("getUserList res={}", JSON.toJSONString(result));
        result.setData(users);
        return result;
    }

}
