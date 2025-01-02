package org.myweb.jobis.payment.model.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


import com.fasterxml.jackson.databind.introspect.TypeResolutionContext;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.Basic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
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

    @Value("${tossSecretKey}")
    private String tossSecretKey;

    String secretKey = tossSecretKey; // 원래 시크릿 키
    private String encodedKey;

    @PostConstruct
    private void init() {
        encodedKey = Base64.getEncoder().encodeToString(tossSecretKey.getBytes());
    }


    public Map<String, Object> confirmPayment(String paymentKey, int amount, String orderId) throws IOException {
        Map<String, Object> requestBody = Map.of(
                "paymentKey", paymentKey,
                "amount", amount,
                "orderId", orderId
        );
        log.info("Service requenstBody : " + requestBody);

        String authorizationHeader = "Basic " + encodedKey;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(TOSS_PAYMENTS_URL))
                .header("Authorization", authorizationHeader)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(new ObjectMapper().writeValueAsString(requestBody)))
                .build();
        log.info("encodeKey : " + encodedKey);
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
            } else if (response.statusCode() == 400 && response.body().contains("PROVIDER_ERROR")) {
                // 재시도 로직 추가
                log.warn("PROVIDER_ERROR, retrying...");
                Thread.sleep(1000); // 1초 대기 후 재시도
                return confirmPayment(paymentKey, amount, orderId); // 재시도 호출
            } else {
                throw new IOException("Toss API returned an error: " + response.body());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Interrupt 상태 복구
            throw new IOException("Request interrupted", e);
        } finally{
            log.info("Service End ");
        }
    }
}