package com.ddbes.qrcode.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import java.util.Date;

@Data
public class PassCodeRecord {

    public static final Integer FRIEND=1;
    public static final Integer GROUP=2;
    public static final Integer THIRD=3;

    @Id
    @ApiModelProperty("二维码标识")
    private Long id;
    @ApiModelProperty("接口url")
    private String url;
    @ApiModelProperty("按钮内显示内容")
    private String content;
    @ApiModelProperty("额外信息")
    private String msg;
    @ApiModelProperty("展示图标")
    private String logo;
    @ApiModelProperty("记录生成时间")
    private Date createTime;
    @ApiModelProperty("1.好友二维码 2.群组二维码 3.第三方登录二维码")
    private Integer type;
    @ApiModelProperty("用户id")
    private String userId;
    @ApiModelProperty("socket UUID")
    private String uuid;
}