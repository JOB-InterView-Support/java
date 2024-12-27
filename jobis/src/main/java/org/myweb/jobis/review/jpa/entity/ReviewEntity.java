package org.myweb.jobis.review.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.myweb.jobis.review.model.dto.Review;
import org.myweb.jobis.user.jpa.entity.UserEntity;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="REVIEW")
@Entity
public class ReviewEntity {
    @Id
    @Column(name = "R_NO", length = 50, nullable = false)
    private String rNo;

    @Column(name = "R_TITLE", length = 90, nullable = false)
    private String rTitle;

    @Column(name = "R_CONTENT", length = 300)
    private String rContent;

    @Column(name = "R_WRITER", length = 50, nullable = false)
    private String rWriter;

    @Column(name = "R_W_DATE", nullable = false)
    private Timestamp rWDate;

    @Column(name = "R_ATTACHMENT_TITLE", length = 90)
    private String rAttachmentTitle;

    @Column(name = "R_A_DATE")
    private Timestamp rADate;

    @Column(name = "R_UPDATE_DATE")
    private Timestamp rUpdateDate;

    @Column(name = "R_IS_DELETED", length = 1, nullable = false)
    private String rIsDeleted;

    @Column(name = "UUID", length = 50, nullable = false)
    private String uuid;

    @Column(name = "R_D_DATE")
    private Timestamp rDDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="UUID", referencedColumnName = "UUID", insertable = false, updatable = false)
    private UserEntity user;

    @Column(name = "R_COUNT", nullable = false, columnDefinition = "NUMBER DEFAULT 1")
    private int rCount = 1;

    public Review toDto() {
        return Review.builder()
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
                .build();
    }

    public static ReviewEntity fromDto(Review review) {
        return ReviewEntity.builder()
                .rNo(review.getRNo())
                .rTitle(review.getRTitle())
                .rContent(review.getRContent())
                .rWriter(review.getRWriter())
                .rWriter(review.getRWriter())
                .rWDate(review.getRWDate())
                .rAttachmentTitle(review.getRAttachmentTitle())
                .rADate(review.getRADate())
                .rUpdateDate(review.getRUpdateDate())
                .rIsDeleted(review.getRIsDeleted())
                .rDDate(review.getRDDate())
                .uuid(review.getUuid())
                .rCount(review.getRCount())
                .build();
    }
}

