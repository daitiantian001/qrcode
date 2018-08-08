package com.ddbes.qrcode.service.impl;

import com.ddbes.qrcode.common.model.R;
import com.ddbes.qrcode.common.model.Result;
import com.ddbes.qrcode.dao.UserInfoMapper;
import com.ddbes.qrcode.entity.BaseUserInfo;
import com.ddbes.qrcode.entity.DdbesUserInfo;
import com.ddbes.qrcode.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by daitian on 2018/7/30.
 */
@Service
public class userInfoService implements IUserInfoService{

    @Autowired
    UserInfoMapper userInfoMapper;

    @Override
    public DdbesUserInfo findUserNameByUserId(String userId) {
        return userInfoMapper.findUserName(userId);
    }

    @Override
    public Result findBaseUserInfo(String userId) {
        BaseUserInfo baseUserInfo=userInfoMapper.findUserInfo(userId);
        return new Result(R.SUCCESS,baseUserInfo);
    }
}
