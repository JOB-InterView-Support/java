package org.myweb.jobis.qna.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.qna.jpa.entity.QnaEntity;
import org.myweb.jobis.qna.jpa.repository.QnaRepository;
import org.myweb.jobis.qna.model.dto.QnaReply;
import org.myweb.jobis.qna.model.service.QnaReplyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/reply")
@RequiredArgsConstructor
public class QnaReplyController {

    private final QnaReplyService qnaReplyService;
    private final QnaRepository qnaRepository;

    @PostMapping("/{qno}")
    public ResponseEntity<?> addReply(
            @PathVariable String qno,
            @RequestBody Map<String, String> replyData) {

        log.info("댓글 등록 요청 수신: qno={}, replyContent={}", qno, replyData.get("replyContent"));

        try {
            // QNA 조회
            Optional<QnaEntity> qnaOptional = qnaRepository.findById(qno);
            if (qnaOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("QnA not found");
            }

            // 댓글 등록
            QnaReply reply = QnaReply.builder()
                    .qno(qno)
                    .repcontent(replyData.get("replyContent"))
                    .uuid("AUTH_UUID_FROM_CONTEXT") // 인증 정보에서 가져오기
                    .repwriter("작성자 이름") // 작성자 이름 설정
                    .build();

            QnaReply savedReply = qnaReplyService.addReply(reply, qnaOptional.get());

            return ResponseEntity.status(HttpStatus.CREATED).body(savedReply);
        } catch (Exception e) {
            log.error("댓글 등록 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 등록 실패");
        }
    }

}

