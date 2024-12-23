package org.myweb.jobis.products.jpa.repository;

import org.myweb.jobis.products.jpa.entity.ProductsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductsRepository extends JpaRepository<ProductsEntity, String>, ProductsRepositoryCustom {
    // 기본 JPA 메소드 사용 가능
}
