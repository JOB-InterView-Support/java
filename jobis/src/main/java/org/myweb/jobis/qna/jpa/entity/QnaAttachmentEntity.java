package org.myweb.jobis.qna.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.myweb.jobis.qna.model.dto.QnaAttachment;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "QNA_ATTACHMENT", schema = "C##SS")
public class QnaAttachmentEntity {

    // 첨부파일 번호 (기본 키)
    @Id
    @Column(name = "Q_A_NO", length = 50, nullable = false)
    private String qANo;

    // 첨부파일명
    @Column(name = "Q_A_TITLE", length = 200, nullable = false)
    private String qATitle;

    // 첨부파일 확장자
    @Column(name = "Q_EXTENSION", length = 50, nullable = false)
    private String qExtension;

    // 질문 번호 (외래 키)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Q_NO", referencedColumnName = "Q_NO", insertable = false, updatable = false)
    private QnaEntity qna;

    // DTO로 변환 메서드
    public QnaAttachment toDto() {
        return QnaAttachment.builder()
                .qANo(qANo)
                .qATitle(qATitle)
                .qExtension(qExtension)
                .qNo(qna != null ? qna.getQNo() : null)
                .build();
    }

    // DTO -> Entity 변환 메서드
    public static QnaAttachmentEntity fromDto(QnaAttachment dto, QnaEntity qna) {
        return QnaAttachmentEntity.builder()
                .qANo(dto.getQANo())
                .qATitle(dto.getQATitle())
                .qExtension(dto.getQExtension())
                .qna(qna)
                .build();
    }
}
