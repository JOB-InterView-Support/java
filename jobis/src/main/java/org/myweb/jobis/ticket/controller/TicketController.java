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

import java.util.List;
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
    public ResponseEntity<Map<String, String>> startMockInterview(
            @RequestBody Map<String, String> requestBody,
            HttpServletRequest request) {
        try {
            String selectedDate = requestBody.get("date");
            if (selectedDate == null || selectedDate.isEmpty()) {
                log.error("date 파라미터가 비어있습니다.");
                return ResponseEntity.badRequest().body(Map.of("status", "FAIL", "message", "date 파라미터가 필요합니다."));
            }

            String token = request.getHeader("Authorization");
            if (token == null || !token.startsWith("Bearer ")) {
                log.error("Authorization 헤더가 없거나 잘못된 형식입니다.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "FAIL", "message", "토큰이 유효하지 않습니다."));
            }

            token = token.substring(7);
            Claims claims = jwtUtil.getClaimsFromToken(token);
            String userId = claims.getSubject();
            log.info("토큰에서 추출한 userId: {}", userId);

            UserEntity userEntity = userRepository.findByUserId(userId)
                    .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 userId: " + userId));
            log.info("UserEntity 조회 성공: {}", userEntity);

            boolean isStarted = ticketService.startMockInterview(selectedDate, userEntity.getUuid());
            if (isStarted) {
                return ResponseEntity.ok(Map.of("status", "SUCCESS", "message", "모의면접 시작 준비 완료"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("status", "FAIL", "message", "조건에 맞는 이용권이 없습니다."));
            }
        } catch (Exception e) {
            log.error("Error processing /start request: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", "FAIL", "message", "서버 오류가 발생했습니다."));
        }
    }

    @GetMapping("/check")
    public ResponseEntity<Map<String, Object>> checkAvailableTickets(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token == null || !token.startsWith("Bearer ")) {
                log.error("Authorization 헤더가 없거나 잘못된 형식입니다.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "FAIL", "message", "토큰이 유효하지 않습니다."));
            }

            token = token.substring(7);
            Claims claims = jwtUtil.getClaimsFromToken(token);
            String userId = claims.getSubject();
            log.info("토큰에서 추출한 userId: {}", userId);

            UserEntity userEntity = userRepository.findByUserId(userId)
                    .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 userId: " + userId));
            log.info("UserEntity 조회 성공: {}", userEntity);

            // getTicketCountsByUuid 메서드 사용
            List<Integer> ticketCounts = ticketService.getTicketCountsByUuid(userEntity.getUuid());
            log.info("사용 가능한 티켓 카운트 목록: {}", ticketCounts);

            return ResponseEntity.ok(Map.of(
                    "status", "SUCCESS",
                    "message", "사용 가능한 이용권 확인 완료",
                    "ticketCounts", ticketCounts
            ));
        } catch (Exception e) {
            log.error("Error processing /check request: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", "FAIL", "message", "서버 오류가 발생했습니다."));
        }
    }


} // 25.01.07 최종 수정