package org.myweb.jobis.qna.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.qna.jpa.entity.QnaEntity;
import org.myweb.jobis.qna.jpa.repository.QnaRepository;
import org.myweb.jobis.qna.model.dto.Qna;
import org.myweb.jobis.qna.model.service.QnaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j    //log 객체 선언임, 별도의 로그객체 선언 필요없음, 제공되는 레퍼런스는 log 임
@RequiredArgsConstructor
@RestController
@RequestMapping("qna")
@CrossOrigin            //다른 port 에서 오는 요청을 처리하기 위함
public class QnaController {

    @Autowired
    private QnaRepository qnaRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    // Q&A 목록 조회 (페이징 포함)
    @GetMapping
    public Map<String, Object> getQnaList(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        // 페이징 처리
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "qWDate"));
        Page<QnaEntity> qnaPage = qnaRepository.findAll(pageable);

        log.info("QCurrent Page: {}", qnaPage.getNumber() + 1);
        log.info("QTotal Pages: {}", qnaPage.getTotalPages());
        log.info("QTotal Items: {}", qnaPage.getTotalElements());
        log.info("QContent: {}", qnaPage.getContent());

        // QnaEntity -> Qna DTO 변환
        List<Qna> qnaList = qnaPage.getContent().stream()
                .map(QnaEntity::toDto) // Entity -> DTO 변환
                .toList();

        // 응답 데이터 구성
        Map<String, Object> response = new HashMap<>();
        response.put("list", qnaList); // DTO 목록으로 변경
        response.put("paging", Map.of(
                "currentPage", qnaPage.getNumber() + 1,
                "maxPage", qnaPage.getTotalPages(),
                "startPage", Math.max(1, qnaPage.getNumber() - 2),
                "endPage", Math.min(qnaPage.getTotalPages(), qnaPage.getNumber() + 3)
        ));

        log.info("Response: {}", response);
        return response;
    }


}
