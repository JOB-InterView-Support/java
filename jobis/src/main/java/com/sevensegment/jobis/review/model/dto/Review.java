package com.sevensegment.jobis.review.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.sevensegment.jobis.review.jpa.entity.ReviewEntity;

import java.sql.Timestamp;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Review {
    private String rNo;
    private String rTitle;
    private String rContent;
    private String rWriter;
    private String reviewPath;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Timestamp rWDate;
    private String rAttachmentTitle;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Timestamp rADate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Timestamp rUpdateDate;

    private String rIsDeleted;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Timestamp rDDate;
    private String uuid;
    private int rCount;

    public ReviewEntity toEntity() {
        return ReviewEntity.builder()
                .rNo(rNo)
                .rTitle(rTitle)
                .rContent(rContent)
                .rWriter(rWriter)
                .rWDate(rWDate)
                .rAttachmentTitle(rAttachmentTitle)
                .rADate(rADate)
                .rUpdateDate(rUpdateDate)
                .rIsDeleted(rIsDeleted)
                .rDDate(rDDate)
                .uuid(uuid)
                .rCount(rCount)
                .reviewPath(reviewPath)
                .build();
    }

    public static Review fromEntity(ReviewEntity entity) {
        return Review.builder()
                .rNo(entity.getRNo())
                .uuid(entity.getUuid())
                .rTitle(entity.getRTitle())
                .rContent(entity.getRContent())
                .rWriter(entity.getRWriter())
                .rWDate(entity.getRWDate())
                .rAttachmentTitle(entity.getRAttachmentTitle())
                .rADate(entity.getRADate())
                .rUpdateDate(entity.getRUpdateDate())
                .rIsDeleted(entity.getRIsDeleted())
                .rDDate(entity.getRDDate())
                .rCount(entity.getRCount())
                .reviewPath(entity.getReviewPath())
                .build();
    }
}
