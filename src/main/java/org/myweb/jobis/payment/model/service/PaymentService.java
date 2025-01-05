package org.myweb.jobis.payment.model.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


import com.fasterxml.jackson.databind.introspect.TypeResolutionContext;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.Basic;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.payment.jpa.entity.PaymentEntity;
import org.myweb.jobis.payment.jpa.repository.PaymentRepository;
import org.myweb.jobis.payment.model.dto.PaymentRequest;
import org.myweb.jobis.payment.model.dto.PaymentResponse;
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
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@Service
public class PaymentService {
    private static final String TOSS_PAYMENTS_URL = "https://api.tosspayments.com/v1/payments/confirm";
    private final PaymentRepository paymentRepository;
    private final HttpServletRequest httpServletRequest;

    @Value("${tossSecretKey}")
    private String tossSecretKey;

    String secretKey = tossSecretKey; // 원래 시크릿 키
    private String encodedKey;

    @PostConstruct
    private void init() {
        encodedKey = Base64.getEncoder().encodeToString(tossSecretKey.getBytes());
    }

    private final Set<String> processingKeys = ConcurrentHashMap.newKeySet();


    public void processPayment(PaymentRequest paymentRequest) throws Exception {
        // 결제 요청 처리
        log.info("Processing payment for order: {}", paymentRequest.getOrderId());

        // 실제 결제 API 호출 등의 로직을 여기에 추가
        // 예: TossPayments API를 호출하여 결제 요청을 전송하는 코드

        // 결제 성공 시 처리
        log.info("결제 요청 성공");

        // 결제 실패 시 예외를 던져서 컨트롤러에서 처리하도록 할 수도 있음
        if (paymentRequest.equals(null)) {
            throw new Exception("결제 실패");
        }

        // 결제 처리 성공 시 로깅 등 추가 작업
        log.info("Payment processed successfully for order: {}", paymentRequest.getOrderId());
        log.info("PaymentRequest: {}", paymentRequest);
    }


    public Map<String, Object> confirmPayment(String paymentKey, int amount, String orderId) throws IOException {
        // 중복 요청 방지 로직
        if (!processingKeys.add(paymentKey)) {
            log.warn("Duplicate request detected for paymentKey: " + paymentKey);
            throw new IllegalStateException("Request is already being processed");
        }

        try {
            Map<String, Object> requestBody = Map.of(
                    "paymentKey", paymentKey,
                    "amount", amount,
                    "orderId", orderId
            );
            log.info("Service requestBody : " + requestBody);

            String authorizationHeader = "Basic " + encodedKey;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(TOSS_PAYMENTS_URL))
                    .header("Authorization", authorizationHeader)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(new ObjectMapper().writeValueAsString(requestBody)))
                    .build();
            log.info("encodeKey : " + encodedKey);
            log.info("Service request : " + request);

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("client : " + client);
            log.info("Service response : " + response);

            if (response.statusCode() == 200) {
                // JSON 응답을 Map으로 변환
                log.info("response code : " + response.statusCode());
                log.info("response body : " + response.body());
                return new ObjectMapper().readValue(response.body(), new TypeReference<Map<String, Object>>() {
                });
            } else {
                throw new IOException("Toss API returned an error: " + response.body());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Interrupt 상태 복구
            throw new IOException("Request interrupted", e);
        } finally {
            // 요청 처리 완료 후 키 제거
            processingKeys.remove(paymentKey);
            log.info("Service End");
        }
    }


    @Autowired
    public PaymentService(PaymentRepository paymentRepository, HttpServletRequest httpServletRequest) {
        this.paymentRepository = paymentRepository;
        this.httpServletRequest = httpServletRequest;
    }

    private void logRequestHeaders(HttpServletRequest request) {
        log.info("Request Headers:");
        request.getHeaderNames().asIterator()
                .forEachRemaining(headerName ->
                        log.info("{}: {}", headerName, request.getHeader(headerName))
                );
    }

    public void savePaymentData(PaymentResponse response) {
        try {
            // 요청 헤더 로깅
            logRequestHeaders(httpServletRequest);

            // DateTimeFormatter를 사용하여 String을 LocalDateTime으로 변환
            DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
            LocalDateTime approvedAtLocalDateTime = LocalDateTime.parse(response.getApprovedAt(), formatter);

            // LocalDateTime을 Timestamp로 변환
            Timestamp approvedAtTimestamp = Timestamp.valueOf(approvedAtLocalDateTime);

            // 엔티티 빌더를 사용하여 데이터 저장
            PaymentEntity paymentEntity = PaymentEntity.builder()
                    .paymentKey(response.getPaymentKey())
                    .orderId(response.getOrderId())
                    .totalAmount(response.getAmount()) // 엔티티의 totalAmount와 매핑
                    .status(response.getStatus())
                    .approvedAt(approvedAtTimestamp) // 변환된 Timestamp 설정
                    .build();

            paymentRepository.save(paymentEntity);
            log.info("Payment data saved: {}", paymentEntity);
        } catch (Exception e) {
            log.error("Error saving payment data: {}", e.getMessage());
            throw new RuntimeException("Failed to save payment data", e);
        }
    }

}