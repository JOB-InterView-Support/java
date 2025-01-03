package org.myweb.jobis.payment.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PaymentRequest{
    private String orderId;     // 주문 ID
    private int amount;         // 결제 금액
    private String paymentKey;  // 결제 키
}
