package com.ddbes.qrcode.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by daitian on 2018/7/27.
 */
@Data
public class ThirdInfo implements Serializable{
    private String authUrl;
    private String scope;
    private String name;
    private String logo;
}
