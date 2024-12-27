package org.myweb.jobis.review.jpa.repository;

import org.myweb.jobis.review.jpa.entity.ReviewEntity;
import org.springframework.data.domain.Pageable;

import java.sql.Date;
import java.util.List;
import java.util.Optional;


public interface ReviewRepositoryCustom {
     String findLastReviewNo();

    long countSearchTitle(String keyword);

    long countSearchWriter(String writer);
     List<ReviewEntity> findSearchTitle(String keyword, Pageable pageable);
     List<ReviewEntity> findSearchContent(String keyword, Pageable pageable);

    //Optional<ReviewEntity> findByrNo(String rno); // 필드 이름과 정확히 일치
   //  List<ReviewEntity> findSearchDate(Date begin, Date end, Pageable pageable);
    // long countSearchDate(Date begin, Date end);
}
