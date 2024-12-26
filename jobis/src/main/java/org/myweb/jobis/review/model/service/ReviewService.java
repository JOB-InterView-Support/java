package org.myweb.jobis.review.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.review.jpa.entity.ReviewEntity;
import org.myweb.jobis.review.jpa.repository.ReviewRepository;
import org.myweb.jobis.review.model.dto.Review;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.NoSuchElementException;

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
                .rIsDeleted(reviewDTO.getRIsDeleted() != '\0' ? reviewDTO.getRIsDeleted() : 'N') // char 기본값 처리
                .rDDate(reviewDTO.getRDDate())
                .uuid(reviewDTO.getUuid())
                .rCount(reviewDTO.getRCount() != 0 ? reviewDTO.getRCount() : 0)
                .build();

        reviewRepository.save(reviewEntity);
    }

    // 리뷰 상세 조회 메서드
    public Review selectReview(String rNo) {
        try {
            // rNo는 String으로 가정. Repository의 findById와 타입이 맞아야 함.
            ReviewEntity entity = reviewRepository.findById(rNo)
                    .orElseThrow(() -> new NoSuchElementException("Review not found with id: " + rNo));
            log.info("Entity에서 변환된 DTO: {}", entity.toDto());
            return entity.toDto();
        } catch (Exception e) {
            log.error("selectReview 메서드에서 예기치 못한 오류 발생:", e);
            throw e;
        }
    }
}
