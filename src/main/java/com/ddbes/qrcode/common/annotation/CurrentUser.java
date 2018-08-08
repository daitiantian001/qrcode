package com.ddbes.qrcode.common.annotation;

import lombok.Data;

/**
 * Created by daitian on 2018/7/10.
 */
@Data
public class CurrentUser {
    public static final String CURRENT_USER="CurrentUser";
    private Long userId;
    private String version;
    private Integer client;
}
