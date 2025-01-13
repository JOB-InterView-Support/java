package com.sevensegment.jobis.notice.jpa.repository;

import com.sevensegment.jobis.notice.jpa.entity.NoticeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface NoticeRepositoryCustom {
    //
    Optional<NoticeEntity> findByNotice(String noticeNo);
    Page<NoticeEntity> findByNoticeIsDeleted(String noticeIsDeleted, Pageable pageable);
    String findLastNoticeNo();
    List<NoticeEntity> findSearchTitle(String keyword, Pageable pageable);
    String countSearchTitle(String keyword);
    List<NoticeEntity> findSearchContent(String keyword, Pageable pageable);
    String countSearchContent(String keyword);
    List<NoticeEntity> findSearchDate(Timestamp begin, Timestamp end, Pageable pageable); // LocalDateTime으로 수정
    String countSearchDate(Timestamp begin, Timestamp end); // LocalDateTime으로 수정
}