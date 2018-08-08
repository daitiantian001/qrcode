package com.ddbes.qrcode.controller;

import com.ddbes.qrcode.common.model.R;
import com.ddbes.qrcode.common.model.Result;
import com.ddbes.qrcode.common.util.HttpKit;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by daitian on 2018/7/30.
 */
@RestController
@RequestMapping("notify/ddbes")
public class TestQRCodeDemo {

    @Autowired
    private HttpKit httpKit;

    @ApiOperation("回调url")
    @GetMapping("code")
    public Result scanQRCode(@RequestParam("code") Long code, @RequestParam("state") String state) throws Exception {
        System.out.println("code=" + code + ",state=" + state);
        //TODO 获取id,accessToken
        Map map = new HashMap<>();
        map.put("appId","1");
        map.put("secret","1");
        map.put("code",code==null?"":code);
        String s = httpKit.doGet("http://192.168.1.28:8301/plat/open/oauth2/access_token", map);
        System.out.println(s);
        return new Result(R.SUCCESS,s);
    }

}
