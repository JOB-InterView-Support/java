package org.myweb.jobis.payment.model.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.myweb.jobis.payment.jpa.entity.PaymentEntity;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payment {
    private String payPaymentKey;
    private int prodNumber;
    private String uuid;
    private String payOrderId;
    private String payOrderName;
    private String payMid;
    private String payCurrenoy;
    private int payTotalAmount;
    private String payStatus;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Timestamp payRequestAt;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Timestamp payApprovedAt;

    private String payCancelYN;

    // Entity 객체로 변환하는 메서드
    public PaymentEntity toEntity() {
        return PaymentEntity.builder()
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
