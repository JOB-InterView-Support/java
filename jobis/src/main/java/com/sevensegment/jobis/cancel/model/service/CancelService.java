package com.sevensegment.jobis.cancel.model.service;

import lombok.extern.slf4j.Slf4j;
import com.sevensegment.jobis.cancel.jpa.entity.CancelEntity;
import com.sevensegment.jobis.payment.jpa.entity.PaymentEntity;
import com.sevensegment.jobis.cancel.jpa.repository.CancelRepository;
import com.sevensegment.jobis.payment.jpa.repository.PaymentRepository;
import com.sevensegment.jobis.cancel.model.dto.CancelRequest;
import com.sevensegment.jobis.cancel.model.dto.CancelResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Slf4j
@Service
public class CancelService {

    private final PaymentRepository paymentRepository;
    private final CancelRepository cancelRepository;

    @Autowired
    public CancelService(PaymentRepository paymentRepository, CancelRepository cancelRepository) {
        this.paymentRepository = paymentRepository;
        this.cancelRepository = cancelRepository;
    }

    public CancelResponse cancelPayment(CancelRequest cancelRequest) {
        try {
            // 결제 키로 결제 데이터 조회
            PaymentEntity payment = (PaymentEntity) paymentRepository.findByPaymentKey(cancelRequest.getPaymentKey())
                    .orElseThrow(() -> new RuntimeException("결제 데이터를 찾을 수 없습니다: " + cancelRequest.getPaymentKey()));

            // 이미 취소된 결제인지 확인
            if ("CANCELED".equalsIgnoreCase(payment.getStatus())) {
                return CancelResponse.builder()
                        .paymentKey(cancelRequest.getPaymentKey())
                        .status("FAILED")
                        .message("이미 취소된 결제입니다.")
                        .build();
            }

            // 결제 상태를 'CANCELED'로 변경
            payment.setStatus("CANCELED");
            paymentRepository.save(payment);

            // 취소 데이터를 저장
            saveCancelData(cancelRequest);

            // 응답 생성
            return CancelResponse.builder()
                    .paymentKey(cancelRequest.getPaymentKey())
                    .status("SUCCESS")
                    .message("결제가 성공적으로 취소되었습니다.")
                    .canceledAt(Timestamp.valueOf(LocalDateTime.now()).toString())
                    .build();

        } catch (Exception e) {
            log.error("결제 취소 중 오류 발생: {}", e.getMessage());
            return CancelResponse.builder()
                    .paymentKey(cancelRequest.getPaymentKey())
                    .status("FAILED")
                    .message("결제 취소 중 오류가 발생했습니다: " + e.getMessage())
                    .build();
        }
    }

    private void saveCancelData(CancelRequest cancelRequest) {
        CancelEntity cancelEntity = CancelEntity.builder()
                .paymentKey(cancelRequest.getPaymentKey())
                .cancelReason(cancelRequest.getReason())
                .cancelApprovedAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();
        cancelRepository.save(cancelEntity);

        log.info("취소 데이터 저장 완료: {}", cancelEntity);
    }
}
