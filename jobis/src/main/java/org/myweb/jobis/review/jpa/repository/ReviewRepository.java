package org.myweb.jobis.review.jpa.repository;

import org.myweb.jobis.review.jpa.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, String>, ReviewRepositoryCustom {

}
