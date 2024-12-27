package org.myweb.jobis.products.jpa.repository;

import org.myweb.jobis.products.jpa.entity.ProductsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductsRepository extends JpaRepository<ProductsEntity, Integer>{
}
