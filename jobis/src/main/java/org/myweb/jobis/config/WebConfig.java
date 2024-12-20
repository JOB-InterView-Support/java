package org.myweb.jobis.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration // Spring에서 이 클래스를 설정 클래스로 인식하게 함
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false); // 확장자 기반 MIME 타입 비활성화
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/static/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "http://localhost:8080")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Authorization", "RefreshToken") // 헤더 노출 추가
                .allowCredentials(true);
    }


    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/{spring:[a-zA-Z0-9-_]+}").setViewName("forward:/");
        registry.addViewController("/**/{spring:[a-zA-Z0-9-_]+}").setViewName("forward:/");
        registry.addViewController("/{spring:[a-zA-Z0-9-_]+}/**{spring:?!(\\.js|\\.css|\\.png|\\.jpg|\\.jpeg|\\.gif)$}").setViewName("forward:/");
    }







}
