package org.myweb.jobis.payment.model.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


import com.fasterxml.jackson.databind.introspect.TypeResolutionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.Map;


@Slf4j
@Service
public class PaymentService {

    private static final String TOSS_PAYMENTS_URL = "https://api.tosspayments.com/v1/payments/confirm";
    public static void main(String[] args) {
        String secretKey = "test_sk_zXLkKEypNArWmo50nX3lmeaxYG5R"; // 원래 시크릿 키
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        System.out.println("Encoded Key: " + encodedKey);
    }
    @Value("${tossSecretKey}")
    private String tossSecretKey;


    public Map<String, Object> confirmPayment(String paymentKey, int amount, String orderId) throws IOException {
        Map<String, Object> requestBody = Map.of(
                "paymentKey", paymentKey,
                "amount", amount,
                "orderId", orderId
        );
        log.info("Service requenstBody : " + requestBody);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(TOSS_PAYMENTS_URL))
                .header("Authorization", tossSecretKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(new ObjectMapper().writeValueAsString(requestBody)))
                .build();
        log.info("Service request : " + request);

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("client " + client);
            log.info("Service response : " + response);
            if (response.statusCode() == 200) {
                // JSON 응답을 Map으로 변환
                log.info("response code : " + response.statusCode());
                log.info("response  : " + response);
                return new ObjectMapper().readValue(response.body(), new TypeReference<Map<String, Object>>() {
                });
            } else {
                throw new IOException("Toss API returned an error: " + response.body());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Interrupt 상태 복구
            throw new IOException("Request interrupted", e);
        }
    }
}