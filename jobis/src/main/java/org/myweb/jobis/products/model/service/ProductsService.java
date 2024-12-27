package org.myweb.jobis.products.model.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.products.jpa.entity.ProductsEntity;
import org.myweb.jobis.products.jpa.repository.ProductsRepository;
import org.myweb.jobis.products.model.dto.Products;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProductsService {
    private final ProductsRepository productsRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public List<Products> getProdInfo() {
        // 예외 처리 포함하여 데이터 조회
        List<Products> prodInfo = new ArrayList<Products>();
        List<ProductsEntity> prodEntities = productsRepository.findAll();
        log.info("prodEntities : " + prodEntities);
        for (ProductsEntity prodEntity : prodEntities) {
            prodInfo.add(prodEntity.toDto());
        }
        return prodInfo;
    }
}

