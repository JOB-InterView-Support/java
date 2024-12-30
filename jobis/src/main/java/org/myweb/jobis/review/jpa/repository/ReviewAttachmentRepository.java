package org.myweb.jobis.review.jpa.repository;

import org.myweb.jobis.qna.jpa.entity.QnaAttachmentEntity;
import org.myweb.jobis.review.jpa.entity.ReviewAttachmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewAttachmentRepository extends JpaRepository<ReviewAttachmentEntity, String>{
    //QnaAttachmentRepository
}
