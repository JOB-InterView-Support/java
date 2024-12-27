package org.myweb.jobis.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
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

        // 첨부파일을 정적으로 서빙
        registry.addResourceHandler("/attachments/**")
                .addResourceLocations("file:/C:/upload_files/"); // 파일 경로
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Token-Expired", "Authorization", "RefreshToken")
                .allowCredentials(true)
        ;
    }



    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/{spring:[a-zA-Z0-9-_]+}").setViewName("forward:/");
        registry.addViewController("/**/{spring:[a-zA-Z0-9-_]+}").setViewName("forward:/");
        registry.addViewController("/{spring:[a-zA-Z0-9-_]+}/**{spring:?!(\\.js|\\.css|\\.png|\\.jpg|\\.jpeg|\\.gif)$}").setViewName("forward:/");
    }


}
