package org.myweb.jobis.payment.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.myweb.jobis.payment.model.dto.Payment;

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
    private String mid;

    @Column(name = "CURRENOY", length = 50, nullable = false )
    private String currenoy;

    @Column(name = "TOTAL_AMOUNT", length = 50, nullable = false )
    private int totalAmount;

    @Column(name = "STATUS", length = 20, nullable = false )
    private String status;

    @Column(name = "REQUEST_AT", nullable = false )
    private Timestamp requestAt;

    @Column(name = "APPROVED_AT", nullable = false )
    private Timestamp approvedAt;

    @Column(name = "CANCELYN", length = 2)
    private String cancelYN;


    @PrePersist
    public void onPrePersist() {
        if (requestAt == null) {
            this.requestAt = new Timestamp(System.currentTimeMillis());  // Set current date + time
        }
    }

    // Entity에서 DTO로 변환
    public Payment toDto() {
        return Payment.builder()
                .payPaymentKey(paymentKey)
                .prodNumber(prodNumber)
                .uuid(uuid)
                .payOrderId(orderId)
                .payOrderName(orderName)
                .payMid(mid)
                .payCurrenoy(currenoy)
                .payTotalAmount(totalAmount)
                .payStatus(status)
                .payRequestAt(requestAt)
                .payApprovedAt(approvedAt)
                .payCancelYN(cancelYN)
                .build();
    }

}
