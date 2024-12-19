package org.myweb.jobis.qna.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.myweb.jobis.qna.model.dto.Qna;
import org.myweb.jobis.user.jpa.entity.UserEntity;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "QNA")
public class QnaEntity {

    @Id
    @Column(name = "Q_NO", length = 50, nullable = false)
    private String qNo;

    @Column(name = "Q_TITLE", length = 90, nullable = false)
    private String qTitle;

    @Column(name = "Q_CONTENT", length = 300)
    private String qContent;

    @Column(name = "Q_WRITER", length = 50, nullable = false)
    private String qWriter;

    @Column(name = "Q_W_DATE", nullable = false)
    private Timestamp qWDate;

    @Column(name = "Q_ATTACHMENT_TITLE", length = 90)
    private String qAttachmentTitle;

    @Column(name = "Q_A_DATE")
    private Timestamp qADate;

    @Column(name = "Q_UPDATE_DATE")
    private Timestamp qUpdateDate;

    @Column(name = "Q_IS_DELETED", length = 1, nullable = false)
    private char qIsDeleted = 'N';

    @Column(name = "Q_D_DATE")
    private Timestamp qDDate;

    @Column(name = "UUID", length = 50, nullable = false)
    private String uuid;

    @Column(name = "Q_ATTACHMENT_YN", length = 1)
    private char qAttachmentYN = 'N';

    @Column(name = "Q_IS_SECRET", length = 1)
    private char qIsSecret = 'N';

    @Column(name = "Q_UPDATE_YN", length = 1)
    private char qUpdateYN = 'N';

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UUID", referencedColumnName = "UUID", insertable = false, updatable = false)
    private UserEntity user;


    // Entity -> DTO 변환 메서드 추가
    public Qna toDto() {
        return Qna.builder()
                .qNo(qNo)
                .qTitle(qTitle)
                .qContent(qContent)
                .qWriter(qWriter)
                .qWDate(qWDate)
                .qAttachmentTitle(qAttachmentTitle)
                .qADate(qADate)
                .qUpdateDate(qUpdateDate)
                .qIsDeleted(qIsDeleted == 'Y')
                .qDDate(qDDate)
                .uuid(uuid)
                .qAttachmentYN(qAttachmentYN == 'Y')
                .qIsSecret(qIsSecret == 'Y')
                .qUpdateYN(qUpdateYN == 'Y')
                .build();
    }

}
