package com.back.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

/**
 * Web MVC 설정
 * 정적 리소스 핸들러 등을 설정
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir}")
    private String uploadDir;

    /**
     * 정적 리소스 핸들러 설정
     * 업로드된 이미지 파일을 URL로 접근할 수 있도록 매핑
     *
     * URL 패턴: /api/products/images/{filename}
     * 실제 경로: {upload-dir}/{filename}
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 절대 경로로 변환
        String absolutePath = Paths.get(uploadDir).toAbsolutePath().toUri().toString();

        registry.addResourceHandler("/api/products/images/**")
                .addResourceLocations(absolutePath)
                .setCachePeriod(3600); // 1시간 캐싱
    }
}

