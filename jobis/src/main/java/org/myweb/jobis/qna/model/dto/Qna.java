package org.myweb.jobis.qna.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.myweb.jobis.qna.jpa.entity.QnaEntity;

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

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Timestamp qWDate;
    private String qAttachmentTitle;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Timestamp qADate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Timestamp qUpdateDate;

    private String qIsDeleted; // 변경: char → String

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Timestamp qDDate;
    private String uuid;
    private String qAttachmentYN; // 변경: char → String
    private String qIsSecret; // 변경: char → String
    private String qUpdateYN; // 변경: char → String

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
}
