package org.myweb.jobis.qna.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.qna.jpa.repository.QnaRepository;
import org.myweb.jobis.qna.model.dto.Qna;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j    //Logger 객체 선언임, 별도의 로그객체 선언 필요없음, 제공되는 레퍼런스는 log 임
@Service
@RequiredArgsConstructor
@Transactional
public class QnaService {

    private QnaRepository qnaRepository;


    public int insertQna(Qna qna){

        qna.setQNo(qnaRepository.findLastQnaNo() + 1);
        log.info("QNAService qna insert : " + qna);
        try {
            qnaRepository.save(qna.toEntity());
            return 1;
        } catch (Exception e) {
            log.error(e.getMessage());
            return 0;
        }
    }

}
