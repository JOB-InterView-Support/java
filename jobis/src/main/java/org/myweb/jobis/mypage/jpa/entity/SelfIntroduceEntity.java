package org.myweb.jobis.mypage.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.myweb.jobis.mypage.model.dto.SelfIntroduce;

import java.time.LocalDateTime;

/**
 * SELF_INTRODUCE 테이블의 Entity 클래스
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "SELF_INTRODUCE")
@Entity
public class SelfIntroduceEntity {

    @Id
    @Column(name = "INTRO_NO", length = 50, nullable = false, updatable = false)
    private String introNo; // 자기소개서 번호

    @Column(name = "UUID", length = 50, nullable = false)
    private String uuid; // 사용자 UUID

    @Column(name = "INTRO_TITLE", length = 100)
    private String introTitle; // 자기소개서 제목

    @Column(name = "INTRO_CONTENTS", length = 2400)
    private String introContents; // 자기소개서 내용

    @Column(name = "INTRO_DATE")
    private LocalDateTime introDate; // 자기소개서 작성일자

    @Column(name = "APPLICATION_COMPANY_NAME", length = 255)
    private String applicationCompanyName; // 지원 회사명

    @Column(name = "WORK_TYPE", length = 255)
    private String workType; // 지원 직무

    @Column(name = "CERTIFICATE", length = 255)
    private String certificate; // 보유 자격증

    @Column(name = "INTRO_IS_DELETED", length = 1, nullable = false)
    private String introIsDeleted = "N"; // 자기소개서 삭제 여부 (기본값 'N')

    @Column(name = "INTRO_DELETED_DATE")
    private LocalDateTime introDeletedDate; // 자기소개서 삭제 일자


    @PrePersist
    private void generateIntroNo() {
        if (this.introNo == null || this.introNo.isEmpty()) {
            this.introNo = "INTRO_"+ uuid + System.currentTimeMillis();
        }
    }

    /**
     * Entity -> DTO 변환 메서드
     */
    public SelfIntroduce toDto() {
        return SelfIntroduce.builder()
                .introNo(this.introNo)
                .uuid(this.uuid)
                .introTitle(this.introTitle)
                .introContents(this.introContents)
                .introDate(this.introDate)
                .applicationCompanyName(this.applicationCompanyName)
                .workType(this.workType)
                .certificate(this.certificate)
                .introIsDeleted(this.introIsDeleted)
                .introDeletedDate(this.introDeletedDate)
                .build();
    }
}
