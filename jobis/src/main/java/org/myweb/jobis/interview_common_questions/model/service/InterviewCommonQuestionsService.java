package org.myweb.jobis.interview_common_questions.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.interview_common_questions.jpa.entity.InterviewCommonQuestionsEntity;
import org.myweb.jobis.interview_common_questions.jpa.repository.InterviewCommonQuestionsRepository;
import org.myweb.jobis.interview_common_questions.model.dto.InterviewCommonQuestions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class InterviewCommonQuestionsService {

    private final InterviewCommonQuestionsRepository repository;

    public List<InterviewCommonQuestions> getAllQuestions() {
        List<InterviewCommonQuestionsEntity> entities = repository.findAll();
        return entities.stream()
                .map(entity -> InterviewCommonQuestions.builder()
                        .queId(entity.getQueId())
                        .queTitle(entity.getQueTitle())
                        .queUseStatus(entity.getQueUseStatus())
                        .queInsertDate(entity.getQueInsertDate())
                        .queUpdateDate(entity.getQueUpdateDate())
                        .build())
                .collect(Collectors.toList());
    }

    public void saveQuestion(String queTitle) {
        // queId 생성
        String queId = "CQ" + System.currentTimeMillis();

        // Entity 생성
        InterviewCommonQuestionsEntity question = InterviewCommonQuestionsEntity.builder()
                .queId(queId)
                .queTitle(queTitle)
                .queUseStatus("Y") // 기본값
                .queInsertDate(LocalDateTime.now()) // 현재 시간
                .queUpdateDate(null) // NULL 설정
                .build();

        // DB에 저장
        repository.save(question);
    }

    public InterviewCommonQuestionsEntity findByQueId(String queId) {
        return repository.findById(queId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid queId: " + queId));
    }

    public void updateQuestion(String queId, String queTitle, String queUseStatus) {
        // queId로 데이터 조회
        InterviewCommonQuestionsEntity question = repository.findById(queId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid queId: " + queId));

        // 데이터 업데이트
        question.setQueTitle(queTitle);
        question.setQueUseStatus(queUseStatus);
        question.setQueUpdateDate(LocalDateTime.now()); // 현재 시간으로 설정

        // 변경 사항 저장
        repository.save(question);
    }

    public void deleteQuestion(String queId) {
        // queId로 데이터 존재 여부 확인
        if (!repository.existsById(queId)) {
            throw new IllegalArgumentException("Invalid queId: " + queId);
        }

        // 데이터 삭제
        repository.deleteById(queId);
    }

}
