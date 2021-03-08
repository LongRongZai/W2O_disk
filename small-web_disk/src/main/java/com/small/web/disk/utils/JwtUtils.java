package com.small.web.disk.utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.small.web.disk.bean.UserBean;
import com.small.web.disk.exceptions.TokenUnavailableException;
import com.small.web.disk.model.LoginModel;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Lehr
 * @create: 2020-02-04
 */
public class JwtUtils {

    /**
     * 签发对象：这个用户的id
     * 签发时间：现在
     * 有效时间：30分钟
     * 载荷内容：暂时设计为：这个人的名字，这个人的昵称
     * 加密密钥：这个人的id加上一串字符串
     */
    public static String createToken(LoginModel loginModel) {

        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.HOUR, 24);
        Date expiresDate = nowTime.getTime();

        return JWT.create().withAudience(loginModel.getNo())   //签发对象
                .withIssuedAt(new Date())    //发行时间
                .withExpiresAt(expiresDate)  //有效时间
                .withClaim("Name", loginModel.getName())    //载荷，随便写几个都可以
                .withClaim("No", loginModel.getNo())
                .withClaim("Account", loginModel.getAccount())
                .sign(Algorithm.HMAC256(loginModel.getNo() + ""));   //加密
    }

    /**
     * 检验合法性，其中secret参数就应该传入的是用户的id
     */
    public static void verifyToken(String token, String secret) throws TokenUnavailableException {
        DecodedJWT jwt = null;
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret + "")).build();
            jwt = verifier.verify(token);
        } catch (Exception e) {
            //效验失败
            throw new TokenUnavailableException("令牌校验失败");
        }
    }

    /**
     * 获取签发对象
     */
    public static String getAudience(String token) throws TokenUnavailableException {
        String audience = null;
        try {
            audience = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException j) {
            //这里是token解析失败
            throw new TokenUnavailableException("令牌解析失败");
        }
        return audience;
    }


    /**
     * 通过载荷名字获取载荷的值
     */
    public static Claim getClaimByName(String token, String name) {
        return JWT.decode(token).getClaim(name);
    }
}

