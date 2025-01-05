package org.myweb.jobis.notice.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.myweb.jobis.notice.jpa.entity.NoticeAttachmentEntity;

// NoticeAttachment DTO
@JsonInclude(JsonInclude.Include.NON_NULL) // null 값 제외
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeAttachment {
    private String noticeANo; // 첨부파일 번호
    private String noticeAName; // 첨부파일명
    private String noticeNo; // 공지번호 (외래 키)

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createdDate; // 첨부파일 생성일 (필요 시)

    // Entity 변환 메서드
    public NoticeAttachmentEntity toEntity() {
        return NoticeAttachmentEntity.builder()
                .noticeANo(noticeANo)
                .noticeAName(noticeAName)
                .build();
    }

    // Entity -> DTO 변환 메서드
    public static NoticeAttachment fromEntity(NoticeAttachmentEntity noticeattachmententity) {
        return NoticeAttachment.builder()
                .noticeANo(noticeattachmententity.getNoticeANo())
                .noticeAName(noticeattachmententity.getNoticeAName())
                .noticeNo(noticeattachmententity.getNotice() != null ? noticeattachmententity.getNotice().getNoticeNo() : null)
                .build();
    }
}