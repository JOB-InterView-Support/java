package org.myweb.jobis.notice.jpa.repository;

import org.myweb.jobis.notice.jpa.entity.NoticeAttachmentEntity;
import org.myweb.jobis.notice.jpa.entity.NoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeAttachmentRepository extends JpaRepository<NoticeAttachmentEntity, String> {
    List<NoticeAttachmentEntity> findByNotice(NoticeEntity notice);
}
