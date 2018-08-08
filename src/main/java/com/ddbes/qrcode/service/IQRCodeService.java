package com.ddbes.qrcode.service;


import com.ddbes.qrcode.common.model.Result;
import com.ddbes.qrcode.controller.QRCodeController;
import com.ddbes.qrcode.entity.PassLoginRecord;

/**
 * Created by daitian on 2018/7/25.
 */
public interface IQRCodeService {
    Result getQRCode(PassLoginRecord passLoginRecord) throws Exception;

    Result scanQRCode(Long targetId, String userId);

    Result dealQRCode(QRCodeController.DealQRCodeModel dealQRCodeModel) throws Exception;

    Result oauth2(QRCodeController.ThirdAuthModel thirdAuthModel) throws Exception;
}
