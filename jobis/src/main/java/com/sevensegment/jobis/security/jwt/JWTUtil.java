package com.sevensegment.jobis.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import com.sevensegment.jobis.user.jpa.entity.UserEntity;
import com.sevensegment.jobis.user.jpa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class JWTUtil {

    private final UserRepository userRepository;


    @Value("${jwt.secret}") // JWT Secret Key
    private String secretKey;

    @Value("${jwt.access_expiration}") // Access Token 만료 시간
    private Long accessExpiration;

    @Value("${jwt.refresh_expiration}") // Refresh Token 만료 시간
    private Long refreshExpiration;

    public JWTUtil(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    // 토큰 생성 메서드 (공통)
    public String generateToken(String userId, String category, Long expiredMs) {
        if (userId == null || userId.trim().isEmpty()) {
            log.error("토큰 생성 실패: userId가 유효하지 않습니다.");
            throw new IllegalArgumentException("유효하지 않은 userId입니다.");
        }

        if (expiredMs == null || expiredMs <= 0) {
            log.error("토큰 생성 실패: 만료 시간이 유효하지 않습니다.");
            throw new IllegalArgumentException("유효하지 않은 만료 시간입니다.");
        }

        log.info("토큰 생성 시작 - userId: {}, category: {}, 만료 시간: {}", userId, category, expiredMs);

        UserEntity user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("userId: " + userId + " not found."));

        return Jwts.builder()
                .setSubject(userId)
                .claim("category", category)
                .claim("userId", user.getUserId())
                .claim("userName", user.getUserName())
                .claim("role", user.getAdminYn().equals("Y") ? "ADMIN" : "USER")
                .setExpiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }


    // Access Token 생성 메서드
    public String generateAccessToken(String userId) {
        return generateToken(userId, "access", accessExpiration);
    }

    // Refresh Token 생성 메서드
    public String generateRefreshToken(String userId) {
        return generateToken(userId, "refresh", refreshExpiration);
    }

    // 토큰에서 클레임 추출
    public Claims getClaimsFromToken(String token) {
        log.info("JWTUtil - Claims 추출 시작: {}", token);

        if (token == null || token.trim().isEmpty()) {
            log.error("토큰이 비어있거나 유효하지 않습니다.");
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token.trim())
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.warn("만료된 토큰에서 Claims 추출: {}", e.getClaims());
            return e.getClaims(); // 만료된 Claims 반환
        } catch (Exception e) {
            log.error("JWT Claims 추출 중 오류: {}", e.getMessage());
            throw e; // 명확한 예외를 던져 호출자가 처리하도록 위임
        }
    }




    // 토큰 만료 여부 확인
    public boolean isTokenExpired(String token) {
        log.info("JWTUtil - 토큰 만료 여부 확인 시작: {}", token);

        if (token == null || token.trim().isEmpty()) {
            log.error("토큰이 비어있거나 유효하지 않습니다.");
            return true; // 만료된 것으로 간주
        }

        try {
            // JWT 파싱 및 Claims 추출
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token.trim())
                    .getBody();

            log.info("JWT 토큰 Claims: {}", claims);

            // 만료 여부 확인
            boolean isExpired = claims.getExpiration().before(new Date());
            log.info("JWT 토큰 만료 여부: {}", isExpired ? "만료됨" : "유효함");
            return isExpired;
        } catch (ExpiredJwtException e) {
            log.warn("토큰이 만료되었습니다: {}", e.getMessage());
            log.info("만료된 토큰 Claims: {}", e.getClaims()); // 만료된 Claims 정보 로그 출력
            return true; // 만료된 경우 true 반환
        } catch (IllegalArgumentException e) {
            log.error("JWT 파싱 중 오류: 토큰 형식이 잘못되었습니다. {}", e.getMessage());
            return true; // 오류 발생 시 만료로 간주
        } catch (Exception e) {
            log.error("JWT 파싱 중 예상치 못한 오류: {}", e.getMessage());
            return true; // 기타 오류도 만료로 간주
        }
    }




    // 토큰에서 사용자 ID 추출
    public String getUserIdFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject(); // userId를 저장한 위치
        } catch (ExpiredJwtException e) {
            log.warn("Token expired, extracting userId: {}", e.getClaims().getSubject());
            return e.getClaims().getSubject(); // 만료된 토큰에서도 userId 추출
        } catch (Exception e) {
            log.error("Error extracting userId from token: {}", e.getMessage());
            return null;
        }
    }





    // 토큰에서 역할(Role) 정보 추출
    public String getRoleFromToken(String token) {
        return getClaimsFromToken(token).get("role", String.class);
    }

    // 역할(Role)에 따른 권한 생성 (예: ROLE_ADMIN, ROLE_USER)
    public String getAuthoritiesFromRole(String role) {
        return role.equals("ADMIN") ? "ROLE_ADMIN" : "ROLE_USER";
    }

    // 토큰에서 카테고리 정보 추출
    public String getCategoryFromToken(String token) {
        return getClaimsFromToken(token).get("category", String.class);
    }

}
