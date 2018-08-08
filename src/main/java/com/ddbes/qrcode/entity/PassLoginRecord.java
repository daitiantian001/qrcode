package com.ddbes.qrcode.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import java.util.Date;

@Data
public class PassLoginRecord {

    public static final Integer RETURN=1;//返回生成二维码
    public static final Integer FINISH=2;//扫码动作完成

    @Id
    @ApiModelProperty("主键")
    private Long id;
    @ApiModelProperty("应用唯一标识")
    private String appId;
    @ApiModelProperty("跳转链接")
    private String redirectUri;
    @ApiModelProperty("code")
    private String responseType;
    @ApiModelProperty("应用授权作用域，拥有多个作用域用逗号（,）分隔，网页应用目前仅填写snsapi_login")
    private String scope;
    @ApiModelProperty("校验参数")
    private String state;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("1.返回二维码 2.扫码完成 4.确认按钮 5.取消按钮 6")
    private Integer status;
}