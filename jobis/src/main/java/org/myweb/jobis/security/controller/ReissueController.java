package org.myweb.jobis.security.controller;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.security.jwt.JWTUtil;
import org.myweb.jobis.user.jpa.entity.UserEntity;
import org.myweb.jobis.user.model.dto.User;
import org.myweb.jobis.user.model.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReissueController {

    private final JWTUtil jwtUtil; // JWT 유틸리티 클래스 (토큰 생성 및 검증)
    private final UserService userService; // 사용자 서비스 클래스

    // AccessToken과 RefreshToken의 만료 시간 설정 (밀리초 단위)
    private static final long ACCESS_TOKEN_EXPIRATION = 30000L; // AccessToken 30초
    private static final long REFRESH_TOKEN_EXPIRATION = 120000L; // RefreshToken 2분

    /**
     * RefreshToken을 이용해 새로운 AccessToken과 RefreshToken을 재발급하는 메서드
     */
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        log.info("ReissueController 실행");
        // 요청 헤더에서 Authorization 값을 가져와서 RefreshToken 추출
        String refreshToken = request.getHeader("Authorization");

        // RefreshToken이 존재하지 않거나 올바른 형식이 아닌 경우
        if (refreshToken == null || !refreshToken.startsWith("Bearer ")) {
            log.warn("Invalid Refresh Token");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Refresh Token");
        }

        // "Bearer " 부분을 제외하고 순수 토큰만 추출
        String token = refreshToken.substring("Bearer ".length());

        try {
            // RefreshToken의 만료 여부 확인
            if (jwtUtil.isTokenExpired(token)) {
                log.info("RefreshToken의 만료 여부 확인");
                log.warn("Refresh Token Expired: {}", token);

                // 만료된 RefreshToken 삭제
                clearRefreshToken(token);

                // 프론트에 명확한 메시지 반환
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Refresh Token Expired");
            }



            // RefreshToken에서 사용자 ID 추출
            String userId = jwtUtil.getUserIdFromToken(token);

            // 사용자 정보를 데이터베이스에서 조회
            User user = userService.selectMember(userId);
            if (user == null) { // 사용자 정보가 없을 경우
                log.warn("User Not Found for ID: {}", userId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found");
            }

            // 데이터베이스에 저장된 RefreshToken과 요청으로 받은 RefreshToken 비교
            if (!token.equals(user.getUserRefreshToken())) {
                log.info("데이터베이스에 저장된 RefreshToken과 요청으로 받은 RefreshToken 비교");
                log.warn("Invalid Refresh Token for User ID: {}", userId);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Refresh Token");
            }
            log.info("새로운 AccessToken 및 RefreshToken 생성");
            // 새로운 AccessToken 및 RefreshToken 생성
            String newAccessToken = jwtUtil.generateToken(userId, "access", ACCESS_TOKEN_EXPIRATION);
            String newRefreshToken = jwtUtil.generateToken(userId, "refresh", REFRESH_TOKEN_EXPIRATION);

            log.info("새로운 RefreshToken을 데이터베이스에 저장 (기존 RefreshToken 갱신)");
            // 새로운 RefreshToken을 데이터베이스에 저장 (기존 RefreshToken 갱신)
            userService.saveRefreshToken(userId, newRefreshToken);

            log.info("새로 발급된 AccessToken과 RefreshToken을 응답 헤더에 추가");
            // 새로 발급된 AccessToken과 RefreshToken을 응답 헤더에 추가
            response.setHeader("Authorization", "Bearer " + newAccessToken);
            response.setHeader("Refresh-Token", "Bearer " + newRefreshToken);
            response.setHeader("Access-Control-Expose-Headers", "Authorization, Refresh-Token");

            log.info("New Tokens Issued for User ID: {}", userId);
            return ResponseEntity.ok("Tokens Reissued"); // 성공 응답 반환
        } catch (Exception e) {
            log.error("Error Reissuing Tokens: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to Reissue Token");
        }
    }

    /**
     * RefreshToken이 만료되었을 때 해당 사용자의 RefreshToken을 삭제하는 메서드
     */
    private void clearRefreshToken(String token) {
        try {
            // RefreshToken에서 사용자 ID 추출
            String userId = jwtUtil.getUserIdFromToken(token);

            // 데이터베이스에서 해당 사용자의 RefreshToken을 삭제
            userService.clearRefreshToken(userId);

            log.info("Refresh Token Cleared for User ID: {}", userId);
        } catch (Exception e) {
            log.error("Failed to Clear Refresh Token: ", e);
        }
    }
}
