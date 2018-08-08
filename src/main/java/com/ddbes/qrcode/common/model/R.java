package com.ddbes.qrcode.common.model;

/**
 * 响应code,msg
 * Created by daitian on 2018/4/17.
 */
public enum R {
    /**基础状态码*/
    SUCCESS(1, "请求成功"),
    NET_ERROR(-1, "网络异常"),
    BAD_REQ(0,"请求错误!"),
    ERROR_REDIS(1001,"redis存储失败!"),
    ERROR_REDIS_EXIST(1002,"redisKey存在!"),
    ERROR_IM(1013,"短信发送失败!"),

    /**用户相关错误码  1100开始*/
    USER_REGISTER(1100,"手机号已经注册"),
    USER_EDIT(1101,"待完善信息!"),
    USER_FREEZE(1102,"该账号被被冻结!"),
    USER_CODE_ERROR(1103,"验证码错误!"),
    USER_LOGIN_ERROR(1104,"手机号未注册!"),
    USER_LOGIN_ERROR2(1105,"密码错误!"),
    USER_LOGIN_ERROR3(1106,"验证码已发送,请稍后再试!"),
    USER_LOGIN_ERROR4(1107,"验证码失效,请重新发送!"),
    USER_LOGIN_AUTH(1108,"更换设备安全验证!"),
    QRCODE_ERROR(1111,"二维码失效!"),
    QRCODE_CODE_ERROR(1112,"code失效!"),
    QRCODE_URL_ERROR(1113,"请输入redirectUrl或者在平台配置失效!"),
    QRCODE_REDIRECT_ERROR(1114,"平台配置回调地址跟请求中不匹配!"),


    USER_INFO_NO(1106,"用户信息不存在!"),

    /**好友*/
    FRIEND_APPLY_ERROR(1201,"好友申请上限"),
    FRIEND_ALREADY_ERROR(1201,"已经是好友了!"),

    /**组织*/

    /***/

    /**自定义异常码*/
    TOKEN_FAIL(2000,"认证失败!"),
    TOKEN_AUTO(2001,"自动重新登录!"),
    TOKEN_LOGIN(2002,"在其他地方登录了!"),
    TOKEN_LONG(2003,"长时间不登录,过期了!");
    private int code;
    private String msg;

    R(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
