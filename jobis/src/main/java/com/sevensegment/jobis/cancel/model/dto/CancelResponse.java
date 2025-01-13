package com.sevensegment.jobis.cancel.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CancelResponse {
    private String paymentKey;  // 결제 키
    private String status;      // 처리 상태 (SUCCESS/FAILED)
    private String message;     // 처리 결과 메시지
    private String canceledAt;  // 취소된 시간
}
