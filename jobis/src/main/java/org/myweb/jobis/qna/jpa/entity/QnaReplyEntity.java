package org.myweb.jobis.qna.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.myweb.jobis.qna.model.dto.QnaReply;
import org.myweb.jobis.user.jpa.entity.UserEntity;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "QNA_REPLY", schema = "C##SS")
public class QnaReplyEntity {

    @Id
    @Column(name = "REP_NO", length = 50, nullable = false)
    private String repno;

    @Column(name = "REP_WRITER", length = 50, nullable = false)
    private String repwriter;

    @Column(name = "REP_DATE", nullable = false)
    private Timestamp repdate;

    @Column(name = "REP_IS_DELETED", length = 1, nullable = false)
    private char repisdeleted;

    @Column(name = "REP_UPDATE_DATE")
    private Timestamp repupdatedate;

    @Column(name = "REP_DELETE_DATE")
    private Timestamp repdeletedate;

    @Column(name = "REP_CONTENT", length = 100, nullable = false)
    private String repcontent;

    @Column(name = "UUID", length = 50, nullable = false)
    private String uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UUID", referencedColumnName = "UUID", insertable = false, updatable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Q_NO", referencedColumnName = "Q_NO", insertable = false, updatable = false)
    private QnaEntity qna;

    // Entity -> DTO 변환 메서드
    public QnaReply toDto() {
        return QnaReply.builder()
                .repno(repno)
                .repwriter(repwriter)
                .repdate(repdate)
                .repisdeleted(repisdeleted)
                .repupdatedate(repupdatedate)
                .repdeletedate(repdeletedate)
                .repcontent(repcontent)
                .uuid(uuid)
                .qno(qna != null ? qna.getQNo() : null)
                .build();
    }

    // DTO -> Entity 변환 메서드
    public static QnaReplyEntity fromDto(QnaReply reply, QnaEntity qna) {
        return QnaReplyEntity.builder()
                .repno(reply.getRepno())
                .repwriter(reply.getRepwriter())
                .repdate(reply.getRepdate())
                .repisdeleted(reply.getRepisdeleted())
                .repupdatedate(reply.getRepupdatedate())
                .repdeletedate(reply.getRepdeletedate())
                .repcontent(reply.getRepcontent())
                .uuid(reply.getUuid())
                .qna(qna)
                .build();
    }
}
