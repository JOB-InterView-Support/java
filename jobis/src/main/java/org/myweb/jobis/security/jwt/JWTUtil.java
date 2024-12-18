package org.myweb.jobis.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.user.jpa.entity.UserEntity;
import org.myweb.jobis.user.jpa.repository.UserRepository;
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
        log.info("Generating token with category: {}", category);

        // 사용자 정보 조회
        UserEntity user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("userId: " + userId + " not found."));

        // 사용자 관리자 여부 확인
        String adminYn = user.getAdminYn();

        // JWT 생성
        return Jwts.builder()
                .setSubject(userId) // 사용자 ID 설정
                .claim("category", category) // 카테고리 정보 ("access", "refresh")
                .claim("userId", user.getUserId()) // 사용자 ID 추가
                .claim("userName", user.getUserName()) // 사용자 이름 추가
                .claim("role", adminYn.equals("Y") ? "ADMIN" : "USER") // ROLE 정보 추가
                .setExpiration(new Date(System.currentTimeMillis() + expiredMs)) // 만료 시간 설정
                .signWith(SignatureAlgorithm.HS256, secretKey) // 서명
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
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.warn("Token has expired: {}", e.getMessage());
            return e.getClaims(); // 만료된 토큰의 Claims 반환
        } catch (Exception e) {
            log.error("Error parsing token: {}", e.getMessage());
            return null;
        }
    }



    // 토큰 만료 여부 확인
    public boolean isTokenExpired(String token) {
        log.info("JWTUtil RefreshToken 만료 여부 확인 시작");
        try {
            // JWT 파싱 및 Claims 추출
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // 파싱된 Claims 정보를 로그로 출력
            log.info("JWT 토큰 Claims: {}", claims);

            // 만료 여부 확인
            boolean isExpired = claims.getExpiration().before(new Date());
            log.info("JWT 토큰 만료 여부: {}", isExpired ? "만료됨" : "유효함");

            return isExpired;
        } catch (ExpiredJwtException e) {
            // 만료된 토큰 예외 처리
            log.warn("Token has expired: {}", e.getMessage());
            log.info("만료된 토큰 Claims: {}", e.getClaims()); // 만료된 Claims 정보 로그 출력

            return true; // 만료된 경우 true 반환
        } catch (Exception e) {
            // 기타 예외 처리
            log.error("Error checking token expiration: {}", e.getMessage());
            return true; // 오류 발생 시 만료로 간주
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
