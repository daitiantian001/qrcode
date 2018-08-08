package com.ddbes.qrcode.dao;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * Created by daitian on 2018/4/15.
 */
public interface MyMapper<T> extends Mapper,MySqlMapper<T> {
}
