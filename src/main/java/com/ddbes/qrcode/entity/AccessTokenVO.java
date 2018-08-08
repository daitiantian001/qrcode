package com.ddbes.qrcode.entity;

import lombok.Data;

/**
 * Created by daitian on 2018/7/26.
 */
@Data
public class AccessTokenVO {
    private String accessToken;
//    private String refreshToken;
    private String expiresIn;
    private String openId;
    private String scope;
}
