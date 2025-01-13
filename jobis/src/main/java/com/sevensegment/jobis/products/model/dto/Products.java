package com.sevensegment.jobis.products.model.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.sevensegment.jobis.products.jpa.entity.ProductsEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Products {
    private int prodNumber;
    private String prodName;
    private String prodDescription;
    private int prodAmount;
    private String prodPeriod;
    private String prodSellable;
    private Integer prodNumberOfTime;

    // Entity 객체로 변환하는 메서드
    public ProductsEntity toEntity() {
        return ProductsEntity.builder()
                .prodNumber(prodNumber)
                .prodName(prodName)
                .prodDescription(prodDescription)
                .prodAmount(prodAmount)
                .prodPeriod(prodPeriod)
                .prodSellable(prodSellable)
                .prodNumberOfTime(prodNumberOfTime)
                .build();
    }
}
