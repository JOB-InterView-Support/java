package org.myweb.jobis.payment.model.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.payment.jpa.entity.PaymentEntity;
import org.myweb.jobis.payment.jpa.repository.PaymentRepository;
import org.myweb.jobis.payment.model.dto.PaymentRequest;
import org.myweb.jobis.payment.model.dto.PaymentResponse;
import org.myweb.jobis.products.jpa.entity.ProductsEntity;
import org.myweb.jobis.products.jpa.repository.ProductsRepository;

import org.myweb.jobis.security.jwt.JWTUtil;
import org.myweb.jobis.ticket.jpa.entity.TicketEntity;
import org.myweb.jobis.ticket.jpa.repository.TicketRepository;
import org.myweb.jobis.ticket.model.dto.Ticket;
import org.myweb.jobis.user.jpa.entity.UserEntity;
import org.myweb.jobis.user.jpa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@Service
public class PaymentService {
    private static final String TOSS_PAYMENTS_URL = "https://api.tosspayments.com/v1/payments/confirm";
    private final PaymentRepository paymentRepository;
    private final ProductsRepository productsRepository;
    private final UserRepository userRepository; // Inject the UserRepository
    private final TicketRepository ticketRepository;

    private final HttpServletRequest httpServletRequest;
    private final JWTUtil jwtUtil; // JWTUtil 객체 선언


    @Autowired
    public PaymentService(PaymentRepository paymentRepository, ProductsRepository productsRepository, HttpServletRequest httpServletRequest, JWTUtil jwtUtil, UserRepository userRepository, TicketRepository ticketRepository) {
        this.paymentRepository = paymentRepository;
        this.productsRepository = productsRepository;
        this.userRepository = userRepository;
        this.ticketRepository = ticketRepository;
        this.httpServletRequest = httpServletRequest;
        this.jwtUtil = jwtUtil;
    }

    @Value("${tossSecretKey}")
    private String tossSecretKey;

    String secretKey = tossSecretKey; // 원래 시크릿 키
    private String encodedKey;

    @PostConstruct
    private void init() {
        encodedKey = Base64.getEncoder().encodeToString(tossSecretKey.getBytes());
    }

    private final Set<String> processingKeys = ConcurrentHashMap.newKeySet();

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        log.info("Authorization Header: {}", bearerToken);  // 헤더 출력
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);  // "Bearer " 제외한 토큰 부분 추출
            log.info("Extracted Token: {}", token);  // 토큰 확인
            return token;
        }
        log.info("Authorization header not found or invalid format.");
        return null; // 토큰이 없으면 null 반환
    }

    public void processPayment(PaymentRequest paymentRequest) throws Exception {
        try {
            // 1. HTTP 요청에서 토큰 추출
            String token = getTokenFromRequest(httpServletRequest);

            // 2. JWTUtil을 사용하여 userId 추출
            String userId = jwtUtil.getUserIdFromToken(token);
            log.info("Extracted userId from token: {}", userId);

            if (userId == null) {
                throw new RuntimeException("유효하지 않은 userId");
            }

            // 3. UserEntity 조회
            UserEntity userEntity = userRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("User not found for userId: " + userId));
            String uuid = userEntity.getUuid();
            log.info("User uuid: {}", uuid);

            // 4. PaymentEntity에서 uuid로 결제 정보 조회
            List<PaymentEntity> existingPayments = paymentRepository.findByUuidAndCancelYN(uuid, "N");
            if (!existingPayments.isEmpty()) {
                for (PaymentEntity payment : existingPayments) {
                    String paymentKey = payment.getPaymentKey();
                    log.info("Existing payment found with paymentKey: {}", paymentKey);

                    // 5. TicketEntity에서 paymentKey로 티켓 조회
                    Optional<TicketEntity> ticket = ticketRepository.findTicketByPaymentKey(paymentKey);
                    if (ticket.isPresent() && ticket.get().getTicketCount() > 0) {
                        log.warn("이용권을 이미 가지고 있습니다. ticketKey: {}, ticketCount: {}",
                                ticket.get().getTicketKey(), ticket.get().getTicketCount());
                        throw new IllegalStateException("이용권을 이미 가지고 있습니다.");
                    }
                }
            }
            // 6. 결제 요청 처리
            log.info("Processing payment for order: {}", paymentRequest.getOrderId());
            // 결제 API 호출 로직 추가
            log.info("Payment processed successfully for order: {}", paymentRequest.getOrderId());
        } catch (IllegalStateException e) {
            log.error("결제 요청 실패: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage()); // 409 상태 코드와 메시지 반환
        } catch (Exception e) {
            log.error("결제 요청 처리 중 오류 발생: {}", e.getMessage());
            throw e;
        }
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

    private void logRequestHeaders(HttpServletRequest request) {
        log.info("Request Headers:");
        request.getHeaderNames().asIterator()
                .forEachRemaining(headerName ->
                        log.info("{}: {}", headerName, request.getHeader(headerName))
                );
    }



    public void savePaymentData(PaymentResponse response) {
        try {
            // HTTP 요청에서 토큰 추출
            String token = getTokenFromRequest(httpServletRequest);

            // JWTUtil을 사용하여 토큰에서 userId 추출
            String userId = jwtUtil.getUserIdFromToken(token);
            log.info("Extracted userId from token: {}", userId);

            if (userId == null) {
                throw new RuntimeException("유효하지 않은 userId");
            }
            // UserRepository에서 userId로 UserEntity를 조회
            UserEntity userEntity = userRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("User not found for userId: " + userId));

            // UserEntity에서 uuid를 추출
            String uuid = userEntity.getUuid();
            log.info("User uuid: {}", uuid);

            // Products에서 prodName과 orderName이 일치하는 prodNumber 조회
            ProductsEntity productEntity = productsRepository.findByProdName(response.getOrderName())
                    .orElseThrow(() -> new RuntimeException("Product not found for orderName: " + response.getOrderName()));
            log.info("Products : {}", productEntity);

            // productEntity에서 prodNumber을 추출
            int prodNumber = productEntity.getProdNumber();
            log.info("User prodNumber: {}", prodNumber);

            // DateTimeFormatter를 사용하여 String을 LocalDateTime으로 변환
            DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
            LocalDateTime approvedAtLocalDateTime = LocalDateTime.parse(response.getApprovedAt(), formatter);
            LocalDateTime requestedAtLocalDateTime = LocalDateTime.parse(response.getRequestedAt(), formatter);


            // LocalDateTime을 Timestamp로 변환
            Timestamp approvedAtTimestamp = Timestamp.valueOf(approvedAtLocalDateTime);
            Timestamp requestedAtTimestamp = Timestamp.valueOf(requestedAtLocalDateTime);


            // 엔티티 빌더를 사용하여 데이터 저장
            PaymentEntity paymentEntity = PaymentEntity.builder()
                    .paymentKey(response.getPaymentKey())
                    .prodNumber(prodNumber)
                    .uuid(uuid) // 추출된 userId 저장
                    .orderId(response.getOrderId())
                    .orderName(response.getOrderName())
                    .mId("tvivarepublica")
                    .currency(response.getCurrency())
                    .totalAmount(response.getTotalAmount()) // 엔티티의 totalAmount와 매핑
                    .status(response.getStatus())
                    .requestedAt(requestedAtTimestamp)
                    .approvedAt(approvedAtTimestamp) // 변환된 Timestamp 설정
                    .cancelYN("N")
                    .build();

            log.info("paymentEntity: {}", paymentEntity);
            paymentRepository.save(paymentEntity);
            log.info("Payment data saved: {}", paymentEntity);
        } catch (Exception e) {
            log.error("Error saving payment data: {}", e.getMessage());
            throw new RuntimeException("Failed to save payment data", e);
        }
    }

    public void saveTicketData(PaymentResponse response) {
        try {
            // HTTP 요청에서 토큰 추출
            String token = getTokenFromRequest(httpServletRequest);

            // JWTUtil을 사용하여 토큰에서 userId 추출
            String userId = jwtUtil.getUserIdFromToken(token);
            log.info("Extracted userId from token: {}", userId);

            if (userId == null) {
                throw new RuntimeException("유효하지 않은 userId");
            }

            // UserRepository에서 userId로 UserEntity를 조회
            UserEntity userEntity = userRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("User not found for userId: " + userId));

            // UserEntity에서 uuid를 추출
            String uuid = userEntity.getUuid();
            log.info("User uuid: {}", uuid);

            // Products에서 prodName과 orderName이 일치하는 ProductsEntity 조회
            ProductsEntity productEntity = productsRepository.findByProdName(response.getOrderName())
                    .orElseThrow(() -> new RuntimeException("Product not found for orderName: " + response.getOrderName()));
            log.info("ProductsEntity: {}", productEntity);

            // DateTimeFormatter를 사용하여 String을 LocalDateTime으로 변환
            DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
            LocalDateTime approvedAtLocalDateTime = LocalDateTime.parse(response.getApprovedAt(), formatter);

            // LocalDateTime을 Timestamp로 변환
            Timestamp approvedAtTimestamp = Timestamp.valueOf(approvedAtLocalDateTime);

            // ticketEndDate 계산
            Timestamp ticketEndDate;
            int prodNumber = productEntity.getProdNumber();
            if (prodNumber == 1) {
                ticketEndDate = Timestamp.valueOf(approvedAtLocalDateTime.plusMonths(6));
            } else if (prodNumber == 2) {
                ticketEndDate = Timestamp.valueOf(approvedAtLocalDateTime.plusMonths(3));
            } else if (prodNumber == 3) {
                ticketEndDate = Timestamp.valueOf(approvedAtLocalDateTime.plusHours(24));
            } else {
                throw new IllegalArgumentException("Invalid prodNumber: " + prodNumber);
            }

            // ticketCount 및 numberOfTime 계산
            int ticketCount = switch (prodNumber) {
                case 1 -> 6;
                case 2 -> 3;
                case 3 -> 1;
                default -> throw new IllegalArgumentException("Invalid prodNumber: " + prodNumber);
            };

            // 엔티티 빌더를 사용하여 데이터 저장
            TicketEntity ticketEntity = TicketEntity.builder()
                    .ticketKey(UUID.randomUUID().toString())
                    .uuid(uuid)
                    .paymentKey(response.getPaymentKey())
                    .prodNumber(prodNumber) // prodNumber를 직접 설정
                    .ticketName(response.getOrderName())
                    .ticketAmount(response.getTotalAmount())
                    .ticketPeriod(productEntity.getProdPeriod())
                    .ticketCount(ticketCount)
                    .ticketStartDate(approvedAtTimestamp)
                    .ticketEndDate(ticketEndDate)
                    .prodNumberOfTime(ticketCount)
                    .build();

            log.info("TicketEntity: {}", ticketEntity);
            ticketRepository.save(ticketEntity);
            log.info("Ticket data saved: {}", ticketEntity);
        } catch (Exception e) {
            log.error("Error saving ticket data: {}", e.getMessage());
            throw new RuntimeException("Failed to save ticket data", e);
        }
    }

    @Transactional
    public void processRefund(String paymentKey) {
        log.info("환불 요청 처리 중 paymentKey: {}", paymentKey);

        // 1. PaymentEntity 조회
        PaymentEntity payment = paymentRepository.findByPaymentKey(paymentKey)
                .orElseThrow(() -> new RuntimeException("해당 paymentKey로 결제를 찾을 수 없습니다. paymentKey: " + paymentKey));

        // 2. 이미 환불된 경우 확인
        if ("Y".equals(payment.getCancelYN())) {
            log.error("이미 환불된 결제입니다. paymentKey: {}", paymentKey);
            throw new IllegalStateException("이미 환불된 결제입니다. paymentKey: " + paymentKey);
        }

        // 3. PaymentEntity의 cancelYN 값을 Y로 변경
        payment.setCancelYN("Y");
        paymentRepository.save(payment);
        log.info("PaymentEntity 환불 처리 완료. paymentKey: {}", paymentKey);

        // 4. TicketEntity의 ticketCount를 0으로 설정
        TicketEntity ticket = ticketRepository.findTicketByPaymentKey(paymentKey)
                .orElseThrow(() -> new RuntimeException("해당 paymentKey로 티켓을 찾을 수 없습니다. paymentKey: " + paymentKey));

        ticket.setTicketCount(0); // ticketCount를 0으로 설정
        ticketRepository.save(ticket);
        log.info("TicketEntity 업데이트 완료. ticketKey: {}, ticketCount: 0", ticket.getTicketKey());

        log.info("환불이 성공적으로 처리되었습니다. paymentKey: {}", paymentKey);
    }
} // 25.01.07 최종 수정