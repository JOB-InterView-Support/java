package org.myweb.jobis.review.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.review.jpa.entity.ReviewEntity;
import org.myweb.jobis.review.jpa.repository.ReviewRepository;
import org.myweb.jobis.review.model.dto.Review;
import org.myweb.jobis.review.model.service.ReviewService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("review")
@CrossOrigin
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewRepository reviewRepository;

    @Value("C:/upload_files")
    private String uploadDir;

    @GetMapping
    public Map<String, Object> getReviewList(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "rWDate"));
        Page<ReviewEntity> reviewPage = reviewRepository.findAll(pageable);

        log.info("요청 받은 페이지: {}", page);
        log.info("Review 데이터: {}", reviewPage.getContent());

        List<Review> reviewList = reviewPage.getContent().stream()
                .map(ReviewEntity::toDto)
                .toList();

        Map<String, Object> response = new HashMap<>();
        response.put("list", reviewList);
        response.put("paging", Map.of(
                "currentPage", reviewPage.getNumber() + 1,
                "maxPage", reviewPage.getTotalPages(),
                "startPage", Math.max(1, reviewPage.getNumber() + 1 - 2),
                "endPage", Math.min(reviewPage.getTotalPages(), reviewPage.getNumber() + 1 + 3),
                "totalItems", reviewPage.getTotalElements()
        ));

        log.info("Response: {}", response);
        return response;
    }

    @PostMapping
    public ResponseEntity<?> createReview(
            @RequestParam("rTitle") String rTitle,
            @RequestParam("rContent") String rContent,
            @RequestParam("rWriter") String rWriter,
            @RequestParam("uuid") String uuid,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        log.info("리뷰 등록 메서드 시작: {}", rTitle);

        try {
            log.info("Received rTitle: {}", rTitle);
            log.info("Received rContent: {}", rContent);
            log.info("Received rWriter: {}", rWriter);
            log.info("Received file: {}", file != null ? file.getOriginalFilename() : "No file");

            String attachmentTitle = null;

            if (file != null && !file.isEmpty()) {
                attachmentTitle = "R_" + file.getOriginalFilename();
                Path uploadPath = Paths.get(uploadDir);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                    log.info("업로드 디렉터리 생성: {}", uploadPath);
                }

                Files.copy(file.getInputStream(), uploadPath.resolve(attachmentTitle), StandardCopyOption.REPLACE_EXISTING);
            }

            Review reviewDTO = Review.builder()
                    .rNo(null)
                    .rTitle(rTitle)
                    .rContent(rContent)
                    .rWriter(rWriter)
                    .rWDate(new Timestamp(System.currentTimeMillis()))
                    .rAttachmentTitle(attachmentTitle)
                    .uuid(uuid)
                    .rIsDeleted('N')
                    .rCount(0)
                    .build();

            if (uuid == null || uuid.isEmpty()) {
                log.error("UUID가 없습니다. 확인 바랍니다.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("UUID가 없습니다.");
            }

            reviewService.insertReview(reviewDTO);

            return ResponseEntity.status(HttpStatus.CREATED).body("리뷰 등록 성공");
        } catch (Exception e) {
            log.error("리뷰 등록 중 에러 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    @GetMapping("/detail/{rNo}")
    public ResponseEntity<Map<String, Object>> getReviewDetail(@PathVariable String rNo) {
        try {
            log.info("상세 조회 rNo: {}", rNo);

            Review review = reviewService.selectReview(rNo);
            log.info("조회된 Review DTO: {}", review);

            Map<String, Object> response = new HashMap<>();
            response.put("review", review);

            return ResponseEntity.ok(response);
        } catch (NoSuchElementException e) {
            log.error("리뷰를 찾을 수 없습니다: {}", rNo);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "리뷰를 찾을 수 없습니다: " + rNo));
        } catch (Exception e) {
            log.error("예기치 못한 오류 발생:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Internal server error"));
        }
    }

    @GetMapping("/attachments/{filename}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("파일을 찾을 수 없습니다: {}", filename, e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
