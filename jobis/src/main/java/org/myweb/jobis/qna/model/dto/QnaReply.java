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

    private String repNo;           // 답변 번호 (Primary Key)
    private String repWriter;       // 작성자명
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp repDate;      // 답변 작성 날짜와 시간
    private char repIsDeleted;      // 답변 삭제 여부 (Y/N)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp repUpdateDate; // 답변 수정 날짜와 시간
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp repDeleteDate; // 답변 삭제 날짜와 시간
    private String uuid;            // 작성자 UUID
    private String qNo;             // 질문 번호 (외래 키)

    // Entity 변환 메서드
    public QnaReplyEntity toEntity() {
        return QnaReplyEntity.builder()
                .repNo(repNo)
                .repWriter(repWriter)
                .repDate(repDate)
                .repIsDeleted(repIsDeleted)
                .repUpdateDate(repUpdateDate)
                .repDeleteDate(repDeleteDate)
                .uuid(uuid)
                .build();
    }

    // Entity -> DTO 변환 메서드
    public static QnaReply fromEntity(QnaReplyEntity entity) {
        return QnaReply.builder()
                .repNo(entity.getRepNo())
                .repWriter(entity.getRepWriter())
                .repDate(entity.getRepDate())
                .repIsDeleted(entity.getRepIsDeleted())
                .repUpdateDate(entity.getRepUpdateDate())
                .repDeleteDate(entity.getRepDeleteDate())
                .uuid(entity.getUuid())
                .qNo(entity.getQna() != null ? entity.getQna().getQNo() : null)
                .build();
    }
}
