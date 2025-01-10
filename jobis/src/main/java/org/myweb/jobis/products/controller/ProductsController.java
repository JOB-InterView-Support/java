package org.myweb.jobis.products.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.products.jpa.entity.ProductsEntity;
import org.myweb.jobis.products.jpa.repository.ProductsRepository;
import org.myweb.jobis.products.model.dto.Products;
import org.myweb.jobis.products.model.service.ProductsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@CrossOrigin
public class ProductsController {
    private final ProductsService productsService;
    private final ProductsRepository productsRepository;

    @PostMapping("/register")
    public ResponseEntity<Products> registerProduct(@RequestBody Products productDto) {
        // 다음 prodNumber 가져오기
        int nextProdNumber = productsService.getNextProdNumber();
        productDto.setProdNumber(nextProdNumber);
        productDto.setProdSellable("N");

        // 엔티티 저장
        ProductsEntity productEntity = productDto.toEntity();
        ProductsEntity savedEntity = productsRepository.save(productEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEntity.toDto());
    }

    // 상품 정보 불러오기
    @GetMapping("/sellable")
    public ResponseEntity<List<Products>> getSellableProducts() {
        List<Products> products = productsService.getSellableProductsSortedByPrice();
        return ResponseEntity.ok(products);
    }
}
