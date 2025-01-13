package com.sevensegment.jobis.review.jpa.repository;

import com.sevensegment.jobis.review.jpa.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, String>, ReviewRepositoryCustom {

    //Page<ReviewEntity> findByRIsDeleted(String rIsDeleted, Pageable pageable);
    //Optional<ReviewEntity> findByrNo(String rNo); // 필드 이름과 정확히 일치
}


