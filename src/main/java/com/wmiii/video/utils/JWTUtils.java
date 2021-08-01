package com.wmiii.video.utils;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtils {

    private static final String jwtSToken = "123456!@#$$";
    private static final String jwtTToken = "345678#$%^&*";
    private static final HashMap<String, String> identifies = new HashMap();

    public JWTUtils() {
        identifies.put("student", jwtSToken);
        identifies.put("teacher", jwtTToken);
    }

    public static String createToken(Integer userId, String identify){
        Map<String,Object> claims = new HashMap<>();
        claims.put("userId",userId);
        JwtBuilder jwtBuilder;

        jwtBuilder = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, identifies.get(identify)) // 签发算法，秘钥为jwtToken
                .setClaims(claims) // body数据，要唯一，自行设置
                .setIssuedAt(new Date()) // 设置签发时间
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 60 * 1000));// 一天的有效时间

        String token = jwtBuilder.compact();
        return token;
    }

    public static Map<String, Object> checkToken(String token, String identify){
        try {
            Jwt parse = Jwts.parser().setSigningKey(identifies.get(identify)).parse(token);
            return (Map<String, Object>) parse.getBody();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }

}
