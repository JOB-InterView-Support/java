package com.sevensegment.jobis.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.sevensegment.jobis.security.jwt.JWTUtil;
import com.sevensegment.jobis.user.model.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    private final JWTUtil jwtUtil;
    private final UserService userService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        System.out.println("logout 메서드 호출됨");
        log.info("로그아웃 요청 처리 중...");
        String authorization = request.getHeader("Authorization");

        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring("Bearer ".length());

            try {
                String userId = jwtUtil.getUserIdFromToken(token);

                if (userId != null) {
                    userService.clearRefreshToken(userId);
                    log.info("Refresh Token 제거 완료: User ID = {}", userId);
                }

                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("로그아웃 성공");
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                try {
                    response.getWriter().write("로그아웃 처리 중 오류 발생");
                } catch (Exception writeException) {
                    writeException.printStackTrace();
                }
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            try {
                response.getWriter().write("유효하지 않은 요청");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void handleErrorResponse(HttpServletResponse response, int status, String message) {
        try {
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("message", message);
            responseBody.put("status", "ERROR");

            response.setStatus(status);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            ObjectMapper mapper = new ObjectMapper();
            response.getWriter().write(mapper.writeValueAsString(responseBody));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
