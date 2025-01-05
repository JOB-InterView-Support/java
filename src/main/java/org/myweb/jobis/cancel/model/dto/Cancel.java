package org.myweb.jobis.cancel.model.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.myweb.jobis.cancel.jpa.entity.CancelEntity;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cancel {
    private String cancelKey;
    private String uuid;
    private int prodNumber;
    private String payPaymentKey;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Timestamp cancelRequestedAt;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Timestamp cancelApprovedAt;


    // Entity 객체로 변환하는 메서드
    public CancelEntity toEntity() {
        return CancelEntity.builder()
                .cancelKey(cancelKey)
                .uuid(uuid)
                .prodNumber(prodNumber)
                .payPaymentKey(payPaymentKey)
                .cancelRequestedAt(cancelRequestedAt)
                .cancelApprovedAt(cancelApprovedAt)
                .build();
    }
}
