package com.sevensegment.jobis.review.jpa.repository;

import com.sevensegment.jobis.review.jpa.entity.ReviewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


public interface ReviewRepositoryCustom {
    String findLastReviewNo();
    long countSearchTitle(String keyword);
    long countSearchWriter(String writer);
    List<ReviewEntity> findSearchTitle(String keyword, Pageable pageable);
    List<ReviewEntity> findSearchContent(String keyword, Pageable pageable);
    Page<ReviewEntity> findByRIsDeleted(String rIsDeleted, Pageable pageable);
    Optional<ReviewEntity> findByrNo(String rno); // 필드 이름과 정확히 일치
    //Optional<ReviewEntity> findByrNo(String rno); // 필드 이름과 정확히 일치
   //  List<ReviewEntity> findSearchDate(Date begin, Date end, Pageable pageable);
    // long countSearchDate(Date begin, Date end);
}
