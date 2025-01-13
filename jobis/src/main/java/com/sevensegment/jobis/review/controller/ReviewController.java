package com.sevensegment.jobis.review.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import com.sevensegment.jobis.review.jpa.entity.ReviewEntity;
import com.sevensegment.jobis.review.jpa.repository.ReviewRepository;
import com.sevensegment.jobis.review.model.dto.Review;
import com.sevensegment.jobis.review.model.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    @Autowired
    private ReviewRepository reviewRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    // 목록 조회
    @GetMapping
    public Map<String, Object> getReviewList(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "rWDate"));
        //Page<ReviewEntity> reviewPage = reviewRepository.findAll(pageable);
        Page<ReviewEntity> reviewPage = reviewRepository.findByRIsDeleted("N", pageable);

        //log.info("요청 받은 페이지: {}", page);
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

        //log.info("Response: {}", response);
        return response;
    }

    @PostMapping
    public ResponseEntity<?> createReview(
            @RequestParam("rTitle") String rTitle,
            @RequestParam("rContent") String rContent,
            @RequestParam("rWriter") String rWriter,
            @RequestParam("uuid") String uuid,
            @RequestParam(value = "rCount", defaultValue = "0") int rCount,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        try {
            String attachmentTitle = null;
            String fileUrl = null;

            if (file != null && !file.isEmpty()) {
                attachmentTitle = "R_" + file.getOriginalFilename();
                Path uploadPath = Paths.get(uploadDir).resolve(attachmentTitle);

                // 디렉토리 생성 확인
                if (!Files.exists(uploadPath.getParent())) {
                    Files.createDirectories(uploadPath.getParent());
                }

                // 파일 저장
                Files.copy(file.getInputStream(), uploadPath, StandardCopyOption.REPLACE_EXISTING);

                // URL 생성
                fileUrl = "http://localhost:8080/review/attachments/" + attachmentTitle;
            }

            // DTO 생성 및 서비스 호출
            Review reviewDTO = Review.builder()
                    .rTitle(rTitle)
                    .rContent(rContent)
                    .rWriter(rWriter)
                    .uuid(uuid)
                    .rAttachmentTitle(attachmentTitle)
                    .reviewPath(fileUrl) // URL 저장
                    .rCount(rCount)
                    .rWDate(new Timestamp(System.currentTimeMillis()))
                    .rIsDeleted("N")
                    .build();

            log.info("생성된 리뷰 데이터: {}", reviewDTO);

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

    @GetMapping("/detail/{rno}")
    public ResponseEntity<?> getReviewDetail(@PathVariable("rno") String rno) {
        try {
            Review review = reviewService.getReviewDetail(rno);

            // 리뷰 조회수 증가
            review.setRCount(review.getRCount() + 1);
            reviewRepository.save(review.toEntity());

            // URL이 포함된 파일 경로 확인
            String baseUrl = "http://localhost:8080/review/attachments/";
            if (review.getReviewPath() != null && !review.getReviewPath().startsWith(baseUrl)) {
                review.setReviewPath(baseUrl + review.getReviewPath());
            }

            return ResponseEntity.ok(review);
        } catch (Exception e) {
            log.error("리뷰 상세 조회 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("리뷰 조회 실패");
        }
    }



//    @GetMapping("/detail/{rno}")
//    public Review getReviewDetail(@PathVariable("rno") String rno) {
//            //public ResponseEntity<?> getReviewDetail(@PathVariable("rno") String rno) {
//        // rno 값 로그 출력
//        System.out.println("Received rno: " + rno);
//        Logger logger = LoggerFactory.getLogger(ReviewController.class);
//        logger.info("Received rno: {}", rno);
//
//        try {
//            // 예: 서비스에서 리뷰 상세 데이터 가져오기
//            Review review = reviewService.getReviewDetail(rno);
//            review.setRCount(review.getRCount() + 1);
//            reviewRepository.save(review.toEntity());
//            //return ResponseEntity.ok(review);
//
//            // reviewPath 중복 방지
//            String baseUrl = "http://localhost:8080/review/attachments/";
//            if (review.getReviewPath() != null && !review.getReviewPath().startsWith(baseUrl)) {
//                review.setReviewPath(baseUrl + review.getReviewPath());
//            }
//            return review;
//        } catch (Exception e) {
//            log.error("공지사항 상세 조회 중 오류 발생", e);
//            return null;
////            logger.error("Error fetching review detail for rno: {}", rno, e);
////            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
////                    .body("Failed to fetch review detail.");
//        }
//    }


    @GetMapping("/attachments/{filename}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        try {
            // 파일 경로 생성
            Path filePath = Paths.get("C:/upload_files").resolve(filename).normalize();
            // 리소스 생성
            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists() && !resource.isReadable()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            // MIME 타입 자동 감지
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream"; // 기본 MIME 타입 설정
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{rno}/delete")
    public ResponseEntity<?> markReviewAsDeleted(@PathVariable String rno) {
        try {
            log.info("삭제 요청 받은 리뷰 번호: {}", rno);

            Optional<ReviewEntity> reviewEntityOptional = reviewRepository.findById(rno);
            if (reviewEntityOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Review not found");
            }

            ReviewEntity reviewEntity = reviewEntityOptional.get();
            reviewEntity.setRIsDeleted("Y"); // rIsDeleted 필드를 "Y"로 변경
            reviewRepository.save(reviewEntity);

            log.info("리뷰가 삭제 처리됨 (rIsDeleted = 'Y'): {}", rno);
            return ResponseEntity.ok("Review marked as deleted");
        } catch (Exception e) {
            log.error("리뷰 삭제 처리 중 에러 발생:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error marking review as deleted");
        }
    }

    @PutMapping("/update/{rno}")
    public ResponseEntity<?> updateReview(
            @PathVariable String rno,
            @RequestParam("rTitle") String rTitle,
            @RequestParam("rContent") String rContent,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        try {
            log.info("Received rno: {}", rno);

            Optional<ReviewEntity> reviewEntityOptional = reviewRepository.findById(rno);
            if (reviewEntityOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Review not found");
            }

            ReviewEntity reviewEntity = reviewEntityOptional.get();
            reviewEntity.setRTitle(rTitle);
            reviewEntity.setRContent(rContent);

            if (file != null && !file.isEmpty()) {
                String attachmentTitle = "R_" + file.getOriginalFilename();
                Path uploadPath = Paths.get("C:/upload_files");
                Files.copy(file.getInputStream(), uploadPath.resolve(attachmentTitle), StandardCopyOption.REPLACE_EXISTING);

                reviewEntity.setRAttachmentTitle(attachmentTitle);
                reviewEntity.setReviewPath(attachmentTitle); // 경로 업데이트
                log.info("Review Entity before save: {}", reviewEntity);

                reviewEntity.setRADate(new Timestamp(System.currentTimeMillis()));
            } else {
                reviewEntity.setRAttachmentTitle(null);
                reviewEntity.setRADate(null);


            }

            reviewEntity.setRUpdateDate(new Timestamp(System.currentTimeMillis()));
            // db 업데이트
            reviewRepository.save(reviewEntity);
            log.info("Review updated successfully: {}", rno);

            return ResponseEntity.ok("Review updated successfully");
        } catch (Exception e) {
            log.error("Error updating review", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating review");
        }
    }


}

