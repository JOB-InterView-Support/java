package org.myweb.jobis.products.model.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.products.jpa.entity.ProductsEntity;
import org.myweb.jobis.products.jpa.repository.ProductsRepository;
import org.myweb.jobis.products.model.dto.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public int getNextProdNumber() {
        int maxProdNumber = productsRepository.findMaxProdNumber();
        return maxProdNumber + 1; // 다음 숫자 계산
    }

    @PersistenceContext
    private EntityManager entityManager;

    public List<Products> getSellableProductsSortedByPrice() {
        List<ProductsEntity> entities = productsRepository.findSellableProductsOrderByPrice();
        return entities.stream().map(ProductsEntity::toDto).toList(); // DTO로 변환
    }

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

    public Page<Products> getProducts(Pageable pageable) {
        Page<ProductsEntity> entitiesPage = productsRepository.findAll(pageable);
        log.debug("페이징된 엔티티 결과: {}", entitiesPage.getContent());
        return entitiesPage.map(ProductsEntity::toDto);
    }

    // sellable YN service
    @Transactional
    public void updateProdSellable(int prodNumber, String sellable) {
        ProductsEntity product = productsRepository.findByProdNumber(prodNumber)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product number"));
        product.setProdSellable(sellable);
        productsRepository.save(product);
    }

    public void updateProduct(ProductsEntity updatedProduct) {
        // 데이터베이스에서 기존 엔티티 조회
        ProductsEntity existingProduct = productsRepository.findById((long) updatedProduct.getProdNumber())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 상품 번호입니다: " + updatedProduct.getProdNumber()));

        // 엔티티의 필드를 업데이트
        existingProduct.setProdName(updatedProduct.getProdName());
        existingProduct.setProdDescription(updatedProduct.getProdDescription());
        existingProduct.setProdAmount(updatedProduct.getProdAmount());
        existingProduct.setProdPeriod(updatedProduct.getProdPeriod());
        existingProduct.setProdNumberOfTime(updatedProduct.getProdNumberOfTime());
        existingProduct.setProdSellable(updatedProduct.getProdSellable());

        // 변경 사항 저장
        productsRepository.save(existingProduct);
        log.info("상품 업데이트 완료: {}", updatedProduct.getProdNumber());
    }

    // 상품 삭제
    public void deleteProduct(int prodNumber) {
        if (!productsRepository.existsById((long) prodNumber)) {
            throw new IllegalArgumentException("해당 상품이 존재하지 않습니다.");
        }
        productsRepository.deleteById((long) prodNumber);
    }
}

