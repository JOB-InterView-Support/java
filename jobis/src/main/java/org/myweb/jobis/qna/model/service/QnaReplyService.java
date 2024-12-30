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

}
