package com.sevensegment.jobis.payment.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.sevensegment.jobis.payment.model.dto.Payment;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="Payment")
public class PaymentEntity {
    @Id
    @Column(name = "PAYMENT_KEY", length = 400, nullable = false )
    private String paymentKey;

    @Column(name = "PROD_NUMBER", length = 50, nullable = false )
    private int prodNumber;

    @Column(name = "UUID", length = 50, nullable = false )
    private String uuid;

    @Column(name = "ORDER_ID", length = 100, nullable = false )
    private String orderId;

    @Column(name = "ORDER_NAME", length = 50, nullable = false )
    private String orderName;

    @Column(name = "MID", length = 40, nullable = false )
    private String mId;

    @Column(name = "CURRENCY", length = 50, nullable = false )
    private String currency;

    @Column(name = "TOTAL_AMOUNT", length = 50, nullable = false )
    private int totalAmount;

    @Column(name = "STATUS", length = 20, nullable = false )
    private String status;

    @Column(name = "REQUESTED_AT", nullable = false )
    private Timestamp requestedAt;

    @Column(name = "APPROVED_AT", nullable = false )
    private Timestamp approvedAt;

    @Column(name = "CANCELYN", length = 2)
    private String cancelYN;


    // Entity에서 DTO로 변환
    public Payment toDto() {
        return Payment.builder()
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
