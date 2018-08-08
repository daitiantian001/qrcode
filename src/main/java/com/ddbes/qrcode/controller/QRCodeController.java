package com.ddbes.qrcode.controller;

import com.ddbes.qrcode.common.model.R;
import com.ddbes.qrcode.common.model.Result;
import com.ddbes.qrcode.common.model.S;
import com.ddbes.qrcode.entity.PassLoginRecord;
import com.ddbes.qrcode.service.IQRCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * Created by daitian on 2018/7/30.
 */
@Api(value = "扫码接口", tags = {"扫码接口"}, description = "扫码登录相关接口")
@RestController
@RequestMapping("plat/open")
public class QRCodeController {

    @Autowired
    private IQRCodeService iqrCodeService;

    @ApiOperation("获取二维码")
    @PostMapping("show")
    public Result getQRCode(@RequestBody @Valid AuthorizationInfoModel authorizationInfoModel) throws Exception {
        //TODO 限流,记录调用频率,次数,时间
        PassLoginRecord passLoginRecord = new PassLoginRecord();
        BeanUtils.copyProperties(authorizationInfoModel, passLoginRecord);
        return iqrCodeService.getQRCode(passLoginRecord);
    }

    @ApiOperation("担当app扫描二维码")
    @GetMapping("scan/{targetId}/{userId}")
    public Result scanQRCode(@PathVariable @Valid @NotNull(message = "二维码不能为空!") Long targetId,@PathVariable @Valid @NotNull(message = "用户id不能为空!") String userId) {
        return iqrCodeService.scanQRCode(targetId,userId);
    }

    @ApiOperation("担当二维码确定/取消")
    @PostMapping("deal")
    public Result dealQRCode(@RequestBody @Valid DealQRCodeModel dealQRCodeModel){
        try {
            return iqrCodeService.dealQRCode(dealQRCodeModel);
        } catch (Exception e) {
            return new Result(R.QRCODE_ERROR);
        }
    }

    @ApiOperation("公共认证接口")
    @GetMapping("oauth2/access_token")
    public Result oauth2(@NotNull(message = "请填写appId!")String appId,@NotNull(message = "code不能为空") Long code, @NotNull(message = "秘钥不能为空")String secret, @NotNull(message = "grantType不能为空")String grantType) throws Exception {
        ThirdAuthModel thirdAuthModel = new ThirdAuthModel();
        thirdAuthModel.setAppId(appId);
        thirdAuthModel.setCode(code);
        thirdAuthModel.setSecret(secret);
        thirdAuthModel.setGrantType(grantType);
        return iqrCodeService.oauth2(thirdAuthModel);
    }

    @Data
    public static class AuthorizationInfoModel implements Serializable {
        @ApiModelProperty("应用唯一标识")
        @NotNull(message = "请填写appId!")
        private String appId;//todo dd+num ???
        @ApiModelProperty("跳转链接")
        @NotNull(message = "请输入跳转链接!")
        @Pattern(regexp = "^[a-zA-z]+%3[Aa]%2[Ff]%2[Ff][^\\s]*$", message = "请使用urlEncode对链接进行处理!")
        private String redirectUri;
        @ApiModelProperty("填code")
        @NotNull(message = "response_type不能为空")
        @Pattern(regexp = "^code$", message = "填写code")
        private String responseType;//code
        @ApiModelProperty("应用授权作用域，拥有多个作用域用逗号（,）分隔，网页应用目前仅填写snsapi_login")
        private String scope;
        @ApiModelProperty("用于保持请求和回调的状态，授权请求后原样带回给第三方。该参数可用于防止csrf攻击（跨站请求伪造攻击），建议第三方带上该参数，可设置为简单的随机数加session进行校验")
        private String state;
    }

    @Data
    public static class DealQRCodeModel implements Serializable {
        @ApiModelProperty("应用唯一标识")
        @NotNull(message = "请填写用户Id!")
        private String userId;
        @ApiModelProperty("请选择确定/取消")
        @NotNull
        private Boolean type;
        @ApiModelProperty("二维码内容")
        @NotNull
        private Long targetId;
        @ApiModelProperty("app版本")
        private String version;
        @ApiModelProperty("1.ios 2.安卓")
        private String client;
    }

    @Data
    public static class ThirdAuthModel implements Serializable {
        @ApiModelProperty("应用唯一标识，在微信开放平台提交应用审核通过后获得")
        @NotNull(message = "标识不能为空!")
        private String appId;
        @ApiModelProperty("应用密钥AppSecret，在微信开放平台提交应用审核通过后获得")
        @NotNull(message = "秘钥不能为空")
        private String secret;
        @ApiModelProperty("填写获取的code参数")
        @NotNull(message = "code不能为空")
        private Long code;
        @ApiModelProperty("authorization_code")
        @NotNull(message = "grantType不能为空")
        private String grantType;
    }

}

