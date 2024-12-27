package org.myweb.jobis.review.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.myweb.jobis.review.jpa.entity.ReviewEntity;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Review {
    private String rNo;
    private String rTitle;
    private String rContent;
    private String rWriter;

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
                .build();
    }
}
