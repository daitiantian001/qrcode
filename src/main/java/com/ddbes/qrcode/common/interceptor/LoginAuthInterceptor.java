package com.ddbes.qrcode.common.interceptor;

import com.ddbes.qrcode.common.annotation.CurrentUser;
import com.ddbes.qrcode.common.exception.MyException;
import com.ddbes.qrcode.common.model.R;
import com.ddbes.qrcode.common.model.S;
import com.ddbes.qrcode.common.util.JwtKit;
import com.ddbes.qrcode.common.util.RedisKit;
import com.ddbes.qrcode.common.util.StrKit;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 登录拦截
 * Created by daitian on 2018/7/10.
 */
public class LoginAuthInterceptor implements HandlerInterceptor {

    public static final String ACCESS_TOKEN = "accessToken";

    @Autowired
    JwtKit jwtKit;

    @Autowired
    RedisKit redisKit;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {

        String accessToken = httpServletRequest.getHeader(ACCESS_TOKEN);
        if (accessToken == null) {
            accessToken = httpServletRequest.getParameter(ACCESS_TOKEN);
        }
        if (accessToken == null) {
            throw new MyException(R.TOKEN_FAIL);
        }

        //auth
        Claims claims = jwtKit.parseJWT(accessToken);
        if(claims==null){
            throw new MyException(R.TOKEN_FAIL);
        }
        Integer client = claims.get("client", Integer.class);
        Integer clientType = claims.get("clientType", Integer.class);
        Long userId = claims.get("userId", Long.class);
        String version = claims.get("version", String.class);

        Date issuedAt = claims.getIssuedAt();
        String redisTokenKey = StrKit.append(S.USER_TOKEN.getValue(), clientType, ":", userId);

        //2.获取缓存时间
        Date rDate = (Date) redisKit.get(redisTokenKey);

        if(rDate==null){
            throw new MyException(R.TOKEN_LONG);
        }

        //颁发时间相同
        if (rDate.getTime() - issuedAt.getTime() <= 1000) {
            Date expiration = claims.getExpiration();
            //token过期
            if (new Date().compareTo(expiration) > 0) {
                boolean b = redisKit.isExpire(redisTokenKey);
                if (b) {
                    throw new MyException(R.TOKEN_AUTO);
                } else {
                    //重新登录,长时间不使用,过期了
                    throw new MyException(R.TOKEN_LONG);
                }

            } else {
                //不过期,刷新缓存时间
                redisKit.flushExpire(redisTokenKey, S.USER_TOKEN.getExpire(), TimeUnit.DAYS);
            }
        } else {
            //重新登录,在其他地方登录
            throw new MyException(R.TOKEN_LOGIN);
        }

        //抽取用户id,版本,登录设备类型.
        CurrentUser currentUser = new CurrentUser();
        currentUser.setUserId(userId);
        currentUser.setClient(client);
        currentUser.setVersion(version);
        httpServletRequest.setAttribute(CurrentUser.CURRENT_USER, currentUser);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
