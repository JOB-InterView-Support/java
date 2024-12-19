package org.myweb.jobis.qna.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class qna {
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
    private boolean qIsDeleted;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Timestamp qDDate;
    private String uuid;
    private boolean qAttachmentYN;
    private boolean qIsSecret;
    private boolean qUpdateYN;


}
