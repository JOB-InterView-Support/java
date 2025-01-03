package org.myweb.jobis.review.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.myweb.jobis.review.jpa.entity.ReviewAttachmentEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewAttachment {
    private String rANo;
    private String rATitle;
    private String rExtension;
    private String rNo;

    public ReviewAttachmentEntity toEntity() {
        return ReviewAttachmentEntity.builder()
                .rANo(rANo)
                .rATitle(rATitle)
                .rExtension(rExtension)
                .build();
    }

    public static ReviewAttachment fromEntity(ReviewAttachmentEntity entity) {
        return ReviewAttachment.builder()
                .rANo(entity.getRANo())
                .rATitle(entity.getRATitle())
                .rExtension(entity.getRExtension())
                .rNo(entity.getReview() != null ? entity.getReview().getRNo() : null)
                .build();
    }
}
