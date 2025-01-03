package org.myweb.jobis.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.user.model.dto.User;
import org.myweb.jobis.user.model.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/face")
@RequiredArgsConstructor
@CrossOrigin
public class FaceController {
    private final UserService userService;

    @PostMapping("/receiveUuid")
    public ResponseEntity<Map<String, Object>> receiveUuid(@RequestBody UuidRequest uuidRequest,
                                                           HttpServletRequest request,
                                                           HttpServletResponse response) {
        String uuid = uuidRequest.getUuid();
        log.info("Received UUID: {}", uuid);

        // UUID를 사용하여 사용자 조회
        User user = userService.getUserByUuid(uuid);
        if (user == null) {
            log.warn("No user found with UUID: {}", uuid);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "UUID로 사용자를 찾을 수 없습니다."));
        }

        log.info("User ID: {}, User PW: {}", user.getUserId(), user.getUserPw());

        // 요청 속성에 userId와 userPw 설정
        request.setAttribute("userId", user.getUserId());
        request.setAttribute("userPw", user.getUserPw());

        // LoginFilter로 요청 전달
        try {
            request.getRequestDispatcher("/login").forward(request, response);
        } catch (Exception e) {
            log.error("Error forwarding to LoginFilter: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Login process failed."));
        }

        // LoginFilter에서 응답이 처리되므로 컨트롤러 응답은 null 반환
        return null;
    }




    public static class UuidRequest {
        private String uuid;

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }
    }
}
