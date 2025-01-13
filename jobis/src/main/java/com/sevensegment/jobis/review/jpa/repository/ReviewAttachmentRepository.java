package com.sevensegment.jobis.review.jpa.repository;

import com.sevensegment.jobis.review.jpa.entity.ReviewAttachmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewAttachmentRepository extends JpaRepository<ReviewAttachmentEntity, String>{
    //QnaAttachmentRepository
}
