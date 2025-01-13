package com.sevensegment.jobis.payment.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PaymentRequest {
    private String paymentKey;
    private String orderId;
    private int amount;
    private String status;
    private String approvedAt; // ISO-8601 형식을 받을 경우 String으로 설정
}

