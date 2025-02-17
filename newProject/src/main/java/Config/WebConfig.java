package Config;

import com.sw.newProject.interceptor.LogInterceptor;
import com.sw.newProject.interceptor.LoginCheckInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor()) // LogInterceptor 등록
                .order(1)   // 적용할 필터 순서 설정
                .addPathPatterns("/**") // 인터셉터를 적용할 url 패턴 지정
                .excludePathPatterns("/error"); // 인터셉터에서 제외할 패턴 지정
        log.info("addInterceptor 실행");
        registry.addInterceptor(new LoginCheckInterceptor()) //LoginCheckInterceptor 등록

//                .order(2)
                .addPathPatterns("/member/login");
//                .excludePathPatterns("/", "/member/join", "/member/login", "/member/logout", "/static/css/**", "/static/js/**");
    }
}
