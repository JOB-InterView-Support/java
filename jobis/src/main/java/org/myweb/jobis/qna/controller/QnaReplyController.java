package org.myweb.jobis.qna.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.qna.jpa.repository.QnaRepository;
import org.myweb.jobis.qna.model.dto.QnaReply;
import org.myweb.jobis.qna.model.service.QnaReplyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequestMapping("/reply")
@RequiredArgsConstructor
public class QnaReplyController {

    private final QnaReplyService qnaReplyService;

    @PostMapping("/{qno}")
    public ResponseEntity<?> addReply(
            @PathVariable String qno,
            @RequestBody Map<String, String> replyData) {

        log.info("댓글 등록 요청 데이터: {}", replyData);

        if (replyData == null || replyData.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("요청 본문이 누락되었습니다.");
        }

        try {
            // 유효성 검증
            if (!replyData.containsKey("replyContent") || !replyData.containsKey("uuid") || !replyData.containsKey("replyWriter")) {
                log.error("요청 데이터 누락: {}", replyData);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("필수 데이터가 누락되었습니다.");
            }

            QnaReply reply = QnaReply.builder()
                    .qno(qno)
                    .repcontent(replyData.get("replyContent"))
                    .uuid(replyData.get("uuid"))
                    .repwriter(replyData.get("replyWriter"))
                    .build();

            qnaReplyService.addReply(reply);

            return ResponseEntity.status(HttpStatus.CREATED).body("댓글이 성공적으로 등록되었습니다.");
        } catch (Exception e) {
            log.error("댓글 등록 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 등록 실패");
        }
    }



    @PutMapping("/{repno}/delete")
    public ResponseEntity<?> markReplyAsDeleted(@PathVariable String repno) {
        log.info("댓글 삭제 요청 수신: repno={}", repno); // 요청 로그 추가

        try {
            qnaReplyService.markReplyAsDeleted(repno);
            log.info("댓글 삭제 성공: repno={}", repno); // 성공 로그 추가
            return ResponseEntity.ok("댓글 삭제 성공");
        } catch (NoSuchElementException e) {
            log.error("댓글 삭제 실패 - 댓글을 찾을 수 없음: repno={}", repno);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("댓글을 찾을 수 없습니다.");
        } catch (Exception e) {
            log.error("댓글 삭제 실패 - 내부 서버 오류: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 삭제 실패");
        }
    }


}

