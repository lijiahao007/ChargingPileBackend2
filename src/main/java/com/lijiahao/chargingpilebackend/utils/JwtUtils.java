package com.lijiahao.chargingpilebackend.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.lijiahao.chargingpilebackend.controller.exception.TokenUnavailableException;

import java.util.Calendar;
import java.util.Date;

public class JwtUtils {
    public static String SECRET = "lijiahao";

    /**
     * 签发对象：这个用户的id
     * 签发时间：现在
     * 有效时间：30分钟
     * 载荷内容：暂时设计为：这个人的名字，这个人的昵称
     * 加密密钥：这个人的id加上一串字符串
     */
    public static String createToken(String userId) {

        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.HOUR, 24);
        Date expiresDate = nowTime.getTime();

        return JWT.create().withAudience(userId)   //签发对象
                .withIssuedAt(new Date())    //发行时间
                .withExpiresAt(expiresDate)  //有效时间
                .sign(Algorithm.HMAC256(userId + SECRET));   //指定密钥进行加密
    }

    /**
     * 检验合法性，secret是加密的密钥，这里就硬编码成  userID + SECRET
     *
     * @param token
     * @throws TokenUnavailableException
     */
    public static void verifyToken(String token, String userId) throws TokenUnavailableException {
        DecodedJWT jwt = null;
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(userId + SECRET)).build();
            // 验证token
            jwt = verifier.verify(token);
        } catch (Exception e) {
            //效验失败
            //这里抛出的异常是我自定义的一个异常，你也可以写成别的
            throw new TokenUnavailableException();
        }
    }

    /**
     * 获取签发对象
     */
    public static String getUserID(String token) throws TokenUnavailableException {
        String userId= null;
        try {
            userId =JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException j) {
            //这里是token解析失败
            j.printStackTrace();
            throw new TokenUnavailableException();
        }
        return userId;
    }


    /**
     * 通过载荷名字获取载荷的值
     */
    public static Claim getClaimByName(String token, String name) {
        return JWT.decode(token).getClaim(name);
    }

}
