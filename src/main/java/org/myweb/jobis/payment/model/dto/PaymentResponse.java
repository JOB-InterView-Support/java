package org.myweb.jobis.payment.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {
    private String paymentKey;
    private String orderId;
    private int amount; // 필드명은 엔티티의 totalAmount와 매칭
    private String status;
    private String approvedAt; // 응답으로 받은 승인 시간
}