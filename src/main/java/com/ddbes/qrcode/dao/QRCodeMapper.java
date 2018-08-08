package com.ddbes.qrcode.dao;

import com.ddbes.qrcode.entity.PassLoginRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by daitian on 2018/7/25.
 */
@Mapper
public interface QRCodeMapper extends MyMapper<PassLoginRecord> {
}
