package org.myweb.jobis.qna.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.qna.jpa.entity.QnaEntity;
import org.myweb.jobis.qna.jpa.repository.QnaRepository;
import org.myweb.jobis.qna.model.dto.Qna;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor // final 필드를 기반으로 생성자 자동 생성
public class QnaService {

    private final QnaRepository qnaRepository; // final 필드로 선언하여 @RequiredArgsConstructor가 생성자 생성

    public void insertQna(Qna qnaDTO) {
        QnaEntity qnaEntity = QnaEntity.builder()
                .qNo(qnaDTO.getQNo() != null ? qnaDTO.getQNo() : "QNA_" + System.currentTimeMillis())
                .qTitle(qnaDTO.getQTitle())
                .qContent(qnaDTO.getQContent())
                .qWriter(qnaDTO.getQWriter())
                .qWDate(qnaDTO.getQWDate() != null ? qnaDTO.getQWDate() : new Timestamp(System.currentTimeMillis()))
                .qAttachmentTitle(qnaDTO.getQAttachmentTitle())
                .qADate(qnaDTO.getQADate())
                .qUpdateDate(qnaDTO.getQUpdateDate())
                .qIsDeleted(qnaDTO.getQIsDeleted() != null ? qnaDTO.getQIsDeleted() : "N")
                .qDDate(qnaDTO.getQDDate())
                .uuid(qnaDTO.getUuid())
                .qAttachmentYN(qnaDTO.getQAttachmentYN())
                .qIsSecret(String.valueOf(qnaDTO.getQIsSecret().charAt(0)))
                .qUpdateYN(qnaDTO.getQUpdateYN())
                .build();

        qnaRepository.save(qnaEntity);
    }

    public Qna selectQna(String qno) {
        Optional<QnaEntity> optionalQna = qnaRepository.findById(qno);
        return optionalQna.orElseThrow(() -> new RuntimeException("QnA not found")).toDto();
    }
}


