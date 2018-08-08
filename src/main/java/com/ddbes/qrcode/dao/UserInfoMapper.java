package com.ddbes.qrcode.dao;

import com.ddbes.qrcode.entity.BaseUserInfo;
import com.ddbes.qrcode.entity.DdbesUserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by daitian on 2018/7/30.
 */
@Mapper
public interface UserInfoMapper extends MyMapper<DdbesUserInfo>{

    @Select("SELECT user_name userName,headimg FROM ddbes_user_info WHERE user_id=#{userId}")
    DdbesUserInfo findUserName(@Param("userId")String userId);

    @Select("SELECT user_name userName,headimg,sex FROM ddbes_user_info WHERE user_id=#{userId}")
    BaseUserInfo findUserInfo(@Param("userId")String userId);
}
