package com.ddbes.qrcode.common.model;

import lombok.Data;

/**
 * 响应实体类
 * Created by daitian on 2018/4/17.
 */
@Data
public class Result<T> {
    private int code;
    private String msg;
    private T data;

    public Result() {
        this.code=R.SUCCESS.getCode();
        this.msg=R.SUCCESS.getMsg();
    }
    public Result(R r) {
        this.code = r.getCode();
        this.msg = r.getMsg();
    }
    public Result(R r, T data) {
        this(r);
        this.data = data;
    }

    public Result(String msg) {
        this.code = R.BAD_REQ.getCode();
        this.msg = msg;
    }
}
