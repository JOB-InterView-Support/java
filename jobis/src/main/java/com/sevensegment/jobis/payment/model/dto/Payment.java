package com.sevensegment.jobis.payment.model.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.sevensegment.jobis.payment.jpa.entity.PaymentEntity;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Payment {
    private String paymentKey;
    private int prodNumber;
    private String uuid;
    private String orderId;
    private String orderName;
    private String mId;
    private String currency;
    private int totalAmount;
    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Timestamp requestedAt;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Timestamp approvedAt;

    private String cancelYN;

    // Entity 객체로 변환하는 메서드
    public PaymentEntity toEntity() {
        return PaymentEntity.builder()
                .paymentKey(paymentKey)
                .prodNumber(prodNumber)
                .uuid(uuid)
                .orderId(orderId)
                .orderName(orderName)
                .mId(mId)
                .currency(currency)
                .totalAmount(totalAmount)
                .status(status)
                .requestedAt(requestedAt)
                .approvedAt(approvedAt)
                .cancelYN(cancelYN)
                .build();
    }
}
