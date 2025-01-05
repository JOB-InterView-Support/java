package org.myweb.jobis.cancel.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.myweb.jobis.cancel.model.dto.Cancel;

import java.sql.Timestamp;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="Cancel")
public class CancelEntity {
    @Id
    @Column(name = "CANCEL_KEY", length = 400, nullable = true )
    private String cancelKey;

    @Column(name = "UUID", length = 50, nullable = true )
    private String uuid;

    @Column(name = "PROD_NUMBER", length = 50, nullable = false )
    private int prodNumber;

    @Column(name = "PAY_PAYMENT_KEY", length = 400, nullable = false )
    private String payPaymentKey;

    @Column(name = "CANCEL_REQUESTED_AT", nullable = true )
    private Timestamp cancelRequestedAt;

    @Column(name = "CANCEL_APPROVED_AT", nullable = true )
    private Timestamp cancelApprovedAt;

    // Entity에서 DTO로 변환
    public Cancel toDto() {
        return Cancel.builder()
                .cancelKey(cancelKey)
                .uuid(uuid)
                .prodNumber(prodNumber)
                .payPaymentKey(payPaymentKey)
                .cancelRequestedAt(cancelRequestedAt)
                .cancelApprovedAt(cancelApprovedAt)
                .build();
    }

}
