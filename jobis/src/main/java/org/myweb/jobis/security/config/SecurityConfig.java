package org.myweb.jobis.security.config;

import jakarta.servlet.http.HttpServletResponse;
import org.myweb.jobis.security.filter.JWTFilter;
import org.myweb.jobis.security.filter.LoginFilter;
import org.myweb.jobis.security.handler.CustomLogoutHandler;
import org.myweb.jobis.security.jwt.JWTUtil;
import org.myweb.jobis.security.model.service.CustomUserDetailsService;
import org.myweb.jobis.user.jpa.repository.UserRepository;
import org.myweb.jobis.user.model.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig implements WebMvcConfigurer {

    private final JWTUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    private final UserRepository userRepository;

    public SecurityConfig(JWTUtil jwtUtil, CustomUserDetailsService userDetailsService, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
    }

    @Bean
    public CustomLogoutHandler customLogoutHandler(UserService userService) {
        return new CustomLogoutHandler(jwtUtil, userService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
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



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager, CustomLogoutHandler customLogoutHandler) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {}) // CORS 설정 활성화
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/index.html", "/static/**", "/manifest.json", "/favicon.ico", "/**/*.js", "/**/*.css", "/**/*.png", "/**/*.jpg").permitAll()
                        .requestMatchers("/{spring:[a-zA-Z0-9-_]+}").permitAll()
                        .requestMatchers("/**/{spring:[a-zA-Z0-9-_]+}").permitAll()

                        // 첨부파일 경로 인증 제외
                        .requestMatchers("/attachments/**").permitAll()

                        // 정적 리소스 및 인증 제외 경로
                        .requestMatchers("/", "/**", "/favicon.ico", "/manifest.json", "/public/**", "/auth/**", "/css/**", "/js/**").permitAll()
                        // .png 파일 인증 없이 허용
                        .requestMatchers("/*.png").permitAll()
                        // 로그인, 토큰 재발급은 인증 제외
                        .requestMatchers("/login","/signup","/users/**", "/reissue", "/users/**").permitAll()
                        // 로그아웃은 인증된 사용자만 가능
                        .requestMatchers("/logout").authenticated()
                        // kakao
                        .requestMatchers("/kakao/**").permitAll()
                        // google
                        .requestMatchers("/google/**").permitAll()
                        // naver
                        .requestMatchers("/naver/**").permitAll()
                        // /mypage/** 경로는 인증만 필요
                        .requestMatchers("/mypage/**").authenticated()
                        // /admin으로 시작하는 경로는 ROLE_ADMIN 권한 필요
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        // 나머지 모든 요청은 인증 필요

                        // QnA 경로 설정
                        .requestMatchers(HttpMethod.GET, "/qna", "/qna/{no}").hasAnyRole("USER", "ADMIN") // 조회는 USER와 ADMIN 허용
                        .requestMatchers(HttpMethod.POST, "/qna/**").hasAnyRole("USER", "ADMIN") // 등록은 USER와 ADMIN 허용
                        .requestMatchers(HttpMethod.PUT, "/qna/{no}").hasAnyRole("USER", "ADMIN")// 수정은 USER와 ADMIN 허용
                        .requestMatchers(HttpMethod.DELETE, "/qna/{no}").hasAnyRole("USER", "ADMIN") // 삭제는 USER와 ADMIN 허용

                        // notice
                        .requestMatchers(HttpMethod.GET, "/notice", "/notice/detail/{no}").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/notice/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/notice/update/{no}").hasAnyRole("ADMIN")

                        // review
                        .requestMatchers(HttpMethod.GET, "/review", "/review/{no}").hasAnyRole("USER", "ADMIN") // 조회는 USER와 ADMIN 허용
                        .requestMatchers(HttpMethod.POST, "/review/**").hasAnyRole("USER", "ADMIN") // 등록은 USER와 ADMIN 허용
                        .requestMatchers(HttpMethod.PUT, "/review/{no}").hasAnyRole("USER", "ADMIN")// 수정은 USER와 ADMIN 허용
                        .requestMatchers(HttpMethod.DELETE, "/review/{no}").hasAnyRole("USER", "ADMIN") // 삭제는 USER와 ADMIN 허용

                        // 채용공고
                        .requestMatchers(HttpMethod.GET,"/jobPostings/**").hasAnyRole("USER", "ADMIN") // 조회는 USER와 ADMIN 허용

                        .anyRequest().authenticated()

                )
                .addFilterBefore(new JWTFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(new LoginFilter(authenticationManager, jwtUtil, userRepository), UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout
                        .logoutUrl("/logout") // 로그아웃 요청 URL
                        .addLogoutHandler(customLogoutHandler) // CustomLogoutHandler 등록
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_OK);
                            response.getWriter().write("로그아웃 성공");
                        })
                        .invalidateHttpSession(true) // 세션 무효화
                        .clearAuthentication(true) // 인증 정보 제거
                        .deleteCookies("JSESSIONID") // 쿠키 제거
                );

        return http.build();
    }
}
