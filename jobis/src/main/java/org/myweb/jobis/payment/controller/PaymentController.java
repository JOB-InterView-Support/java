package org.myweb.jobis.payment.controller;

import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.payment.model.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/confirm")
    public ResponseEntity<String> confirmPayment(
            @RequestBody String orderId,
            @RequestBody int amount,
            @RequestBody String paymentKey
    ) {
        log.info("전달온 orderId : {}", orderId);
        ResponseEntity<String> response = paymentService.confirmPayment(orderId, amount, paymentKey);
        log.info("response : " + response);
        return response;
    }

}
