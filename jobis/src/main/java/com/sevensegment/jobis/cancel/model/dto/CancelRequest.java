package com.sevensegment.jobis.cancel.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CancelRequest {
    private String paymentKey; // 취소할 결제 키
    private String reason;     // 취소 사유
}