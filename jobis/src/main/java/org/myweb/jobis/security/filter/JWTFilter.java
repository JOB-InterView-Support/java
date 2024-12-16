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

import java.io.IOException;
import java.util.Collections;

@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("JWTFilter 실행 중...");

        String requestURI = request.getRequestURI();

        // .png 파일 필터링 제외
        if (requestURI.endsWith(".png") ||
                requestURI.equals("/") ||
                requestURI.equals("/favicon.ico") ||
                requestURI.equals("/manifest.json")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 정적 리소스와 특정 URL 필터링 제외
        if (requestURI.equals("/") ||
                requestURI.startsWith("/login") ||
                requestURI.equals("/favicon.ico") ||
                requestURI.equals("/manifest.json") ||
                requestURI.startsWith("/static")) {
            filterChain.doFilter(request, response); // 필터 통과
            return;
        }

        if (requestURI.equals("/users/checkuserId") ||
                requestURI.equals("/users/checkEmail") ||
                requestURI.equals("/users/login") ||
                requestURI.equals("/users/signup")||
                requestURI.equals("/users/checkPhoneNumber")||
                requestURI.equals("/users/sendVerificationEmail") ||
                requestURI.equals("/users/verifyCode")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String authorizationHeader = request.getHeader("Authorization");

            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                log.warn("Authorization 헤더가 비어있거나 잘못된 형식입니다.");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 상태 반환
                response.getWriter().write("Missing or invalid Authorization header");
                return;
            }

            String token = authorizationHeader.split(" ")[1];

            if (jwtUtil.isTokenExpired(token)) {
                log.warn("토큰이 만료되었습니다.");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 상태 반환
                response.getWriter().write("Access token expired");
                return;
            }

            String userId = jwtUtil.getUserIdFromToken(token);
            String role = jwtUtil.getRoleFromToken(token);

            // 권한 생성 및 SecurityContext에 설정
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userId, null, Collections.singletonList(authority)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            log.error("JWT 처리 중 예외 발생: ", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error processing JWT");
        }
    }




}
