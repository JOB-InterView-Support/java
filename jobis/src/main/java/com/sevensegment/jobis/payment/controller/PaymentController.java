package com.sevensegment.jobis.payment.controller;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import com.sevensegment.jobis.payment.jpa.entity.PaymentEntity;
import com.sevensegment.jobis.payment.jpa.repository.PaymentRepository;
import com.sevensegment.jobis.payment.model.dto.PaymentRequest;
import com.sevensegment.jobis.payment.model.dto.PaymentResponse;
import com.sevensegment.jobis.payment.model.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentRepository paymentRepository;

    private PaymentRequest paymentRequest;

    @PostMapping("/request")
    public ResponseEntity<?> requestPayment(@RequestBody PaymentRequest paymentRequest) {
        try {
            paymentService.processPayment(paymentRequest);
            return ResponseEntity.ok().body("결제 요청 성공");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "message", e.getMessage()
            )); // 409 상태와 메시지 반환
        } catch (Exception e) {
            log.error("결제 요청 처리 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "message", "결제 요청 처리 실패",
                    "error", e.getMessage()
            ));
        }
    }


    private void logRequestHeaders(HttpServletRequest request) {
        request.getHeaderNames().asIterator()
                .forEachRemaining(headerName -> log.info("{}: {}", headerName, request.getHeader(headerName)));
    }

    @PostMapping("/paymentSuccess")
    public ResponseEntity<?> handlePaymentSuccess(@RequestBody Map<String, Object> requestData) {
        String paymentKey = (String) requestData.get("paymentKey");
        String orderId = (String) requestData.get("orderId");

        log.info("결제 성공 처리: paymentKey={}, orderId={}", paymentKey, orderId);
        // 추가 로직 구현
        return ResponseEntity.ok("결제 성공");
    }


    @PostMapping("/confirm")
    public ResponseEntity<?> confirmPayment(@RequestBody Map<String, Object> requestData) {
        log.info("Confirm Payment API called with data: {}", requestData);

        try {
            String paymentKey = (String) requestData.get("paymentKey");
            log.info("Received paymentKey: {}", paymentKey);
            int amount = (int) requestData.get("amount");
            String orderId = (String) requestData.get("orderId");
            log.info("Received orderId: {}", orderId);

            boolean isAlreadyProcessed = paymentRepository.existsByOrderId(orderId);
            if (isAlreadyProcessed) {
                throw new IllegalStateException("Duplicate request for orderId: " + orderId);
            }
            // 이미 처리된 요청 확인 로직
            if (isAlreadyProcessed(paymentKey, orderId)) {
                log.info("이미 처리된 요청입니다: paymentKey={}, orderId={}", paymentKey, orderId);
                return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                        "code", "ALREADY_PROCESSED_PAYMENT",
                        "message", "이미 처리된 결제입니다."
                ));
            }

            Map<String, Object> response = paymentService.confirmPayment(paymentKey, amount, orderId);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            log.error("IOException occurred: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "code", "IO_ERROR",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            log.error("Unexpected error occurred: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "code", "UNKNOWN_ERROR",
                    "message", "An unexpected error occurred"
            ));
        }
    }
    @PostMapping("/save")
    public ResponseEntity<?> savePaymentData(@RequestBody PaymentResponse response) {
        log.info("save - paymentResponse : " + response);
        try {
            log.info("mid" , response.getMId());
            paymentService.savePaymentData(response);
            log.info("paymentData saved success");
            paymentService.saveTicketData(response);
            return ResponseEntity.ok("Payment data and Ticket data saved successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save payment data.");
        }
    }

    @GetMapping("/checkRefund/{paymentKey}")
    public ResponseEntity<?> checkRefundStatus(@PathVariable String paymentKey) {
        try {
            PaymentEntity payment = paymentRepository.findByPaymentKey(paymentKey)
                    .orElseThrow(() -> new RuntimeException("결제를 찾을 수 없습니다."));

            Map<String, Object> response = Map.of(
                    "cancelYN", payment.getCancelYN(),
                    "paymentKey", payment.getPaymentKey()
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("환불 상태 확인 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "환불 상태 확인 중 오류가 발생했습니다."));
        }
    }

    @PutMapping("/refund/{paymentKey}")
    public ResponseEntity<?> processRefund(@PathVariable String paymentKey) {
        try {
            paymentService.processRefund(paymentKey); // 올바른 paymentKey로 처리
            return ResponseEntity.ok("환불이 성공적으로 처리되었습니다.");
        } catch (RuntimeException e) {
            log.error("환불 처리 실패", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            log.error("환불 처리 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("환불 처리 중 오류가 발생했습니다.");
        }
    }

    private boolean isAlreadyProcessed(String paymentKey, String orderId) {
        // 주입된 paymentRepository 인스턴스를 통해 메서드 호출
        return paymentRepository.existsByPaymentKeyAndOrderId(paymentKey, orderId);
    }


}