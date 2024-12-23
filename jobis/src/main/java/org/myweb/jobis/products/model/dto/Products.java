package org.myweb.jobis.products.model.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.myweb.jobis.payment.jpa.entity.PaymentEntity;
import org.myweb.jobis.products.jpa.entity.ProductsEntity;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Products {
    private int prodNumber;
    private String prodDescription;
    private int prodAmount;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Timestamp prodPeriod;

    private String prodSellable;



    // Entity 객체로 변환하는 메서드
    public ProductsEntity toEntity() {
        return ProductsEntity.builder()
                .prodNumber(prodNumber)
                .prodDescription(prodDescription)
                .prodAmount(prodAmount)
                .prodPeriod(prodPeriod)
                .prodSellable(prodSellable)
                .build();
    }
}
