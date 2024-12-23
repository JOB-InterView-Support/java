package org.myweb.jobis.qna.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.myweb.jobis.qna.jpa.entity.QnaEntity;

import java.sql.Timestamp;

// Qna DTO
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

    private char qIsDeleted;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Timestamp qDDate;
    private String uuid;
    private char qAttachmentYN;
    private char qIsSecret;
    private char qUpdateYN;

    //entity 변환
    public QnaEntity toEntity(){
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
