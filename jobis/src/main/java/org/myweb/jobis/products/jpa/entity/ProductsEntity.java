package org.myweb.jobis.products.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.myweb.jobis.products.model.dto.Products;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="Products")
public class ProductsEntity {
    @Id
    @Column(name = "PROD_NUMBER", length = 50, nullable = true )
    private int prodNumber;

    @Column(name = "PROD_DESCRIPTION", length = 500, nullable = true )
    private String prodDescription;

    @Column(name = "PROD_AMOUNT", length = 50, nullable = true )
    private int prodAmount;

    @Column(name = "PROD_PERIOD", nullable = true )
    private Timestamp prodPeriod;

    @Column(name = "PROD_SELLABLE", length = 2, nullable = true )
    private String prodSellable;

    // Entity에서 DTO로 변환
    public Products toDto() {
        return Products.builder()
                .prodNumber(prodNumber)
                .prodDescription(prodDescription)
                .prodAmount(prodAmount)
                .prodPeriod(prodPeriod)
                .prodSellable(prodSellable)
                .build();
    }

}
