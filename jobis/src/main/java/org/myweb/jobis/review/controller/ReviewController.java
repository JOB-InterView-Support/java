package org.myweb.jobis.review.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.qna.jpa.entity.QnaEntity;
import org.myweb.jobis.review.jpa.entity.ReviewEntity;
import org.myweb.jobis.review.jpa.repository.ReviewRepository;
import org.myweb.jobis.review.model.dto.Review;
import org.myweb.jobis.review.model.service.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private final ReviewRepository reviewRepository;

    @Value("C:/upload_files")
    private String uploadDir;

    // 목록 조회
    @GetMapping
    public Map<String, Object> getReviewList(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "rWDate"));
        Page<ReviewEntity> reviewPage = reviewRepository.findAll(pageable);
        //Page<ReviewEntity> reviewPage = reviewRepository.findByrIsDeleted("N", pageable);

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
            @RequestParam(value = "file", required = false) MultipartFile file) {

        log.info("리뷰 등록 메서드 시작: {}", rTitle);

        try {
            log.info("Received rTitle: {}", rTitle);
            log.info("Received rContent: {}", rContent);
            log.info("Received rWriter: {}", rWriter);
            log.info("Received file: {}", file != null ? file.getOriginalFilename() : "No file");

            String attachmentTitle = null;
            // 첨부파일 처리
            if (file != null && !file.isEmpty()) {
                attachmentTitle = "R_" + file.getOriginalFilename();
                Path uploadPath = Paths.get("C:/upload_files");

                if (!Files.exists(uploadPath)) {
                    try {
                        Files.createDirectories(uploadPath);
                        log.info("업로드 디렉터리 생성: {}", uploadPath);
                    } catch (Exception e) {
                        log.error("업로드 디렉터리 생성 실패", e);
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("업로드 디렉터리 생성 실패");
                    }
                }
                    Files.copy(file.getInputStream(), uploadPath.resolve(attachmentTitle), StandardCopyOption.REPLACE_EXISTING);
            }
            // Review 객체 생성 (리뷰 등록 데이터)
            Review reviewDTO = Review.builder()
                    .rNo(null) // `System.currentTimeMillis()`가 중복되지 않는지 확인
                    .rTitle(rTitle)
                    .rContent(rContent)
                    .rWriter(rWriter)
                    .rWDate(new Timestamp(System.currentTimeMillis()))

                    .rAttachmentTitle(attachmentTitle)
                    .uuid(uuid)
                    .rIsDeleted("N")
                    .rCount(0)
                    .build();

            log.info("생성된 리뷰 데이터: {}", reviewDTO); // 디버깅 로그 추가


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
        // rno 값 로그 출력
        System.out.println("Received rno: " + rno);
        Logger logger = LoggerFactory.getLogger(ReviewController.class);
        logger.info("Received rno: {}", rno);

        try {
            // 예: 서비스에서 리뷰 상세 데이터 가져오기
            Review review = reviewService.getReviewDetail(rno);
            return ResponseEntity.ok(review);
        } catch (Exception e) {
            logger.error("Error fetching review detail for rno: {}", rno, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch review detail.");
        }
    }





    @GetMapping("/attachments/{filename}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        try {
            Path filePath = Paths.get("C:/upload_files").resolve(filename).normalize();
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
        @PutMapping("/{rno}/delete")
        public ResponseEntity<?> markReviewAsDeleted(@PathVariable String rno) {
            try {
                log.info("삭제 요청 받은 QnA 번호: {}", rno);

                Optional<ReviewEntity> reviewEntityOptional = reviewRepository.findById((rno));
                if (reviewEntityOptional.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Review not found");
                }

                ReviewEntity reviewEntity = reviewEntityOptional.get();
                reviewEntity.setRIsDeleted("Y");

                reviewRepository.save(reviewEntity);

                log.info("QnA가 삭제 처리됨 (qIsDeleted = 'Y'): {}", rno);
                return ResponseEntity.ok("QnA marked as deleted");
            } catch (Exception e) {
                log.error("QnA 삭제 처리 중 에러 발생:", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error marking QnA as deleted");
            }
    }
}
