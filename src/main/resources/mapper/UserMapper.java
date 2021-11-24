package com.sun.jollygame.mapper;

import com.sun.jollygame.entity.po.DataUserPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author sunkai
 * @since 2021/3/17 11:19 上午
 */
@Mapper
public interface UserMapper {

    List<DataUserPo> listUser(DataUserPo user);


    @Select("select count(*) from user")
    int countUser();

    @Select("select user_id userId,user_name userName,user_address userAddress from user limit #{startIndex},#{pageSize}")
    List<DataUserPo> listUserByConditon(@Param("startIndex") int startIndex, @Param("pageSize") int pageSize);
}
