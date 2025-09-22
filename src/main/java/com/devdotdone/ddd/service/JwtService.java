package com.devdotdone.ddd.service;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
  private String strKey = "com.devdotdone.dddbackend.secret.key";
  private SecretKey secretKey;
  private long jwtDuration = 60 * 24 * 60 * 60 * 1000;

  public JwtService() throws Exception {
    byte[] bytes = strKey.getBytes();
    secretKey = Keys.hmacShaKeyFor(bytes);
  }

  // jwt 생성 메서드
  public String createJwt(String userLoginId, String userEmail) {
    JwtBuilder jwtBuilder = Jwts.builder();
    
    // jwt에 포함할 내용 추가
    jwtBuilder.subject(userLoginId);
    jwtBuilder.claim("userEmail", userEmail);

    jwtBuilder.expiration(new Date(new Date().getTime() + jwtDuration));

    jwtBuilder.signWith(secretKey);

    String jwt = jwtBuilder.compact();

    return jwt;
  }
}
