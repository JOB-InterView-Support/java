package org.myweb.jobis.products.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.products.jpa.entity.ProductsEntity;
import org.myweb.jobis.products.model.dto.Products;
import org.myweb.jobis.products.model.service.ProductsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@CrossOrigin
public class ProductsController {
    private final ProductsService productsService;

    // 상품 정보 불러오기
    @GetMapping
    public ResponseEntity<List<Products>> getUserInfo() {
        log.info("컨트롤러 시작");
        List<Products> prodInfo = productsService.getProdInfo();
        log.info("prodInfo : " + prodInfo);
        return ResponseEntity.ok(prodInfo);
    }
}
