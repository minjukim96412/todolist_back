package com.todolist.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // /api/** 경로에 대해 CORS 허용
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3000") // React 앱이 실행 중인 주소
                .allowedMethods("GET", "POST", "PUT", "DELETE" , "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600); // 캐시 시간
    }
}
