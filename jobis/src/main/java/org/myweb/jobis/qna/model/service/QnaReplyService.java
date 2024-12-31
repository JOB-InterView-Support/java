package org.myweb.jobis.qna.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.qna.jpa.entity.QnaEntity;
import org.myweb.jobis.qna.jpa.entity.QnaReplyEntity;
import org.myweb.jobis.qna.jpa.repository.QnaReplyRepository;
import org.myweb.jobis.qna.jpa.repository.QnaRepository;
import org.myweb.jobis.qna.model.dto.QnaReply;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j    //Logger 객체 선언임, 별도의 로그객체 선언 필요없음, 제공되는 레퍼런스는 log 임
@Service
@RequiredArgsConstructor
@Transactional
public class QnaReplyService {
    //QnaReply
    private final QnaRepository qnaRepository;

    private final QnaReplyRepository qnaReplyRepository;

//    // 댓글 저장
//    public QnaReplyEntity saveReply(QnaReplyEntity reply) {
//        return qnaReplyRepository.save(reply);
//    }
//
//    // 댓글 조회
//    public List<QnaReply> findRepliesByQno(String qno) {
//        return qnaReplyRepository.findByQna_QnoAndRepisdeleted(qno, 'N')
//                .stream()
//                .map(QnaReplyEntity::toDto)
//                .collect(Collectors.toList());
//    }
//
//    // 댓글 번호로 조회
//    public QnaReplyEntity findReplyByRepno(String repno) {
//        return qnaReplyRepository.findById(repno)
//                .orElseThrow(() -> new NoSuchElementException("댓글을 찾을 수 없습니다."));
//    }
//
//    // QnA 번호로 QnA 엔티티 조회
//    public QnaEntity findQnaEntityByQno(String qno) {
//        return qnaRepository.findById(qno)
//                .orElseThrow(() -> new NoSuchElementException("게시글을 찾을 수 없습니다."));
//    }


    // 댓글 추가 메서드
    // 댓글 추가 메서드
    @Transactional
    public void addReply(QnaReply replyDto) {
        // QnaEntity를 조회하여 연결
        QnaEntity qna = qnaRepository.findById(replyDto.getQno())
                .orElseThrow(() -> new RuntimeException("QnA not found"));

        log.info("QnaEntity 조회 성공: {}", qna);

        // repno를 qno와 현재 시간(9시간 추가)으로 생성
        LocalDateTime now = LocalDateTime.now().plusHours(9); // 9시간 추가
        String generatedRepno = replyDto.getQno() + "-" + now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        log.info("생성된 댓글 번호: {}", generatedRepno);

        // QnaReplyEntity로 변환하면서 QnaEntity를 매핑
        QnaReplyEntity replyEntity = QnaReplyEntity.builder()
                .repno(generatedRepno) // 동적으로 생성된 repno 설정
                .repwriter(replyDto.getRepwriter())
                .repdate(Timestamp.valueOf(now)) // 현재 시간으로 설정
                .repisdeleted('N') // 기본값 설정
                .repupdatedate(null)
                .repdeletedate(null)
                .repcontent(replyDto.getRepcontent())
                .uuid(replyDto.getUuid())
                .qna(qna) // QnaEntity 연결
                .build();

        log.info("QnaReplyEntity 생성 성공: {}", replyEntity);

        // 데이터 저장
        qnaReplyRepository.save(replyEntity);
        log.info("댓글 저장 완료: {}", replyEntity);
    }

    public void markReplyAsDeleted(String repno) {
        // 댓글 엔티티 조회
        QnaReplyEntity reply = qnaReplyRepository.findById(repno)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다. repno=" + repno));

        // 삭제 상태 업데이트
        reply.setRepisdeleted('Y'); // 삭제 여부 설정
        reply.setRepdeletedate(Timestamp.valueOf(LocalDateTime.now()));

        // 저장
        qnaReplyRepository.save(reply);
        log.info("댓글 삭제 처리 완료: {}", reply);
    }


    // 특정 QnA의 댓글 목록 조회
    public List<QnaReply> findRepliesByQno(String qno) {
        QnaEntity qnaEntity = qnaRepository.findById(qno)
                .orElseThrow(() -> new NoSuchElementException("QnA not found with id: " + qno));

        List<QnaReplyEntity> replyEntities = qnaReplyRepository.findByQna(qnaEntity);

        return replyEntities.stream()
                .map(QnaReplyEntity::toDto) // Entity -> DTO 변환
                .collect(Collectors.toList());
    }


}
