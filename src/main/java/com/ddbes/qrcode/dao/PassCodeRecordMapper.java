package com.ddbes.qrcode.dao;

import com.ddbes.qrcode.entity.PassCodeRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * Created by daitian on 2018/7/26.
 */
@Mapper
public interface PassCodeRecordMapper extends MyMapper<PassCodeRecord> {

    @Update("UPDATE pass_code_record SET uuid=#{uuid} WHERE id=#{id}")
    void updateUuidById(@Param("id") long id, @Param("uuid") String sessionId);
}
