package com.devdotdone.ddd.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.devdotdone.ddd.service.JwtService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {
  @Autowired
  private JwtService jwtService;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
      return true;
    }

    HandlerMethod handlerMethod = (HandlerMethod) handler;
    Login login = handlerMethod.getMethodAnnotation(Login.class);

    if (login == null) {
      return true;
    } else {
      String jwt = null;

      String authorization = request.getHeader("Authorization");
      if (authorization != null) {
        // Authorization: Bearer xxxxxx
        if (!authorization.substring(7).equals("")) {
          jwt = authorization.substring(7);
        }
      }

      if (jwt == null) {
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "JWT가 없습니다.");
        return false;
      } else {
        if (jwtService.validateJwt(jwt)) {
          return true;
        } else {
          response.sendError(HttpServletResponse.SC_FORBIDDEN, "JWT가 유효하지 않습니다.");
          return false;
        }
      }
    }

  }

  

}
