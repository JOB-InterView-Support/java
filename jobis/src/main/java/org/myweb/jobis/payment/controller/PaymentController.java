package org.myweb.jobis.payment.controller;


import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.payment.jpa.repository.PaymentRepository;
import org.myweb.jobis.payment.model.dto.Payment;
import org.myweb.jobis.payment.model.dto.PaymentRequest;
import org.myweb.jobis.payment.model.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
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
            // PaymentService에서 결제 요청 처리
            paymentService.processPayment(paymentRequest);
            log.info("paymentRequest" + paymentRequest);
            return ResponseEntity.ok().body("결제 요청 성공");
        } catch (Exception e) {
            // 예외 처리
            log.error("결제 요청 처리 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("결제 요청 처리 실패");
        }
    }


    @PostMapping("/payment/confirm")
    public ResponseEntity<?> confirmPayment(@RequestBody Map<String, Object> requestData) {
        log.info("Confirm Payment API called with data: {}", requestData);

        try {
            String paymentKey = (String) requestData.get("paymentKey");
            int amount = (int) requestData.get("amount");
            String orderId = (String) requestData.get("orderId");

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

    private boolean isAlreadyProcessed(String paymentKey, String orderId) {
        // 주입된 paymentRepository 인스턴스를 통해 메서드 호출
        return paymentRepository.existsByPaymentKeyAndOrderId(paymentKey, orderId);
    }
}