package com.sevensegment.jobis.notice.jpa.repository;

import com.sevensegment.jobis.notice.jpa.entity.NoticeAttachmentEntity;
import com.sevensegment.jobis.notice.jpa.entity.NoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeAttachmentRepository extends JpaRepository<NoticeAttachmentEntity, String> {
    List<NoticeAttachmentEntity> findByNotice(NoticeEntity notice);
}
