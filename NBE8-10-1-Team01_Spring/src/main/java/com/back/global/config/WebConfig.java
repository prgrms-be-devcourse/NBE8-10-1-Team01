package com.back.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

/**
 * Web MVC 설정
 * CORS 설정 및 정적 리소스 핸들러 등 설정
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir}")
    private String uploadDir;

    /**
     * CORS 설정
     * 로컬 프론트엔드에서 들어오는 모든 요청을 허용
     *
     * - 허용 출처: http://localhost:3000 (Next.js 기본 포트)
     * - 허용 메서드: GET, POST, PUT, DELETE, PATCH, OPTIONS
     * - 허용 헤더: 모든 헤더
     * - 인증 정보: 쿠키 등 허용
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")  // /api로 시작하는 모든 경로에 CORS 적용
                .allowedOrigins(
                        "http://localhost:3000",      // Next.js 개발 서버
                        "http://localhost:3001",      // 다른 포트 사용 시 대비
                        "http://127.0.0.1:3000"       // localhost 대신 IP 사용 시
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")  // 모든 HTTP 메서드 허용
                .allowedHeaders("*")            // 모든 헤더 허용
                .allowCredentials(true)         // 쿠키, 인증 정보 포함 허용
                .maxAge(3600);                  // preflight 요청 캐시 시간 (1시간)
    }

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

