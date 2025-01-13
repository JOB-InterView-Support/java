package org.myweb.jobis.products.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.products.jpa.entity.ProductsEntity;
import org.myweb.jobis.products.jpa.repository.ProductsRepository;
import org.myweb.jobis.products.model.dto.Products;
import org.myweb.jobis.products.model.service.ProductsService;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/products")
public class ProductsController {
    private final ProductsService productsService;
    private final ProductsRepository productsRepository;

    @PostMapping("/insert")
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

    @GetMapping("/manage")
    public ResponseEntity<Map<String, Object>> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("prodNumber").descending());
        Page<Products> productsPage = productsService.getProducts(pageable);

        log.debug("총 상품 개수: {}", productsPage.getTotalElements());
        log.debug("현재 페이지 데이터: {}", productsPage.getContent());

        Map<String, Object> response = new HashMap<>();
        response.put("content", productsPage.getContent());
        response.put("totalElements", productsPage.getTotalElements());
        response.put("totalPages", productsPage.getTotalPages());
        response.put("size", productsPage.getSize());
        response.put("number", productsPage.getNumber());

        return ResponseEntity.ok(response);
    }

    // sellable YN controller
    @PatchMapping("/{prodNumber}/sellable")
    public ResponseEntity<Void> updateProdSellable(@PathVariable int prodNumber, @RequestParam String sellable) {
        productsService.updateProdSellable(prodNumber, sellable);
        return ResponseEntity.noContent().build();
    }

    // product update controller
    @PutMapping("/updateProd/{prodNumber}")
    public ResponseEntity<String> updateProduct(
            @PathVariable int prodNumber,
            @RequestBody(required = false) ProductsEntity productEntity) {
        try {
            // 요청 데이터 확인
            if (productEntity == null) {
                log.error("요청 본문이 비어있습니다.");
                return ResponseEntity.badRequest().body("요청 본문이 비어있습니다.");
            }

            // 상품 번호 확인
            if (prodNumber != productEntity.getProdNumber()) {
                log.error("URL의 상품 번호와 본문 데이터가 일치하지 않습니다.");
                return ResponseEntity.badRequest().body("상품 번호가 일치하지 않습니다.");
            }

            // 서비스 호출
            productsService.updateProduct(productEntity);
            log.info("상품 업데이트 성공: {}", prodNumber);

            return ResponseEntity.ok("상품이 성공적으로 업데이트되었습니다!");
        } catch (Exception e) {
            log.error("상품 업데이트 중 오류 발생: ", e);
            return ResponseEntity.status(500).body("상품 업데이트에 실패했습니다.");
        }
    }

    @DeleteMapping("/{prodNumber}")
    public ResponseEntity<String> deleteProduct(@PathVariable int prodNumber) {
        try {
            // 상품 삭제 서비스 호출
            productsService.deleteProduct(prodNumber);
            log.info("상품 삭제 성공: {}", prodNumber);
            return ResponseEntity.ok("상품이 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            log.error("상품 삭제 중 오류 발생: ", e);
            return ResponseEntity.status(500).body("상품 삭제에 실패했습니다.");
        }
    }

//    @PutMapping("/updateProd/{prodNumber}")
//    public ResponseEntity<String> debugUpdateProduct(@RequestBody(required = false) String rawRequestBody) {
//        log.info("Raw Request Body: {}", rawRequestBody);
//        return ResponseEntity.ok("Debug complete");
//    }
}
