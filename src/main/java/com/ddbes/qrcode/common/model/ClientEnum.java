package com.ddbes.qrcode.common.model;

/**
 * Created by daitian on 2018/7/17.
 */
public enum ClientEnum {
    APP("手机端",1),
    PASS("开放平台",2);
    private String name;
    private Integer value;

    ClientEnum(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
