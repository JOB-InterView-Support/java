package com.sevensegment.jobis.security.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.sevensegment.jobis.security.jwt.JWTUtil;
import com.sevensegment.jobis.user.model.service.UserService;
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

    private static final long ACCESS_TOKEN_EXPIRATION = 1800000L; //
    private static final long REFRESH_TOKEN_EXPIRATION = 86400000L; //

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        log.info("ReissueController 실행");

        // 헤더 값 가져오기
        String accessTokenHeader = request.getHeader("Authorization");
        String refreshTokenHeader = request.getHeader("RefreshToken");
        String extendLogin = request.getHeader("ExtendLogin");

        log.info("Received Authorization Header: {}", refreshTokenHeader);
        log.info("Received AccessToken Header: {}", accessTokenHeader);
        log.info("Received ExtendLogin Header: {}", extendLogin);

        try {
            // 헤더에서 Bearer 접두사 제거
            String refreshToken = refreshTokenHeader != null && refreshTokenHeader.startsWith("Bearer ")
                    ? refreshTokenHeader.substring("Bearer ".length()).trim()
                    : null;
            String accessToken = accessTokenHeader != null && accessTokenHeader.startsWith("Bearer ")
                    ? accessTokenHeader.substring("Bearer ".length()).trim()
                    : null;

            if (refreshToken == null || accessToken == null) {
                log.warn("RefreshToken 또는 AccessToken이 제공되지 않았습니다.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Tokens");
            }

            log.info("Checking AccessToken expiration...");
            boolean isAccessTokenExpired = jwtUtil.isTokenExpired(accessToken);
            log.info("AccessToken 만료 여부: {}", isAccessTokenExpired ? "만료됨" : "유효함");

            log.info("Checking RefreshToken expiration...");
            boolean isRefreshTokenExpired = jwtUtil.isTokenExpired(refreshToken);
            log.info("RefreshToken 만료 여부: {}", isRefreshTokenExpired ? "만료됨" : "유효함");

            // AccessToken이 유효하고 RefreshToken이 만료된 경우
            if (!isAccessTokenExpired && isRefreshTokenExpired) {
                if ("true".equalsIgnoreCase(extendLogin)) {
                    log.info("로그인 연장 요청 처리 중...");
                    String userId = jwtUtil.getUserIdFromToken(accessToken);

                    String newRefreshToken = jwtUtil.generateToken(userId, "refresh", REFRESH_TOKEN_EXPIRATION);
                    response.setHeader("Refresh-Token", "Bearer " + newRefreshToken);
                    response.setHeader("Access-Control-Expose-Headers", "Refresh-Token");
                    return ResponseEntity.ok("RefreshToken Reissued");
                } else {
                    log.warn("사용자가 로그인 연장을 요청하지 않았습니다.");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session Expired");
                }
            }

            // 둘 다 만료된 경우
            if (jwtUtil.isTokenExpired(accessToken) && jwtUtil.isTokenExpired(refreshToken)) {
                log.warn("둘 다 만료됨. 세션 종료.");
                String userId = jwtUtil.getUserIdFromToken(accessToken);
                userService.clearRefreshToken(userId);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session Expired");
            }


            // AccessToken 만료, RefreshToken 유효
            if (isAccessTokenExpired && !isRefreshTokenExpired) {
                log.info("AccessToken 만료, RefreshToken 유효. AccessToken 재발급 중...");
                String userId = jwtUtil.getUserIdFromToken(refreshToken);

                String newAccessToken = jwtUtil.generateToken(userId, "access", ACCESS_TOKEN_EXPIRATION);
                response.setHeader("Authorization", "Bearer " + newAccessToken);
                response.setHeader("Access-Control-Expose-Headers", "Authorization");
                return ResponseEntity.ok("AccessToken Reissued");
            }

            return ResponseEntity.badRequest().body("Invalid Token State");

        } catch (Exception e) {
            log.error("Reissue 처리 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to Reissue Tokens");
        }
    }


}