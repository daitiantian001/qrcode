package com.ddbes.qrcode.service;

import com.ddbes.qrcode.common.model.Result;
import com.ddbes.qrcode.entity.DdbesUserInfo;

/**
 * Created by daitian on 2018/7/30.
 */
public interface IUserInfoService {
    DdbesUserInfo findUserNameByUserId(String userId);

    Result findBaseUserInfo(String userId);
}
