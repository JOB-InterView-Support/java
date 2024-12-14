package org.myweb.jobis.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {
                }) // cors 활성화
                .formLogin(form -> form.disable()) // 시큐리티가 제공하는 로그인 폼 사용 못하게 함
                .httpBasic(basic -> basic.disable()) // form 태그로 submit 해서 오는 요청은 사용 못하게 함
                // 인증과 인가를 설정하는 부분
                .authorizeHttpRequests(auth -> auth
                                // 현재 프로젝트 안에 뷰페이지를 작업할 때
                                .requestMatchers("/public/**", "/auth/**", "/css/**", "/js/**").permitAll() // 공개 경로
                                .requestMatchers("/user/**").permitAll()
                                // jwt 사용시 추가되는 설정임 -------------------------
                                // 공지사항 관리자용 서비스 요청 설정
                                .requestMatchers("/**", "/login", "/reissue").permitAll()
                                .requestMatchers("logout").authenticated() // 로그아웃 요청은 로그인한 사용자만 가능
                        // 위의 인가 설정을 제외한 나머지 요청들은 인증을 거치도록 설정함
//                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
