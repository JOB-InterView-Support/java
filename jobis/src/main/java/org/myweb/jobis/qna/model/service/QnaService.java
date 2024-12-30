package org.myweb.jobis.qna.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.qna.jpa.entity.QnaEntity;
import org.myweb.jobis.qna.jpa.repository.QnaRepository;
import org.myweb.jobis.qna.model.dto.Qna;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.NoSuchElementException;
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
                .qADate(qnaDTO.getQADate() != null ? qnaDTO.getQADate() : (qnaDTO.getQAttachmentTitle() != null ? new Timestamp(System.currentTimeMillis()) : null))
                .qUpdateDate(qnaDTO.getQUpdateDate())
                .qIsDeleted(qnaDTO.getQIsDeleted() != null ? qnaDTO.getQIsDeleted() : "N")
                .qDDate(qnaDTO.getQDDate())
                .uuid(qnaDTO.getUuid())
                .qAttachmentYN(qnaDTO.getQAttachmentYN() != null ? qnaDTO.getQAttachmentYN() : "N")
                .qIsSecret(String.valueOf(qnaDTO.getQIsSecret().charAt(0)))
                .qUpdateYN(qnaDTO.getQUpdateYN() != null ? qnaDTO.getQUpdateYN() : "N")
                .build();

        qnaRepository.save(qnaEntity);
    }


    public Qna selectQna(String qno) {
        try {
            // QnA 엔티티를 데이터베이스에서 조회
            QnaEntity entity = qnaRepository.findByQno(qno)
                    .orElseThrow(() -> new NoSuchElementException("QnA not found with id: " + qno));

            // 로그를 통해 첨부파일 제목 확인
            log.info("첨부파일 제목 확인: {}", entity.getQAttachmentTitle());

            // 엔티티를 DTO로 변환 후 반환
            Qna dto = entity.toDto();
            log.info("Entity에서 변환된 DTO: {}", dto); // DTO 로그 출력
            return dto;
        } catch (Exception e) {
            log.error("selectQna 메서드에서 예기치 못한 오류 발생:", e);
            throw e; // 예외 재던짐
        }
    }


//    public QnaEntity findQnaEntityByQno(String qno) {
//    }
}


