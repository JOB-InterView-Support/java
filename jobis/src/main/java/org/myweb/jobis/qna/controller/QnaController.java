package org.myweb.jobis.qna.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.qna.jpa.entity.QnaEntity;
import org.myweb.jobis.qna.jpa.repository.QnaRepository;
import org.myweb.jobis.qna.model.dto.Qna;
import org.myweb.jobis.qna.model.service.QnaReplyService;
import org.myweb.jobis.qna.model.service.QnaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.util.*;

@Slf4j    //log 객체 선언임, 별도의 로그객체 선언 필요없음, 제공되는 레퍼런스는 log 임
@RequiredArgsConstructor
@RestController
@RequestMapping("qna")
@CrossOrigin            //다른 port 에서 오는 요청을 처리하기 위함
public class QnaController {

    private final QnaService qnaService;
    private final QnaReplyService qnaReplyService;

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

        log.info("요청 받은 페이지: {}", page); // 요청 받은 페이지 로그 출력
        log.info("QnA 데이터: {}", qnaPage.getContent()); // 응답할 데이터 로그 출력

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
                "startPage", Math.max(1, qnaPage.getNumber() + 1 - 2),
                "endPage", Math.min(qnaPage.getTotalPages(), qnaPage.getNumber() + 1 + 3),
                "totalItems", qnaPage.getTotalElements() // 전체 아이템 수 추가
        ));

        log.info("Response: {}", response);
        return response;
    }



    @PostMapping() // 등록
    public ResponseEntity<?> createQna(
            @RequestParam("qTitle") String qTitle, // 제목
            @RequestParam("qContent") String qContent, // 내용
            @RequestParam("qIsSecret") String qIsSecret, // 비밀글 여부
            @RequestParam("qWriter") String qWriter, // 작성자
            @RequestParam("uuid") String uuid,  // UUID 매핑 확인
            @RequestParam(value = "file", required = false) MultipartFile file) { // 첨부 파일 (선택)

        log.info("등록 메서드 시작 : {}", qTitle);

        try {
            log.info("Received qTitle: {}", qTitle);
            log.info("Received qContent: {}", qContent);
            log.info("Received qIsSecret: {}", qIsSecret);
            log.info("Received qWriter: {}", qWriter);
            log.info("Received file: {}", file != null ? file.getOriginalFilename() : "No file");

            String attachmentTitle = null;

            // 파일 처리
            if (file != null && !file.isEmpty()) {
                attachmentTitle = "Q_" + file.getOriginalFilename(); // 파일 이름 저장
                Path uploadPath = Paths.get("C:/upload_files"); // 파일 저장 경로 (디렉터리)

                // 디렉터리 존재 여부 확인 및 생성
                if (!Files.exists(uploadPath)) {
                    try {
                        Files.createDirectories(uploadPath);
                        log.info("업로드 디렉터리 생성: {}", uploadPath.toString());
                    } catch (Exception e) {
                        log.error("업로드 디렉터리 생성 실패", e);
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("업로드 디렉터리 생성 실패");
                    }
                }

                // 파일 저장
                Files.copy(file.getInputStream(), uploadPath.resolve(attachmentTitle), StandardCopyOption.REPLACE_EXISTING);
            }

            // Qna DTO 생성
            Qna qnaDTO = Qna.builder()
                    .qNo(null) // 고유 ID는 서비스에서 생성
                    .qTitle(qTitle)
                    .qContent(qContent)
                    .qWriter(qWriter)
                    .qWDate(new Timestamp(System.currentTimeMillis())) // 작성 시간
                    .qAttachmentTitle(attachmentTitle) // 첨부 파일 제목
                    .qAttachmentYN(file != null ? "Y" : "N") // 첨부 여부
                    .qIsSecret(qIsSecret) // 비밀 여부
                    .qIsDeleted("N") // 기본값
                    .uuid(uuid) // UUID 추가
                    .build();

            if (uuid == null || uuid.isEmpty()) {
                log.error("UUID가 없습니다. 확인 바랍니다.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("UUID가 없습니다.");
            }

            // QnaService 호출
            qnaService.insertQna(qnaDTO);

            return ResponseEntity.status(HttpStatus.CREATED).body("QnA created successfully"); // 성공 응답
        } catch (Exception e) {
            log.error("QnA 등록 중 에러 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error"); // 실패 응답
        }
    }


    @GetMapping("/detail/{qno}")
    public ResponseEntity<Map<String, Object>> qnaDetailMethod(@PathVariable String qno) {
        try {
            log.info("상세페이지 qno: {}", qno);

            Qna qna = qnaService.selectQna(qno);
            log.info("조회된 Qna DTO: {}", qna);

            Map<String, Object> response = new HashMap<>();
            response.put("qna", qna);

            return ResponseEntity.ok(response);
        } catch (NoSuchElementException e) {
            log.error("QnA not found: {}", qno);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "QnA not found for id: " + qno));
        } catch (Exception e) {
            log.error("Unexpected error occurred:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Internal server error"));
        }
    }


    @GetMapping("/attachments/{filename}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        try {
            Path filePath = Paths.get("C:/upload_files").resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("File not found: {}", filename, e);
            return ResponseEntity.internalServerError().build();
        }
    }




}















