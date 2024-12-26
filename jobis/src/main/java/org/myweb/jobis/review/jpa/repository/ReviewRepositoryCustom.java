package org.myweb.jobis.review.jpa.repository;

import org.myweb.jobis.review.jpa.entity.ReviewEntity;
import org.springframework.data.domain.Pageable;

import java.sql.Date;
import java.util.List;


public interface ReviewRepositoryCustom {
     String findLastReviewNo();

    long countSearchTitle(String keyword);
    long countSearchWriter(String keyword);

     List<ReviewEntity> findSearchTitle(String keyword, Pageable pageable);
     List<ReviewEntity> findSearchContent(String keyword, Pageable pageable);


   //  List<ReviewEntity> findSearchDate(Date begin, Date end, Pageable pageable);
    // long countSearchDate(Date begin, Date end);
}
