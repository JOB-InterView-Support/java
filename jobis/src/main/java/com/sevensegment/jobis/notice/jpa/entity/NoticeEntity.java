package com.sevensegment.jobis.notice.jpa.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import com.sevensegment.jobis.notice.model.dto.Notice;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "NOTICE")
public class NoticeEntity {

    @Id
    @Column(name = "NOTICE_NO", length = 50, nullable = false)
    private String noticeNo; // 공지번호

    @Column(name = "UUID", length = 50, nullable = false)
    private String uuid; // UUID

    @Column(name = "NOTICE_TITLE", length = 100, nullable = false)
    private String noticeTitle; // 공지제목

    @Column(name = "NOTICE_CONTENT", length = 4000, nullable = false)
    private String noticeContent; // 공지내용

    @Column(name = "NOTICE_W_DATE", nullable = false)
    private Timestamp noticeWDate; // 작성날짜

    @Column(name = "NOTICE_U_DATE")
    private Timestamp noticeUDate; // 수정날짜

    @Column(name = "NOTICE_D_DATE")
    private Timestamp noticeDDate; // 삭제날짜

    @Column(name = "NOTICE_IS_DELETED", length = 1, nullable = false)
    private String noticeIsDeleted = "N"; // 공지삭제여부

    @Column(name = "NOTICE_V_COUNT", nullable = false)
    private Integer noticeVCount = 0; // 조회수

    @Column(name = "NOTICE_PATH", length = 250, nullable = false)
    private String noticePath; // 첨부파일경로

    @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<NoticeAttachmentEntity> noticeAttachments = new ArrayList<>();

    // 작성 날짜 초기화
    @PrePersist
    public void prePersist() {
        if (noticeWDate == null) {
            noticeWDate = new Timestamp(System.currentTimeMillis()); // 현재 시간을 Timestamp로 설정
        }
        if (noticeIsDeleted == null) {
            noticeIsDeleted = "N"; // 삭제 여부 기본값 설정
        }
        if (noticeVCount == null) {
            noticeVCount = 0; // 조회수 기본값 설정
        }
    }

    // Entity -> DTO 변환 메서드
    public Notice toDto() {
        return Notice.builder()
                .noticeNo(noticeNo)
                .uuid(uuid)
                .noticeTitle(noticeTitle)
                .noticeContent(noticeContent)
                .noticeWDate(noticeWDate)
                .noticeUDate(noticeUDate)
                .noticeDDate(noticeDDate)
                .noticeIsDeleted(noticeIsDeleted)
                .noticeVCount(noticeVCount)
                .noticePath(noticePath) // 파일 이름만 반환
                .build();
    }

    // DTO -> Entity 변환 메서드
    public static NoticeEntity fromDto(Notice notice) {
        return NoticeEntity.builder()
                .noticeNo(notice.getNoticeNo())
                .uuid(notice.getUuid())
                .noticeTitle(notice.getNoticeTitle())
                .noticeContent(notice.getNoticeContent())
                .noticeWDate(notice.getNoticeWDate())
                .noticeUDate(notice.getNoticeUDate())
                .noticeDDate(notice.getNoticeDDate())
                .noticeIsDeleted(notice.getNoticeIsDeleted())
                .noticeVCount(notice.getNoticeVCount())
                .noticePath(notice.getNoticePath())
                .build();
    }
}