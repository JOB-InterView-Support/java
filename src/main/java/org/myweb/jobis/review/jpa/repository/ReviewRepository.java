package org.myweb.jobis.review.jpa.repository;

import org.myweb.jobis.review.jpa.entity.ReviewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, String>, ReviewRepositoryCustom {

    //Page<ReviewEntity> findByRIsDeleted(String rIsDeleted, Pageable pageable);
    //Optional<ReviewEntity> findByrNo(String rNo); // 필드 이름과 정확히 일치
}


