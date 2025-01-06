package org.myweb.jobis.interview_common_questions.jpa.repository;

import org.myweb.jobis.interview_common_questions.jpa.entity.InterviewCommonQuestionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewCommonQuestionsRepository extends JpaRepository<InterviewCommonQuestionsEntity, String> {
}