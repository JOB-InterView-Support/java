package org.myweb.jobis.security.controller;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.security.jwt.JWTUtil;
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

    private static final long ACCESS_TOKEN_EXPIRATION = 20000L; // 30초
    private static final long REFRESH_TOKEN_EXPIRATION = 40000L; // 2분

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        log.info("ReissueController 실행");

        String refreshToken = request.getHeader("Authorization");
        if (refreshToken == null || !refreshToken.startsWith("Bearer ")) {
            log.warn("Invalid Refresh Token");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Refresh Token");
        }

        String token = refreshToken.substring("Bearer ".length());

        try {
            log.info("ReissueController RefreshToken 만료 여부 확인 시작"); // RefreshToken 유효성 확인 절차 시작

            // RefreshToken의 만료 여부를 확인하는 메서드 호출
            if (jwtUtil.isTokenExpired(token)) {
                log.warn("Refresh Token Expired: {}", token); // RefreshToken이 만료되었음을 경고

                // 만료된 RefreshToken에서도 사용자 ID를 추출
                log.info("만료된 RefreshToken에서도 사용자 ID를 추출");
                String userId = jwtUtil.getUserIdFromToken(token);
                log.info("Extracted userId from expired RefreshToken: {}", userId); // 만료된 토큰에서 추출한 userId 로그

                // DB에서 해당 사용자(userId)의 RefreshToken을 삭제 처리
                log.info("clearRefreshToken 호출 시작... (userId: {})", userId); // RefreshToken 삭제 프로세스 시작
                userService.clearRefreshToken(userId); // RefreshToken 삭제 메서드 호출
                log.info("clearRefreshToken 호출 완료! (userId: {})", userId); // RefreshToken 삭제 완료 로그

                // RefreshToken 만료로 인해 UNAUTHORIZED 상태를 클라이언트에 응답
                log.info("Refresh Token 만료로 401 Unauthorized 응답 반환 (userId: {})", userId);
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

            String newAccessToken = jwtUtil.generateToken(userId, "access", ACCESS_TOKEN_EXPIRATION);
            String newRefreshToken = jwtUtil.generateToken(userId, "refresh", REFRESH_TOKEN_EXPIRATION);

            userService.saveRefreshToken(userId, newRefreshToken); // 새로운 RefreshToken 저장

            response.setHeader("Authorization", "Bearer " + newAccessToken);
            response.setHeader("Refresh-Token", "Bearer " + newRefreshToken);
            response.setHeader("Access-Control-Expose-Headers", "Authorization, Refresh-Token");

            return ResponseEntity.ok("Tokens Reissued");

        } catch (ExpiredJwtException e) {
            log.error("Expired Token Exception: ", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token Expired");
        } catch (Exception e) {
            log.error("Error Reissuing Tokens: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to Reissue Token");
        }
    }
}