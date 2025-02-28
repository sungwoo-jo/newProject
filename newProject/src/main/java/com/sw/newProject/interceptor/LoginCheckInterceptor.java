package com.sw.newProject.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        log.info("[{}][requestURI] : {}", this.getClass().getName(), requestURI);
        HttpSession session = request.getSession();
        Object obj = session.getAttribute("member");

        if(obj == null) {
            // 로그인 되지 않음
            log.info("[미인증 사용자 요청]");

            //로그인으로 redirect
            response.sendRedirect("/member/login");
            return false;
        } else {
            // 로그인 되어있을 떄
            log.info("[{}][memberSession] : {}", this.getClass().getName(), obj);
            return true;
        }
    }
}
