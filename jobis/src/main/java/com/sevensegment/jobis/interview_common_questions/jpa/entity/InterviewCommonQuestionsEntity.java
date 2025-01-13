package com.sevensegment.jobis.interview_common_questions.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.sevensegment.jobis.interview_common_questions.model.dto.InterviewCommonQuestions;

import java.time.LocalDateTime;

/**
 * INTERVIEW_QUESTIONS 테이블의 Entity 클래스
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "INTERVIEW_QUESTIONS")
@Entity
public class InterviewCommonQuestionsEntity {

    @Id
    @Column(name = "QUE_ID", length = 50, nullable = false)
    private String queId; // 공통질문 ID

    @Column(name = "QUE_TITLE", length = 512, nullable = false)
    private String queTitle; // 질문명

    @Builder.Default
    @Column(name = "QUE_USE_STATUS", length = 1, nullable = false)
    private String queUseStatus = "Y"; // 사용 여부

    @Column(name = "QUE_INSERT_DATE", nullable = false)
    private LocalDateTime queInsertDate; // 등록 날짜

    @Column(name = "QUE_UPDATE_DATE")
    private LocalDateTime queUpdateDate; // 수정 날짜

    @PrePersist
    protected void onCreate() {
        this.queInsertDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.queUpdateDate = LocalDateTime.now();
    }

    /**
     * Entity -> DTO 변환 메서드
     */
    public InterviewCommonQuestions toDto() {
        return InterviewCommonQuestions.builder()
                .queId(this.queId)
                .queTitle(this.queTitle)
                .queUseStatus(this.queUseStatus)
                .queInsertDate(this.queInsertDate)
                .queUpdateDate(this.queUpdateDate)
                .build();
    }
}
