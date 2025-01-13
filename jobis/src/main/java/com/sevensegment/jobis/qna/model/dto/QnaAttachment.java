package com.sevensegment.jobis.qna.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.sevensegment.jobis.qna.jpa.entity.QnaAttachmentEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QnaAttachment {

    private String qANo;           // 첨부파일 번호 (Primary Key)
    private String qATitle;        // 첨부파일명
    private String qExtension;     // 첨부파일 확장자
    private String qNo;            // 질문 번호 (외래 키)

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createdDate;    // 첨부파일 생성일 (필요 시)

    // Entity 변환 메서드
    public QnaAttachmentEntity toEntity() {
        return QnaAttachmentEntity.builder()
                .qANo(qANo)
                .qATitle(qATitle)
                .qExtension(qExtension)
                .build();
    }

    // Entity -> DTO 변환 메서드
    public static QnaAttachment fromEntity(QnaAttachmentEntity entity) {
        return QnaAttachment.builder()
                .qANo(entity.getQANo())
                .qATitle(entity.getQATitle())
                .qExtension(entity.getQExtension())
                .qNo(entity.getQna() != null ? entity.getQna().getQNo() : null)
                .build();
    }
}
