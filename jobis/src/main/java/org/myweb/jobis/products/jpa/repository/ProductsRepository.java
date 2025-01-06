package org.myweb.jobis.products.jpa.repository;

import org.myweb.jobis.products.jpa.entity.ProductsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ProductsRepository extends JpaRepository<ProductsEntity, Long>{

    Optional<ProductsEntity> findByProdName(String prodName);

}
