package org.myweb.jobis.qna.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.qna.jpa.entity.QnaEntity;
import org.myweb.jobis.qna.jpa.repository.QnaRepository;
import org.myweb.jobis.qna.model.dto.Qna;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor // final 필드를 기반으로 생성자 자동 생성
public class QnaService {

    private final QnaRepository qnaRepository; // final 필드로 선언하여 @RequiredArgsConstructor가 생성자 생성

    public void insertQna(Qna qnaDTO) {

        // 9시간을 밀리초로 변환
        long nineHoursInMillis = 9 * 60 * 60 * 1000;

        QnaEntity qnaEntity = QnaEntity.builder()
                .qNo(qnaDTO.getQNo() != null ? qnaDTO.getQNo() : "QNA_" + System.currentTimeMillis())
                .qTitle(qnaDTO.getQTitle())
                .qContent(qnaDTO.getQContent())
                .qWriter(qnaDTO.getQWriter())
                .qWDate(qnaDTO.getQWDate() != null
                        ? qnaDTO.getQWDate()
                        : new Timestamp(System.currentTimeMillis() + nineHoursInMillis)) // 작성 시간 + 9시간
                .qAttachmentTitle(qnaDTO.getQAttachmentTitle())
                .qADate(qnaDTO.getQADate() != null
                        ? qnaDTO.getQADate()
                        : (qnaDTO.getQAttachmentTitle() != null
                        ? new Timestamp(System.currentTimeMillis() + nineHoursInMillis) // 첨부 시간 + 9시간
                        : null))
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

    // 검색 기능
    public List<Qna> searchQna(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<QnaEntity> entities = qnaRepository.searchByKeyword(keyword, pageable);
        return entities.stream()
                .map(entity -> new Qna(
                        entity.getQNo(), // 수정: getQNo
                        entity.getQTitle(),
                        entity.getQContent(),
                        entity.getQWriter(),
                        entity.getQWDate() // Timestamp -> LocalDateTime 변환
                ))
                .collect(Collectors.toList());
    }

}


