package com.sw.newProject.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("LoginCheckInterceptor - preHandle 호출됨"); // 추가된 로그
        String requestURI = request.getRequestURI();
        log.info("[interceptor] : " + requestURI);
        HttpSession session = request.getSession();

        Object obj = session.getAttribute("member");
        System.out.println(obj);

        if(obj == null) {
            // 로그인 되지 않음
            log.info("[미인증 사용자 요청]");
            System.out.println("[미인증 사용자 요청]");

            //로그인으로 redirect
            response.sendRedirect("/member/login?redirectURL=" + requestURI);
            return false;
        }
        // 로그인 되어있을 떄
        return true;
    }
}
