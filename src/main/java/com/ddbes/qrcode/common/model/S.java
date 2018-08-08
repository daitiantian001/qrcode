package com.ddbes.qrcode.common.model;

/**
 * redis主键管理
 * Created by daitian on 2018/5/29.
 */
public enum S {
    FRIEND_APPLY_COUNT("申请好友次数","user:friend:applyCount:",1L),

    USER_INFO("用户信息", "user:info:", -1L),
    USER_TOKEN("验证码","user:login:token:",30L),
    MSG_CODE("验证码","user:sign:msg:",2L),
    QR_CODE_TEMP("二维码","user:sign:temp:",10L);
    private String name;
    private String value;
    private Long expire;

    S(String name, String value, Long expire) {
        this.name = name;
        this.value = value;
        this.expire = expire;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getExpire() {
        return expire;
    }

    public void setExpire(Long expire) {
        this.expire = expire;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
