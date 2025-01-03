package org.myweb.jobis.payment.controller;

import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.payment.model.dto.PaymentRequest;

import org.myweb.jobis.payment.model.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/api/payments/confirm")
    public ResponseEntity<?> confirmPayment(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestHeader(value = "refreshToken", required = false) String refreshToken,
            @RequestBody PaymentRequest paymentRequest) {

        System.out.println("Authorization Header: " + authorization);
        System.out.println("RefreshToken Header: " + refreshToken);

        if (authorization == null || refreshToken == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Missing Authorization or RefreshToken");
        }

        // 토큰 검증 및 결제 처리 로직
        return ResponseEntity.ok("Payment Confirmed");
    }
    @PostMapping("/confirm")
    public ResponseEntity<?> confirmPayment(@RequestBody PaymentRequest paymentRequest) {
        log.info("결제 승인 요청");
        log.info("paymentRequest : " + paymentRequest);
        return paymentService.confirmPayment(paymentRequest);
    }

    // 결제 성공 및 실패 처리
    @GetMapping("/success")
    public ResponseEntity<String> paymentSuccess(@RequestParam Map<String, String> params) {
        log.info("success url 생성");
        return ResponseEntity.ok("결제 성공: " + params.toString());
    }

    @GetMapping("/fail")
    public ResponseEntity<String> paymentFail(@RequestParam Map<String, String> params) {
        log.info("fail url 생성");
        return ResponseEntity.ok("결제 실패: " + params.toString());
    }
}
