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
    @Column(name = "PAY_PAYMENT_KEY", length = 400, nullable = false )
    private String payPaymentKey;

    @Column(name = "PROD_NUMBER", length = 50, nullable = false )
    private int prodNumber;

    @Column(name = "UUID", length = 50, nullable = false )
    private String uuid;

    @Column(name = "PAY_ORDER_ID", length = 100, nullable = false )
    private String payOrderId;

    @Column(name = "PAY_ORDER_NAME", length = 50, nullable = false )
    private String payOrderName;

    @Column(name = "PAY_MID", length = 40, nullable = false )
    private String payMid;

    @Column(name = "PAY_CURRENOY", length = 50, nullable = false )
    private String payCurrenoy;

    @Column(name = "PAY_TOTAL_AMOUNT", length = 50, nullable = false )
    private int payTotalAmount;

    @Column(name = "PAY_STATUS", length = 20, nullable = false )
    private String payStatus;

    @Column(name = "PAY_REQUEST_AT", nullable = false )
    private Timestamp payRequestAt;

    @Column(name = "PAY_APPROVED_AT", nullable = false )
    private Timestamp payApprovedAt;

    @Column(name = "PAY_CANCELYN", length = 2)
    private String payCancelYN;


    @PrePersist
    public void onPrePersist() {
        if (payRequestAt == null) {
            this.payRequestAt = new Timestamp(System.currentTimeMillis());  // Set current date + time
        }
    }

    // Entity에서 DTO로 변환
    public Payment toDto() {
        return Payment.builder()
                .payPaymentKey(payPaymentKey)
                .prodNumber(prodNumber)
                .uuid(uuid)
                .payOrderId(payOrderId)
                .payOrderName(payOrderName)
                .payMid(payMid)
                .payCurrenoy(payCurrenoy)
                .payTotalAmount(payTotalAmount)
                .payStatus(payStatus)
                .payRequestAt(payRequestAt)
                .payApprovedAt(payApprovedAt)
                .payCancelYN(payCancelYN)
                .build();
    }

}
