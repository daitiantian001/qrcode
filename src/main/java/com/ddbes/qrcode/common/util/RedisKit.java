package com.ddbes.qrcode.common.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by daitian on 2018/5/31.
 */
@Component
public class RedisKit {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 设置对象（Hash结构）
     *
     * @param key
     * @param o
     */
    public void hset(Object key, Object o) {
        redisTemplate.opsForHash().putAll(key, ConvertKit.obj2map(o));
    }

    /**
     * 设置对象属性（Hash结构）
     *
     * @param key       redisKey
     * @param hashKey   属性
     * @param hashValue 值
     */
    public void hset(Object key, Object hashKey, Object hashValue) {
        redisTemplate.opsForHash().put(key, hashKey, hashValue);
    }

    /**
     * 获取对象属性 （Hash结构）
     *
     * @param key     redisKey
     * @param hashKey 属性
     * @return
     */
    public Object hget(Object key, Object hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * 获取对象
     *
     * @param key redisKey
     * @param t   对象引用
     * @param <T> 对象类型
     * @return 传入对象
     */
    public <T> T hget(String key, T t) {
        Field[] fields = t.getClass().getDeclaredFields();
        Field.setAccessible(fields, true);
        for (Field field : fields) {
            try {
                Object o = redisTemplate.opsForHash().get(key, field.getName());
                field.set(t, o);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return t;
    }

    public void setStr(String key, String value, String time) {
        redisTemplate.opsForValue().set(key, value, Long.parseLong(time), TimeUnit.MINUTES);
    }

    public void set(String key, Object value, String time,TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, Long.parseLong(time), timeUnit);
    }

    public void flushExpire(String key,Long time,TimeUnit t){
        redisTemplate.expire(key,time,t);
    }

    public boolean isExpire(String key){
        return redisTemplate.hasKey(key);
    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public boolean setAt(String key, String value, Date expire) {
        boolean result= false;
        redisTemplate.opsForValue().set(key,value);
        if (expire.getTime()>new Date().getTime()) {
            result = redisTemplate.expireAt(key, expire);
        }
        return result;
    }

    public void setExpireAt(String key,Date expire){
        redisTemplate.expireAt(key, expire);
    }


}
