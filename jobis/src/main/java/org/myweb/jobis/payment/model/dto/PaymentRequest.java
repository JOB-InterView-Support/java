package org.myweb.jobis.payment.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentRequest{
    private String paymentKey;  // 결제 키
    private String orderId;     // 주문 ID
    private int amount;         // 결제 금액
    private String orderName;   // 주문 이름
}
