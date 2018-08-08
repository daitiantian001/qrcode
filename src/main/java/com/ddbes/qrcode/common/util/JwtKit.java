package com.ddbes.qrcode.common.util;

import com.ddbes.qrcode.common.model.S;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by daitian on 2018/5/31.
 */
@Component
public class JwtKit {
    private final Logger log = LoggerFactory.getLogger(JwtKit.class);

    @Value("${conf.jwt.secret}")
    private String JWT_SECRET;
    @Value("${conf.jwt.expire}")
    private Integer JWT_EXP;

    @Autowired
    RedisKit redisKit;

    /**
     * 加密
     * @param claims
     * @return
     */
    private String createJWT(Map<String, Object> claims,Date issuedAt) {

        if (claims == null || claims.size() == 0) {
            return null;
        }
        log.info("JWT_SECRET:" + JWT_SECRET);

        JwtBuilder jwtBuilder = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .setIssuedAt(issuedAt)
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET);

        if (JWT_EXP != null && JWT_EXP > 0) {
            jwtBuilder.setExpiration(DateKit.dayAfter(JWT_EXP));
        }
        return jwtBuilder.compact();
    }

    public String createSimpleJWT(Map<String, Object> claims) {
        Date date =DateKit.now();
        String redisTokenKey = StrKit.append(S.USER_TOKEN.getValue(),claims.get("clientType"),":",claims.get("userId"));
        redisKit.set(redisTokenKey,date,S.USER_TOKEN.getExpire().toString(), TimeUnit.DAYS);
        return createJWT(claims,date);
    }

    /**
     * 解密
     *
     * @param token
     * @return
     */
    public Claims parseJWT(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(JWT_SECRET)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
            log.error("解析JWT Token失败，token无效,Msg:" + e.getMessage());
        }
        return claims;
    }
}
