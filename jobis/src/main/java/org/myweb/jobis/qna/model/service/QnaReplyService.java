package org.myweb.jobis.qna.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.qna.jpa.entity.QnaReplyEntity;
import org.myweb.jobis.qna.jpa.repository.QnaReplyRepository;
import org.myweb.jobis.qna.model.dto.QnaReply;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j    //Logger 객체 선언임, 별도의 로그객체 선언 필요없음, 제공되는 레퍼런스는 log 임
@Service
@RequiredArgsConstructor
@Transactional
public class QnaReplyService {
    //QnaReply

    private final QnaReplyRepository qnaReplyRepository;

    //원글에 대한 댓글, 대댓글 목록 조회
    public ArrayList<QnaReply> selecReplyList(String qno) {
        //추가한 메소드 : 전달받은 원글번호로 board_ref 컬럼의 값이 일치하는 댓글과 대댓글 조회용
        List<QnaReplyEntity> entities = qnaReplyRepository.findAllReply(qno);
        ArrayList<QnaReply> list = new ArrayList<>();
        for(QnaReplyEntity entity : entities){
            list.add(entity.toDto());
        }
        return list;
    }

}
