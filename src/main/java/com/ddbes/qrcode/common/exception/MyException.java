package com.ddbes.qrcode.common.exception;

import com.ddbes.qrcode.common.model.R;
import lombok.Data;

/**
 * Created by daitian on 2018/4/17.
 */
@Data
public class MyException extends RuntimeException {
    private R r;
    public MyException(R r) {
        super(r.getMsg());
        this.r = r;
    }
}
