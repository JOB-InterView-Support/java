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

    private final JWTUtil jwtUtil;
    private final UserService userService;

    private static final long ACCESS_TOKEN_EXPIRATION = 1800000L; // 30분
    private static final long REFRESH_TOKEN_EXPIRATION = 86400000L; // 24시간

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = request.getHeader("Authorization");

        if (refreshToken == null || !refreshToken.startsWith("Bearer ")) {
            log.warn("Invalid Refresh Token");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Refresh Token");
        }

        String token = refreshToken.substring("Bearer ".length());

        try {
            if (jwtUtil.isTokenExpired(token)) {
                log.warn("Refresh Token Expired: {}", token);
                clearRefreshToken(token);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh Token Expired");
            }

            String userId = jwtUtil.getUserIdFromToken(token);
            User user = userService.selectMember(userId);
            if (user == null) {
                log.warn("User Not Found for ID: {}", userId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found");
            }

            if (!token.equals(user.getUserRefreshToken())) {
                log.warn("Invalid Refresh Token for User ID: {}", userId);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Refresh Token");
            }

            // 새로운 AccessToken 및 RefreshToken 생성
            String newAccessToken = jwtUtil.generateToken(userId, "access", ACCESS_TOKEN_EXPIRATION);
            String newRefreshToken = jwtUtil.generateToken(userId, "refresh", REFRESH_TOKEN_EXPIRATION);

            // 새로운 RefreshToken 저장
            userService.saveRefreshToken(userId, newRefreshToken);

            // 응답 헤더에 추가
            response.setHeader("Authorization", "Bearer " + newAccessToken);
            response.setHeader("Refresh-Token", "Bearer " + newRefreshToken);
            response.setHeader("Access-Control-Expose-Headers", "Authorization, Refresh-Token");

            log.info("New Tokens Issued for User ID: {}", userId);
            return ResponseEntity.ok("Tokens Reissued");
        } catch (Exception e) {
            log.error("Error Reissuing Tokens: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to Reissue Token");
        }
    }


    private void clearRefreshToken(String token) {
        try {
            String userId = jwtUtil.getUserIdFromToken(token);
            userService.clearRefreshToken(userId);
            log.info("Refresh Token Cleared for User ID: {}", userId);
        } catch (Exception e) {
            log.error("Failed to Clear Refresh Token: ", e);
        }
    }
}
