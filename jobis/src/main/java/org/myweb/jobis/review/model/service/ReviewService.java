package org.myweb.jobis.review.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.review.jpa.entity.ReviewEntity;
import org.myweb.jobis.review.jpa.repository.ReviewRepository;
import org.myweb.jobis.review.model.dto.Review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    // 리뷰 등록 메서드
    public void insertReview(Review reviewDTO) {
        ReviewEntity reviewEntity = ReviewEntity.builder()
                .rNo(reviewDTO.getRNo() != null ? reviewDTO.getRNo() : "REVIEW_" + System.currentTimeMillis())
                .rTitle(reviewDTO.getRTitle())
                .rContent(reviewDTO.getRContent())
                .rWriter(reviewDTO.getRWriter())
                .rWDate(reviewDTO.getRWDate() != null ? reviewDTO.getRWDate() : new Timestamp(System.currentTimeMillis()))
                .rAttachmentTitle(reviewDTO.getRAttachmentTitle())
                .rADate(reviewDTO.getRADate())
                .rUpdateDate(reviewDTO.getRUpdateDate())
                .rIsDeleted(reviewDTO.getRIsDeleted() != null ? reviewDTO.getRIsDeleted() : "N")
                
                .reviewPath(reviewDTO.getReviewPath() != null ? reviewDTO.getReviewPath() : "")

                .rDDate(reviewDTO.getRDDate())
                .uuid(reviewDTO.getUuid())
                .rCount(Optional.ofNullable(reviewDTO.getRCount()).orElse(0))
                .build();

        reviewRepository.save(reviewEntity);
    }

    // 리뷰 상세 조회 메서드
    public Review selectReview(String rno) {
        try {
            ReviewEntity entity = reviewRepository.findByrNo(rno)
                    .orElseThrow(() -> new NoSuchElementException("리뷰를 찾을 수 없습니다. : " + rno));
            log.info("Entity에서 변환된 DTO: {}", entity.toDto());
            return entity.toDto();
        } catch (Exception e) {
            log.error("selectReview 메서드에서 예기치 못한 오류 발생:", e);
            throw e;
        }
    }
    

    public Review getReviewDetail(String rno) {
        // 리뷰 엔티티 조회
        ReviewEntity reviewEntity = reviewRepository.findById(rno)
                .orElseThrow(() -> new NoSuchElementException("리뷰를 찾을 수 없습니다. rno: " + rno));

        // 엔티티 -> DTO 변환
        return reviewEntity.toDto();
    }

    public int deleteReview(String rno) {
        try {
            reviewRepository.deleteById(rno);
            return 1;
        } catch (Exception e) {
            log.info(e.getMessage());
            return 0;
        }
    }

    public int updateReview(Review review) {
        try {
            reviewRepository.save(review.toEntity());
            return 1;
        } catch(Exception e) {
            log.info(e.getMessage());
            return 0;
        }
    }

    private String saveFile(MultipartFile file) throws IOException {
        String uploadDir = "C:/upload_files";
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
            log.info("업로드 디렉터리 생성: {}", uploadPath);
        }

        String filePath = uploadDir + "/" + file.getOriginalFilename();
        Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
        log.info("파일 저장 완료: {}", filePath);
        return filePath;
    }

    private String generaterno(String rno, int attachmentIndex) {
        String reviewAno = "R_" + rno + "_" + String.format("%02d", attachmentIndex);
        log.info("생성된 review_rno: {}", reviewAno);
        return rno;
    }
}
