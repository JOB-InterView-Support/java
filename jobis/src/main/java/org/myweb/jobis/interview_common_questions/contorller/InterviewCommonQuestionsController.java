package org.myweb.jobis.interview_common_questions.contorller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.interview_common_questions.jpa.entity.InterviewCommonQuestionsEntity;
import org.myweb.jobis.interview_common_questions.model.dto.InterviewCommonQuestions;
import org.myweb.jobis.interview_common_questions.model.service.InterviewCommonQuestionsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/interviewCommonQuestions")
@RequiredArgsConstructor
@CrossOrigin
public class InterviewCommonQuestionsController {

    private final InterviewCommonQuestionsService interviewCommonQuestionsService;

    @GetMapping("/list")
    public ResponseEntity<List<InterviewCommonQuestions>> getAllQuestions() {
        // Service 호출
        List<InterviewCommonQuestions> questions = interviewCommonQuestionsService.getAllQuestions();

        // 전체 리스트를 로그로 출력
        log.info("Fetched Interview Common Questions: {}", questions);

        return ResponseEntity.ok(questions);
    }


    @PostMapping("/insert")
    public ResponseEntity<String> insertQuestion(@RequestBody Map<String, String> requestBody) {
        String queTitle = requestBody.get("queTitle");

        // 로그 출력
        log.info("Received new question title: {}", queTitle);

        // Service 호출하여 저장
        interviewCommonQuestionsService.saveQuestion(queTitle);

        // 성공 응답 반환
        return ResponseEntity.ok("Question saved successfully");
    }

    @GetMapping("/detail/{queId}")
    public ResponseEntity<InterviewCommonQuestionsEntity> getQuestionDetail(@PathVariable String queId) {
        log.info("Fetching details for queId: {}", queId);
        InterviewCommonQuestionsEntity question = interviewCommonQuestionsService.findByQueId(queId);
        return ResponseEntity.ok(question);
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateQuestion(@RequestBody Map<String, String> requestBody) {
        String queId = requestBody.get("queId");
        String queTitle = requestBody.get("queTitle");
        String queUseStatus = requestBody.get("queUseStatus");

        // 로그 출력
        log.info("Update Request - queId: {}, queTitle: {}, queUseStatus: {}", queId, queTitle, queUseStatus);

        // Service 호출
        interviewCommonQuestionsService.updateQuestion(queId, queTitle, queUseStatus);

        // 성공 응답 반환
        return ResponseEntity.ok("Question updated successfully");
    }


    @DeleteMapping("/delete/{queId}")
    public ResponseEntity<String> deleteQuestion(@PathVariable String queId) {
        // 로그 출력
        log.info("Delete Request - queId: {}", queId);

        // Service 호출
        interviewCommonQuestionsService.deleteQuestion(queId);

        // 성공 응답 반환
        return ResponseEntity.ok("Question deleted successfully");
    }

}
