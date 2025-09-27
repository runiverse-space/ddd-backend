package com.devdotdone.ddd.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JwtService {
  private String strKey = "com.devdotdone.dddbackend.secret.key";
  private SecretKey secretKey;
  private long jwtDuration = 90 * 24 * 60 * 60 * 1000;

  public JwtService() throws Exception {
    byte[] bytes = strKey.getBytes();
    secretKey = Keys.hmacShaKeyFor(bytes);
  }

  public String createJwt(int userId, String userLoginId, String userEmail) {
    JwtBuilder jwtBuilder = Jwts.builder();
    
    jwtBuilder.subject(String.valueOf(userId));
    jwtBuilder.claim("userLoginId", userLoginId);
    jwtBuilder.claim("userEmail", userEmail);

    jwtBuilder.expiration(new Date(new Date().getTime() + jwtDuration));

    jwtBuilder.signWith(secretKey);

    String jwt = jwtBuilder.compact();

    return jwt;
  }

  public boolean validateJwt(String jwt) {
    boolean result = false;
    try {
      // JWT를 해석하는 JwtParser 얻기
      JwtParserBuilder jwtParserBuilder = Jwts.parser();
      jwtParserBuilder.verifyWith(secretKey);
      JwtParser jwtParser = jwtParserBuilder.build();

      // JWT 해석
      Jws<Claims> jws = jwtParser.parseSignedClaims(jwt);
      result = true;
    } catch (ExpiredJwtException e) {
      log.info("기간이 만료된 토큰입니다.");
    } catch (io.jsonwebtoken.security.SecurityException e) {
      log.info("잘못 서명된 토큰입니다.");
    } catch (Exception e) {
      log.info("토큰을 해석할 수 없습니다.");
    }
    return result;
  }

  public Map<String, String> getClaims(String jwt) {
    // JWT를 해석하는 JwtParser 얻기
    JwtParserBuilder jwtParserBuilder = Jwts.parser();
    jwtParserBuilder.verifyWith(secretKey);
    JwtParser jwtParser = jwtParserBuilder.build();

    // JWT 해석    
    Jws<Claims> jws = jwtParser.parseSignedClaims(jwt);
    Claims claims = jws.getPayload();

    Map<String, String> map = new HashMap<>();
    map.put("userId", claims.getSubject());
    map.put("userLoginId", claims.get("userLoginId").toString());
    map.put("userEmail", claims.get("userEmail").toString());
    
    return map;
  }
}
