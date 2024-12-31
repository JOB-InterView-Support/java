package org.myweb.jobis.payment.model.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Service
public class PaymentService {

    private static final String TOSS_CONFIRM_URL = "https://api.tosspayments.com/v1/payments/confirm";
    private static final String SECRET_KEY = "test_sk_zXLkKEypNArWmo50nX3lmeaxYG5R";

    @Autowired
    private RestTemplate restTemplate;

    public ResponseEntity<String> confirmPayment(String paymentKey, int amount, String orderId) {
        // 1. Basic Authorization 헤더 생성
        String credentials = SECRET_KEY + ":";
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Basic " + encodedCredentials);
        log.info(encodedCredentials);


        // 2. 요청 바디 생성
        Map<String, Object> body = new HashMap<>();
        body.put("paymentKey", paymentKey);
        body.put("amount", amount);
        body.put("orderId", orderId);
        log.info("body : " + body);

        // 3. HTTP 요청 객체 생성
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // 4. API 호출
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    TOSS_CONFIRM_URL,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );
            log.info("response : " + response);
            return response; // 성공 응답 반환
        } catch (Exception e) {
            throw new RuntimeException("결제 승인 요청 중 오류 발생: " + e.getMessage());
        }
    }
}
