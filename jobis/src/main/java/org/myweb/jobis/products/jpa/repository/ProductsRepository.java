package org.myweb.jobis.products.jpa.repository;

import org.myweb.jobis.products.jpa.entity.ProductsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ProductsRepository extends JpaRepository<ProductsEntity, Long>{

    @Query("SELECT MAX(p.prodNumber) FROM ProductsEntity p")
    int findMaxProdNumber(); // 최대값 조회

    Optional<ProductsEntity> findByProdName(String prodName);

    @Query("SELECT p FROM ProductsEntity p WHERE p.prodSellable = 'Y' ORDER BY p.prodAmount DESC")
    List<ProductsEntity> findSellableProductsOrderByPrice(); // 필터링 및 정렬

    Optional<ProductsEntity> findByProdNumber(int prodNumber);
}
