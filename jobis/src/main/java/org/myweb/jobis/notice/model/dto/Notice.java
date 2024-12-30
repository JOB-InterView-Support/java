package org.myweb.jobis.notice.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.myweb.jobis.notice.jpa.entity.NoticeEntity;

import java.sql.Timestamp;
import java.util.List;

// Notice DTO
@JsonInclude(JsonInclude.Include.NON_NULL) // null 값 제외
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notice {
        private String noticeNo; // 공지번호
        private String uuid; // UUID
        private String noticeTitle; // 공지제목
        private String noticeContent; // 공지내용
        private int noticeVCount; // 조회수
        private String noticePath; // 첨부파일경로

        @JsonFormat(pattern = "yyyy-MM-dd")
        private Timestamp noticeWDate; // 작성날짜

        @JsonFormat(pattern = "yyyy-MM-dd")
        private Timestamp noticeUDate; // 수정날짜

        private String noticeIsDeleted; // 공지삭제여부

        @JsonFormat(pattern = "yyyy-MM-dd")
        private Timestamp noticeDDate; // 삭제날짜

        private List<NoticeAttachment> noticeAttachments; // 첨부파일 리스트

        // Entity 변환 메서드
        public NoticeEntity toEntity() {
                return NoticeEntity.builder()
                        .noticeNo(noticeNo)
                        .uuid(uuid)
                        .noticeTitle(noticeTitle)
                        .noticeContent(noticeContent)
                        .noticeVCount(noticeVCount)
                        .noticeWDate(noticeWDate)
                        .noticeUDate(noticeUDate)
                        .noticeIsDeleted(noticeIsDeleted)
                        .noticeDDate(noticeDDate)
                        .noticePath(noticePath == null ? "" : noticePath)
                        .build();
        }

        // Entity -> DTO 변환 메서드
        public static Notice fromEntity(NoticeEntity entity) {
                return Notice.builder()
                        .noticeNo(entity.getNoticeNo())
                        .uuid(entity.getUuid())
                        .noticeTitle(entity.getNoticeTitle())
                        .noticeContent(entity.getNoticeContent())
                        .noticeVCount(entity.getNoticeVCount())
                        .noticePath(entity.getNoticePath())
                        .noticeWDate(entity.getNoticeWDate())
                        .noticeUDate(entity.getNoticeUDate())
                        .noticeIsDeleted(entity.getNoticeIsDeleted())
                        .noticeDDate(entity.getNoticeDDate())
                        .build();
        }

        public void setAttachments(List<NoticeAttachment> attachments) {
        }
}