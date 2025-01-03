package org.myweb.jobis.payment.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.payment.model.dto.Payment;
import org.myweb.jobis.payment.model.dto.PaymentRequest;
import org.myweb.jobis.payment.jpa.repository.PaymentRepository;
import org.myweb.jobis.payment.jpa.entity.PaymentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Value("${tossClientKey}")
    private String tossClientKey;

    // 결제 승인 처리
    public ResponseEntity<?> confirmPayment(PaymentRequest paymentRequest) {
        try {
            // Toss Payments API 호출
            String apiUrl = "https://api.tosspayments.com/v1/payments/confirm";
            String clientKey = tossClientKey;
            String authorizationHeader = "Basic " + java.util.Base64.getEncoder().encodeToString(clientKey.getBytes());

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authorizationHeader);
            headers.set("Content-Type", "application/json");

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("paymentKey", paymentRequest.getPaymentKey());
            requestBody.put("orderId", paymentRequest.getOrderId());
            requestBody.put("amount", paymentRequest.getAmount());

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            log.info("Toss API 호출 요청 데이터: {}", requestBody);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);

            log.info("Toss API 응답: {}", response.getBody());
            if (response.getStatusCode() == HttpStatus.OK) {
                // Payment 객체 인스턴스화 후 값 설정
                Payment payment = new Payment();  // 인스턴스화
                payment.setPaymentKey(paymentRequest.getPaymentKey());  // 인스턴스에서 호출
                payment.setOrderId(paymentRequest.getOrderId());
                payment.setOrderName("Test Order");  // 예시 값
                payment.setStatus("APPROVED");  // 결제 상태
                payment.setTotalAmount(paymentRequest.getAmount());
                payment.setApprovedAt(new Timestamp(System.currentTimeMillis()));

                // Payment 객체를 Entity로 변환 후 DB에 저장
                PaymentEntity paymentEntity = payment.toEntity();
                paymentRepository.save(paymentEntity);

                return ResponseEntity.ok("결제 승인 및 저장 완료: " + response.getBody());
            } else {
                log.error("Toss Payments API 응답 오류: {}", response.getBody());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("결제 승인 실패");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("결제 승인 중 오류 발생: " + e.getMessage());
        }
    }
}
