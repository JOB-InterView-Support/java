package org.myweb.jobis.payment.controller;


import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.payment.model.dto.Payment;
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
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/confirm")
    public ResponseEntity<?> confirmPayment(@RequestBody Map<String, Object> requestData) {
        log.info("Confirm Payment API called with data: {}", requestData);
        // 기존 코드 유지
        try {
            String paymentKey = (String) requestData.get("paymentKey");
            int amount = (int) requestData.get("amount");
            String orderId = (String) requestData.get("orderId");

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
}