package org.myweb.jobis.interview_common_questions.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.myweb.jobis.interview_common_questions.jpa.entity.InterviewCommonQuestionsEntity;

import java.time.LocalDateTime;

/**
 * INTERVIEW_QUESTIONS 테이블의 DTO 클래스
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InterviewCommonQuestions {

    private String queId; // 공통질문 ID
    private String queTitle; // 질문명
    private String queUseStatus; // 사용 여부
    private LocalDateTime queInsertDate; // 등록 날짜
    private LocalDateTime queUpdateDate; // 수정 날짜

    /**
     * DTO -> Entity 변환 메서드
     *
     * @return InterviewCommonQuestionsEntity
     */
    public InterviewCommonQuestionsEntity toEntity() {
        return InterviewCommonQuestionsEntity.builder()
                .queId(this.queId)
                .queTitle(this.queTitle)
                .queUseStatus(this.queUseStatus)
                .queInsertDate(this.queInsertDate)
                .queUpdateDate(this.queUpdateDate)
                .build();
    }
}
