package com.sevensegment.jobis.cancel.model.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.sevensegment.jobis.cancel.jpa.entity.CancelEntity;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cancel {
    private String cancelKey;
    private String uuid;
    private int prodNumber;
    private String paymentKey;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Timestamp cancelRequestedAt;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Timestamp cancelApprovedAt;

    private String cancelReason;

    // Entity 객체로 변환하는 메서드
    public CancelEntity toEntity() {
        return CancelEntity.builder()
                .cancelKey(cancelKey)
                .uuid(uuid)
                .prodNumber(prodNumber)
                .paymentKey(paymentKey)
                .cancelRequestedAt(cancelRequestedAt)
                .cancelApprovedAt(cancelApprovedAt)
                .cancelReason(cancelReason)
                .build();
    }
}
