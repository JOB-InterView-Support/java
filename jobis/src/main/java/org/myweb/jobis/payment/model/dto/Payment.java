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
    private String paymentKey;
    private int prodNumber;
    private String uuid;
    private String orderId;
    private String orderName;
    private String mid;
    private String currenoy;
    private int totalAmount;
    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Timestamp requestAt;

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
                .mid(mid)
                .currenoy(currenoy)
                .totalAmount(totalAmount)
                .status(status)
                .requestAt(requestAt)
                .approvedAt(approvedAt)
                .cancelYN(cancelYN)
                .build();
    }


}
