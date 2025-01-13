package com.sevensegment.jobis.interview_common_questions.jpa.repository;

import com.sevensegment.jobis.interview_common_questions.jpa.entity.InterviewCommonQuestionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewCommonQuestionsRepository extends JpaRepository<InterviewCommonQuestionsEntity, String> {
}