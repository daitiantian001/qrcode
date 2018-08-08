package com.ddbes.qrcode.controller;

import com.ddbes.qrcode.common.model.R;
import com.ddbes.qrcode.common.model.Result;
import com.ddbes.qrcode.service.IUserInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by daitian on 2018/7/30.
 */
@RestController
@RequestMapping("open/user")
public class UserController {

    @Autowired
    private IUserInfoService iUserInfoService;

    @ApiOperation("获取用户信息")
    @GetMapping("base")
    public Result findBaseUserInfo(String userId){
        return iUserInfoService.findBaseUserInfo(userId);
    }

}
