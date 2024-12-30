package org.myweb.jobis.notice.jpa.repository;

import org.myweb.jobis.notice.jpa.entity.NoticeEntity;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.util.List;

public interface NoticeRepositoryCustom {
    String findLastNoticeNo();
    List<NoticeEntity> findSearchTitle(String keyword, Pageable pageable);
    String countSearchTitle(String keyword);
    List<NoticeEntity> findSearchContent(String keyword, Pageable pageable);
    String countSearchContent(String keyword);
    List<NoticeEntity> findSearchDate(Timestamp begin, Timestamp end, Pageable pageable); // LocalDateTime으로 수정
    String countSearchDate(Timestamp begin, Timestamp end); // LocalDateTime으로 수정
}