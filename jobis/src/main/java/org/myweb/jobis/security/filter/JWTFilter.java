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
                requestURI.equals("/users/checkPhoneNumber") ||
                requestURI.equals("/users/sendVerificationEmail") ||
                requestURI.equals("/users/verifyCode") ||
                requestURI.equals("/reissue"); // 추가
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        log.info("JWTFilter 실행 중: {}", requestURI);

        if (isExcludedUrl(requestURI)) {
            log.info("URL 제외 처리: {}", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String authorizationHeader = request.getHeader("Authorization");

            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                log.warn("Authorization 헤더가 비어있거나 잘못된 형식입니다: {}", authorizationHeader);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Missing or invalid Authorization header");
                return;
            }

            String[] parts = authorizationHeader.split(" ");
            if (parts.length != 2 || parts[1].isEmpty()) {
                log.warn("Authorization 토큰 형식 오류: {}", authorizationHeader);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid Authorization token format");
                return;
            }

            String token = parts[1].trim();
            log.info("JWT 토큰 파싱 시작: {}", token);

            // /reissue 요청은 만료된 토큰도 허용
            if (jwtUtil.isTokenExpired(token) && !requestURI.equals("/reissue")) {
                log.warn("JWTFilter 토큰이 만료되었습니다.");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Access token expired");
                return;
            }

            String userId = jwtUtil.getUserIdFromToken(token);
            String role = jwtUtil.getRoleFromToken(token);

            if (userId == null || role == null) {
                log.warn("JWT에서 userId 또는 role이 비어있습니다. userId: {}, role: {}", userId, role);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid token claims");
                return;
            }

            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userId, null, Collections.singletonList(authority)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            // /reissue 요청의 경우 만료된 토큰도 허용
            if (requestURI.equals("/reissue")) {
                log.warn("만료된 JWT 사용 시도: {}", e.getMessage());
                filterChain.doFilter(request, response);
            } else {
                log.warn("만료된 JWT 사용 시도: {}", e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("JWT expired");
            }
        } catch (IllegalArgumentException e) {
            log.error("잘못된 JWT 요청: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid JWT");
        } catch (Exception e) {
            log.error("JWT 처리 중 예상치 못한 오류: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error processing JWT");
        }
    }
}
