package com.sevensegment.jobis.qna.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.sevensegment.jobis.qna.jpa.entity.QnaEntity;

import java.sql.Timestamp;

// Qna DTO
@JsonInclude(JsonInclude.Include.NON_NULL) // null 값 제외
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Qna {
    private String qNo;
    private String qTitle;
    private String qContent;
    private String qWriter;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp qWDate;
    private String qAttachmentTitle;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp qADate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp qUpdateDate;

    private String qIsDeleted;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp qDDate;
    private String uuid;
    private String qAttachmentYN;
    private String qIsSecret;
    private String qUpdateYN;

    // Entity 변환 메서드
    public QnaEntity toEntity() {
        return QnaEntity.builder()
                .qNo(qNo)
                .qTitle(qTitle)
                .qContent(qContent)
                .qWriter(qWriter)
                .qWDate(qWDate)
                .qAttachmentTitle(qAttachmentTitle)
                .qADate(qADate)
                .qUpdateDate(qUpdateDate)
                .qIsDeleted(qIsDeleted)
                .qDDate(qDDate)
                .uuid(uuid)
                .qAttachmentYN(qAttachmentYN)
                .qIsSecret(qIsSecret)
                .qUpdateYN(qUpdateYN)
                .build();
    }

    public Qna(String qNo, String qTitle, String qContent, String qWriter, Timestamp qWDate) {
        this.qNo = qNo;
        this.qTitle = qTitle;
        this.qContent = qContent;
        this.qWriter = qWriter;
        this.qWDate = qWDate;
    }
}


