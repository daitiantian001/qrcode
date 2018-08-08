package com.ddbes.qrcode.service.impl;

import com.corundumstudio.socketio.SocketIOServer;
import com.ddbes.qrcode.common.model.ClientEnum;
import com.ddbes.qrcode.common.model.R;
import com.ddbes.qrcode.common.model.Result;
import com.ddbes.qrcode.common.model.S;
import com.ddbes.qrcode.common.util.*;
import com.ddbes.qrcode.controller.QRCodeController;
import com.ddbes.qrcode.dao.PassCodeRecordMapper;
import com.ddbes.qrcode.dao.QRCodeMapper;
import com.ddbes.qrcode.entity.AccessTokenVO;
import com.ddbes.qrcode.entity.DdbesUserInfo;
import com.ddbes.qrcode.entity.PassCodeRecord;
import com.ddbes.qrcode.entity.PassLoginRecord;
import com.ddbes.qrcode.service.IQRCodeService;
import com.ddbes.qrcode.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by daitian on 2018/7/25.
 */
@Service
public class QRCodeService implements IQRCodeService {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private QRCodeMapper qrCodeMapper;

    @Autowired
    private PassCodeRecordMapper passCodeRecordMapper;

    @Autowired
    private HttpKit httpKit;

    @Autowired
    private RedisKit redisKit;

    @Autowired
    private JwtKit jwtKit;

    @Autowired
    SocketIOServer socketIOServer;

    @Autowired
    private IUserInfoService userInfoService;

    @Value("${conf.third.host}")
    private String tHost;
    @Value("${conf.third.port}")
    private String tPort;

    @Value("${conf.self.host}")
    private String sHost;
    @Value("${conf.self.port}")
    private String sPort;

    @Override
    public Result getQRCode(PassLoginRecord passLoginRecord) throws Exception {
        Long id = idWorker.nextId();
//        Long appId = passLoginRecord.getAppId();
        Integer type;
        switch (passLoginRecord.getResponseType()) {
            case "code":
                type = PassCodeRecord.THIRD;
                break;
            default:
                type = 0;
                break;
        }
        //TODO feign
        String u = StrKit.append("http://",tHost,":",tPort,"/check/",passLoginRecord.getAppId());
        String s = httpKit.doGet(u);
        if(StrKit.isBlank(s)){
            return new Result("平台调用失败!");
        }

        //TODO scope校验!
        Result result = JsonKit.toObj(s);
        String name, logo,scope,authUrl;
        if (result.getCode() == 1) {
            LinkedHashMap linkedHashMap = (LinkedHashMap) result.getData();
            name = linkedHashMap.get("name").toString();
            logo = linkedHashMap.get("logo").toString();
            scope=linkedHashMap.get("scope").toString();
            authUrl=linkedHashMap.get("authUrl").toString();
        } else {
            return result;
        }

        String url = StrKit.append(id);
        passLoginRecord.setId(id);
        passLoginRecord.setStatus(PassLoginRecord.RETURN);
        passLoginRecord.setCreateTime(new Date());

        if(StrKit.isBlank(passLoginRecord.getRedirectUri())){
            if(!StrKit.isBlank(authUrl)){
                passLoginRecord.setRedirectUri(authUrl);
            }else{
                return new Result(R.QRCODE_URL_ERROR);
            }
        }

        if(authUrl!=null&&!StrKit.isBlank(passLoginRecord.getRedirectUri()) && !URLDecoder.decode(passLoginRecord.getRedirectUri(), "UTF-8").startsWith(authUrl)){
            return new Result(R.QRCODE_REDIRECT_ERROR);
        }

        qrCodeMapper.insertSelective(passLoginRecord);

        PassCodeRecord passCodeRecord = new PassCodeRecord();
        passCodeRecord.setId(id);
        passCodeRecord.setContent("授权登录" + name);
        passCodeRecord.setCreateTime(new Date());
        passCodeRecord.setMsg("");
        passCodeRecord.setLogo(logo);
        passCodeRecord.setType(type);
        passCodeRecord.setUrl(StrKit.append("http://",sHost,":",sPort,"/plat/open/deal"));
        passCodeRecordMapper.insertSelective(passCodeRecord);

        return new Result(R.SUCCESS, url);
    }

    @Override
    public Result scanQRCode(Long targetId, String userId) {
        PassCodeRecord passCodeRecord = (PassCodeRecord) passCodeRecordMapper.selectByPrimaryKey(targetId);
        if (passCodeRecord == null) {
            return new Result("二维码失效!");
        }
        switch (passCodeRecord.getType()) {
            case 3:
                PassLoginRecord pc = new PassLoginRecord();
                pc.setId(targetId);
                pc.setStatus(PassLoginRecord.FINISH);
                qrCodeMapper.updateByPrimaryKeySelective(pc);
                PassCodeRecord pc1 = (PassCodeRecord) passCodeRecordMapper.selectByPrimaryKey(targetId);
                //根据用户id查询头像
                DdbesUserInfo ddbesUserInfo =userInfoService.findUserNameByUserId(userId);
                try {
                    //头像发给二维码页面
                    socketIOServer.getClient(UUID.fromString(pc1.getUuid())).sendEvent("msg", ddbesUserInfo.getHeadimg());
                } catch (Exception e) {
                    return new Result(R.QRCODE_ERROR);
                }
                return new Result(R.SUCCESS, passCodeRecord);
            case 1:
            case 2:
                return new Result("待实现!");
            default:
                return new Result("二维码解析失败!");
        }

    }

    @Override
    public Result dealQRCode(QRCodeController.DealQRCodeModel dealQRCodeModel) throws Exception {
        //确定
        PassLoginRecord passLoginRecord = (PassLoginRecord) qrCodeMapper.selectByPrimaryKey(dealQRCodeModel.getTargetId());
        //更新用户id
        PassCodeRecord passCodeRecord = new PassCodeRecord();
        passCodeRecord.setId(dealQRCodeModel.getTargetId());
        passCodeRecord.setUserId(dealQRCodeModel.getUserId());
        passCodeRecordMapper.updateByPrimaryKeySelective(passCodeRecord);
        PassCodeRecord pc = (PassCodeRecord) passCodeRecordMapper.selectByPrimaryKey(dealQRCodeModel.getTargetId());
        Map map = new HashMap();
        if (dealQRCodeModel.getType()) {
            //TODO  判断是否有权限
            String code = StrKit.getCode();
            redisKit.set(StrKit.append(S.QR_CODE_TEMP.getValue(), passLoginRecord.getAppId(), code), dealQRCodeModel.getTargetId(), "2", TimeUnit.MINUTES);
            map.put("code", code);
            map.put("state", passLoginRecord.getState());
            map.put("redirectUrl",URLDecoder.decode(passLoginRecord.getRedirectUri(), "UTF-8"));
//            httpKit.doGet(URLDecoder.decode(passLoginRecord.getRedirectUri(), "UTF-8"), map);
            //TODO 发消息,获取用户头像
            socketIOServer.getClient(UUID.fromString(pc.getUuid())).sendEvent("finish", map);
        } else {
            map.put("state", passLoginRecord.getState());
            map.put("code", "");
            map.put("redirectUrl",URLDecoder.decode(passLoginRecord.getRedirectUri(), "UTF-8"));
            socketIOServer.getClient(UUID.fromString(pc.getUuid())).sendEvent("finish", map);
//            httpKit.doGet(URLDecoder.decode(passLoginRecord.getRedirectUri(), "UTF-8"), map);
        }
        return new Result(R.SUCCESS);
    }

    @Override
    public Result oauth2(QRCodeController.ThirdAuthModel thirdAuthModel) throws Exception {

        //redis中获取信息
        Long targetId = (Long) redisKit.get(StrKit.append(S.QR_CODE_TEMP.getValue(), thirdAuthModel.getAppId(), thirdAuthModel.getCode()));
        if (targetId == null) {
            return new Result(R.QRCODE_CODE_ERROR);
        }
        PassLoginRecord passLoginRecord = (PassLoginRecord) qrCodeMapper.selectByPrimaryKey(targetId);
        PassCodeRecord passCodeRecord = (PassCodeRecord) passCodeRecordMapper.selectByPrimaryKey(targetId);
        //TODO 验证 appId secret
        String s = httpKit.doGet(StrKit.append("http://" + tHost, ":", tPort, "/check/appInfo/", passLoginRecord.getAppId(), "/", thirdAuthModel.getSecret()));
        Result result = JsonKit.toObj(s);

        if(result.getCode()!=1){
            return result;
        }

        AccessTokenVO accessTokenVO = new AccessTokenVO();
        accessTokenVO.setOpenId(passCodeRecord.getUserId());
        accessTokenVO.setScope(passLoginRecord.getScope());

        //生成token
        Map claims = new HashMap();
        claims.put("userId", passCodeRecord.getUserId());
        claims.put("version", "1.6");
        //苹果安卓 放1
        claims.put("clientType", ClientEnum.PASS.getValue());
        claims.put("client", 3);//3开发平台生成.
        String token = jwtKit.createSimpleJWT(claims);
        accessTokenVO.setAccessToken(token);
        accessTokenVO.setExpiresIn(7 * 24 * 60 * 60 + "");
        return new Result(R.SUCCESS, accessTokenVO);
    }
}
