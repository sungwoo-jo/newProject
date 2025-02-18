package com.sw.newProject.config;

import com.sw.newProject.interceptor.LoginCheckInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor()) // LoginCheckInterceptor 등록
                .order(1)
                .addPathPatterns("/**") // 인터셉터 적용 페이지(전부)
                .excludePathPatterns("/", "/member/login", "/member/doLogin", "/member/join", "/member/findId", "/member/findPw", "/member/doFindId", "/member/doFindPw", "/member/logout", "/css/**", "/script/**"); // 인터셉터 적용 예외 페이지
    }
}
