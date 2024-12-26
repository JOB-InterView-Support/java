package org.myweb.jobis.review.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.myweb.jobis.review.jpa.entity.ReviewEntity;

import org.myweb.jobis.review.model.dto.ReviewAttachment;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "REVIEW_ATTACHMENT", schema = "C##SS")
public class ReviewAttachmentEntity {

    // 첨부파일 번호 (기본 키)
    @Id
    @Column(name = "R_A_NO", length = 50, nullable = false)
    private String rANo;

    // 첨부파일명
    @Column(name = "R_A_TITLE", length = 200, nullable = false)
    private String rATitle;

    // 첨부파일 확장자
    @Column(name = "R_EXTENSION", length = 50, nullable = false)
    private String rExtension;

    // 질문 번호 (외래 키)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "R_NO", referencedColumnName = "R_NO", insertable = false, updatable = false)
    private ReviewEntity review;

    // DTO로 변환 메서드
    public ReviewAttachment toDto() {
        return ReviewAttachment.builder()
                .rANo(rANo)
                .rATitle(rATitle)
                .rExtension(rExtension)
                .rNo(review != null ? review.getRNo() : null)
                .build();
    }

    // DTO -> Entity 변환 메서드
    public static ReviewAttachmentEntity fromDto(ReviewAttachment dto, ReviewEntity review) {
        return ReviewAttachmentEntity.builder()
                .rANo(dto.getRANo())
                .rATitle(dto.getRATitle())
                .rExtension(dto.getRExtension())
                .review(review)
                .build();
    }
}
