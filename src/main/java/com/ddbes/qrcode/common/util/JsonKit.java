package com.ddbes.qrcode.common.util;

import com.ddbes.qrcode.common.model.Result;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by daitian on 2018/4/21.
 */
public class JsonKit {

    public static final ObjectMapper objectMapper = new ObjectMapper();

    public static String toJson(Object o) {
        try {
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public static Result toObj(String json){
        try {
            return objectMapper.readValue(json,Result.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
