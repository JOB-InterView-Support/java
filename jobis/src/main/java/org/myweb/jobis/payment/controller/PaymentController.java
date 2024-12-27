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
            @RequestParam String paymentKey,
            @RequestParam int amount,
            @RequestParam String orderId
    ) {
        ResponseEntity<String> response = paymentService.confirmPayment(paymentKey, amount, orderId);
        log.info("response : " + response);
        return response;
    }

}
