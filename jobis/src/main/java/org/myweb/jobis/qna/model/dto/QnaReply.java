package org.myweb.jobis.qna.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.myweb.jobis.qna.jpa.entity.QnaReplyEntity;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QnaReply {

    private String repno;           // 답변 번호 (Primary Key)
    private String repwriter;       // 작성자명
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp repdate;      // 답변 작성 날짜와 시간
    private char repisdeleted;      // 답변 삭제 여부 (Y/N)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp repupdatedate; // 답변 수정 날짜와 시간
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp repdeletedate; // 답변 삭제 날짜와 시간
    private String repcontent;      // 답변 내용
    private String uuid;            // 작성자 UUID
    private String qno;             // 질문 번호 (외래 키)

    // Entity 변환 메서드
    public QnaReplyEntity toEntity() {
        return QnaReplyEntity.builder()
                .repno(repno)
                .repwriter(repwriter)
                .repdate(repdate)
                .repisdeleted(repisdeleted)
                .repupdatedate(repupdatedate)
                .repdeletedate(repdeletedate)
                .repcontent(repcontent)
                .uuid(uuid)
                .build();
    }

    // Entity -> DTO 변환 메서드
    public static QnaReply fromEntity(QnaReplyEntity entity) {
        return QnaReply.builder()
                .repno(entity.getRepno())
                .repwriter(entity.getRepwriter())
                .repdate(entity.getRepdate())
                .repisdeleted(entity.getRepisdeleted())
                .repupdatedate(entity.getRepupdatedate())
                .repdeletedate(entity.getRepdeletedate())
                .repcontent(entity.getRepcontent())
                .uuid(entity.getUuid())
                .qno(entity.getQna() != null ? entity.getQna().getQNo() : null)
                .build();
    }
}
