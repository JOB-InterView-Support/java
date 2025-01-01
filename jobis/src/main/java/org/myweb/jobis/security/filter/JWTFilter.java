package org.myweb.jobis.security.filter;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.security.jwt.JWTUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import java.io.IOException;
import java.util.Collections;

@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    private boolean isExcludedUrl(String requestURI) {
        return requestURI.endsWith(".png") ||
                requestURI.equals("/") ||
                requestURI.equals("/favicon.ico") ||
                requestURI.equals("/manifest.json") ||
                requestURI.startsWith("/login") ||
                requestURI.startsWith("/static") ||
                requestURI.equals("/users/checkuserId") ||
                requestURI.equals("/users/checkEmail") ||
                requestURI.equals("/users/login") ||
                requestURI.equals("/users/signup") ||
                requestURI.equals("/users/snsSignup") ||
                requestURI.equals("/users/checkPhoneNumber") ||
                requestURI.equals("/users/sendVerificationEmail") ||
                requestURI.equals("/users/verifyCode") ||
                requestURI.equals("/reissue") || // 추가
                requestURI.equals("/kakao/apicode") || // 추가
                requestURI.equals("/kakaoLogin") || // 추가
                requestURI.equals("/kakaoLink") || // 추가
                requestURI.equals("/naver/apicode") || // 추가
                requestURI.equals("/naverLogin") || // 추가
                requestURI.equals("/naverLink") || // 추가
                requestURI.equals("/google/apicode") || // 추가
                requestURI.equals("/googleLogin") ||
                requestURI.equals("/googleLink") ||
                requestURI.equals("/faceRegistration") ||
                requestURI.equals("/mypage/faceId/**") ||
                requestURI.equals("/updateUser") ||

                requestURI.equals("/favorites") || // 추가
                requestURI.equals("/jobPosting") || // 추가
                requestURI.equals("/review") || // 추가
                requestURI.equals("/notice")||

                requestURI.equals("/payments/success")||// 12.27 추가 건열
                requestURI.equals("/payments/fail")|| // 12.27 추가 건열
                requestURI.equals("/payments/confirm")|| // 12.30 추가 건열
                requestURI.equals("/paymentSuccess")|| // 12.30 추가 건열

                requestURI.startsWith("/notice/attachments/") || //1.1 추가 인경 notice 첨부파일 경로

                 requestURI.startsWith("/qna/attachments/"); // 첨부 파일 경로 추가

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        log.info("JWTFilter 실행 중: {}", requestURI);

        log.info("모든 요청 헤더 출력");
        // 모든 요청 헤더 출력
//        request.getHeaderNames().asIterator()
//                .forEachRemaining(header -> log.info("요청 헤더: {} = {}", header, request.getHeader(header)));


        // 제외 대상 URL 처리
        if (isExcludedUrl(requestURI)) {
//            log.info("JWTFilter 제외 대상 URL: {}", requestURI);

            filterChain.doFilter(request, response);
            return;
        }

        String accessTokenHeader = request.getHeader("Authorization");
        String refreshTokenHeader = request.getHeader("RefreshToken");

        if (accessTokenHeader == null || accessTokenHeader.isEmpty()) {
            log.warn("Authorization 헤더가 없습니다.");
        }
        if (refreshTokenHeader == null || refreshTokenHeader.isEmpty()) {
            log.warn("RefreshToken 헤더가 없습니다.");
        }

        try {
            if (accessTokenHeader != null && refreshTokenHeader != null) {
                String accessToken = accessTokenHeader.substring("Bearer ".length());
                String refreshToken = refreshTokenHeader.substring("Bearer ".length());

                // RefreshToken 만료, AccessToken 유효
                if (!jwtUtil.isTokenExpired(accessToken) && jwtUtil.isTokenExpired(refreshToken)) {
                    log.warn("RefreshToken 만료, AccessToken 유효.");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setHeader("token-expired", "RefreshToken"); // 명확한 헤더 추가
                    response.getWriter().write("{\"error\": \"RefreshToken expired\"}");
                    return;
                }


                // AccessToken 만료
                if (jwtUtil.isTokenExpired(accessToken)) {
                    log.warn("AccessToken 만료.");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setHeader("token-expired", "AccessToken");
                    response.getWriter().write("{\"error\": \"AccessToken expired\"}");
                    return;
                }
            } else {
                log.warn("AccessToken 또는 RefreshToken이 제공되지 않았습니다.");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\": \"Missing or invalid tokens\"}");
                return;
            }

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            log.error("JWT 처리 중 오류 발생: ", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Internal server error\"}");
        }
    }


}
