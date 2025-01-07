package org.myweb.jobis.ticket.controller;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.security.jwt.JWTUtil;
import org.myweb.jobis.ticket.model.service.TicketService;
import org.myweb.jobis.user.jpa.entity.UserEntity;
import org.myweb.jobis.user.jpa.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/ticket")
public class TicketController {

    private final TicketService ticketService;
    private final JWTUtil jwtUtil; // JWTUtil 주입
    private final UserRepository userRepository; // UserRepository 주입


    @PostMapping("/start")
    public ResponseEntity<String> startMockInterview(
            @RequestBody Map<String, String> requestBody,
            HttpServletRequest request) {
        try {
            // JSON에서 date 값 추출
            String selectedDate = requestBody.get("date");
            if (selectedDate == null || selectedDate.isEmpty()) {
                log.error("date 파라미터가 비어있습니다.");
                return ResponseEntity.badRequest().body("date 파라미터가 필요합니다.");
            }

            // 헤더에서 Authorization 토큰 추출
            String token = request.getHeader("Authorization");
            if (token == null || !token.startsWith("Bearer ")) {
                log.error("Authorization 헤더가 없거나 잘못된 형식입니다.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 유효하지 않습니다.");
            }

            // "Bearer " 접두사 제거
            token = token.substring(7);

            // JWTUtil을 사용하여 토큰에서 userId 추출
            Claims claims = jwtUtil.getClaimsFromToken(token);
            String userId = claims.getSubject(); // userId는 subject로 저장됨
            log.info("토큰에서 추출한 userId: {}", userId);

            // UserRepository에서 userId를 사용해 UserEntity 조회
            UserEntity userEntity = userRepository.findByUserId(userId)
                    .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 userId: " + userId));
            log.info("UserEntity 조회 성공: {}", userEntity);

            // TicketService 호출 (UUID 전달)
            boolean isStarted = ticketService.startMockInterview(selectedDate, userEntity.getUuid());
            if (isStarted) {
                log.info("Ai 모의면접 시작 페이지로 넘어감");
                return ResponseEntity.ok("모의면접 시작 준비 완료");
            } else {
                return ResponseEntity.badRequest().body("조건에 맞는 이용권이 없습니다.");
            }
        } catch (Exception e) {
            log.error("Error processing /start request: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }
    }
}