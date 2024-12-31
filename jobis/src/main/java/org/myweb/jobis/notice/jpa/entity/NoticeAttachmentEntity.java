package org.myweb.jobis.notice.jpa.entity;

import jakarta.persistence.*;
import lombok.*;
import org.myweb.jobis.notice.model.dto.NoticeAttachment;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "NOTICE_ATTACHMENT")
public class NoticeAttachmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NOTICE_ANO", length = 50, nullable = false)
    private String noticeANo; // 첨부파일번호

    @Column(name = "NOTICE_ANAME", length = 200, nullable = false)
    private String noticeAName; // 첨부파일명

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NOTICE_NO", referencedColumnName = "NOTICE_NO")
    private NoticeEntity notice; // 공지사항과 연관 관계

    // Entity -> DTO 변환 메서드
    public NoticeAttachment toDto() {
        return NoticeAttachment.builder()
                .noticeANo(noticeANo)
                .noticeAName(noticeAName)
                .build();
    }

    // DTO -> Entity 변환 메서드
    public static NoticeAttachmentEntity fromDto(NoticeAttachment noticeattachment, NoticeEntity notice) {
        return NoticeAttachmentEntity.builder()
                .noticeANo(noticeattachment.getNoticeANo())
                .noticeAName(noticeattachment.getNoticeAName())
                .notice(notice)
                .build();
    }
}