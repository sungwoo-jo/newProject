package com.sw.newProject.config;

import com.sw.newProject.interceptor.LoginCheckInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor()) // LoginCheckInterceptor 등록
                .order(1)
                .addPathPatterns("/**") // 인터셉터 적용 페이지(전부)
                .excludePathPatterns("/", "/member/login", "/member/doLogin", "/member/join", "/member/doJoin", "/member/findId", "/member/findPw", "/member/doFindId", "/member/doFindPw", "/member/logout", "/css/**", "/script/**"); // 인터셉터 적용 예외 페이지
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/upload/**")
                .addResourceLocations("file:C:/Users/sungw/OneDrive/바탕 화면/new 프로젝트/newProject/newProject/src/main/resources/static/upload");
    }
}
