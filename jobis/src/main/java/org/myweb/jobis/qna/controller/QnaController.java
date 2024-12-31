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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
import java.sql.Timestamp;
import java.util.*;

@Slf4j    //log 객체 선언임, 별도의 로그객체 선언 필요없음, 제공되는 레퍼런스는 log 임
@RequiredArgsConstructor
@RestController
@RequestMapping("qna")
@CrossOrigin            //다른 port 에서 오는 요청을 처리하기 위함
public class QnaController {

    private final QnaService qnaService;

    @Autowired
    private QnaRepository qnaRepository;


    @Value("${file.upload-dir}")
    private String uploadDir;

    // Q&A 목록 조회 (페이징 포함)
    @GetMapping
    public Map<String, Object> getQnaList(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "qWDate")); // 내림차순 정렬 설정
        Page<QnaEntity> qnaPage = qnaRepository.findByQIsDeleted("N", pageable);

        // 로그로 쿼리 결과를 확인
        log.info("QnA 목록 데이터: {}", qnaPage.getContent());

        List<Qna> qnaList = qnaPage.getContent().stream()
                .map(QnaEntity::toDto)
                .toList();

        Map<String, Object> response = new HashMap<>();
        response.put("list", qnaList);
        response.put("paging", Map.of(
                "currentPage", qnaPage.getNumber() + 1,
                "maxPage", qnaPage.getTotalPages(),
                "startPage", Math.max(1, qnaPage.getNumber() + 1 - 2),
                "endPage", Math.min(qnaPage.getTotalPages(), qnaPage.getNumber() + 1 + 3),
                "totalItems", qnaPage.getTotalElements()
        ));
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
                attachmentTitle = "Q_I_" + file.getOriginalFilename(); // 파일 이름 저장
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
                    .qADate(file != null ? new Timestamp(System.currentTimeMillis()) : null) // 첨부 파일 저장 날짜
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
            // 파일 경로 생성
            Path filePath = Paths.get("C:/upload_files").resolve(filename).normalize();
            log.info("Resolved file path: {}", filePath.toAbsolutePath());

            // 리소스 생성
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                log.info("File exists and is readable: {}", filePath);

                // MIME 타입 확인 및 기본값 설정
                String contentType = Files.probeContentType(filePath);
                if (contentType == null) {
                    if (filename.endsWith(".png")) {
                        contentType = "image/png";
                    } else if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
                        contentType = "image/jpeg";
                    } else if (filename.endsWith(".gif")) {
                        contentType = "image/gif";
                    } else {
                        contentType = "application/octet-stream";
                    }
                }

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .header(HttpHeaders.CACHE_CONTROL, "max-age=3600")
                        .body(resource);
            } else {
                log.warn("File does not exist or is not readable: {}", filePath);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 빈 ResponseEntity 반환
            }
        } catch (NoSuchFileException e) {
            log.error("File not found: {}", filename, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 파일이 없는 경우
        } catch (Exception e) {
            log.error("Unexpected error while providing file: {}", filename, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 기타 예외 발생 시
        }
    }





    @PutMapping("/{qno}/delete")
    public ResponseEntity<?> markQnaAsDeleted(@PathVariable String qno) {
        try {
            log.info("삭제 요청 받은 QnA 번호: {}", qno);

            // 데이터베이스에서 해당 QnA 번호(qno)로 QnA 엔티티 조회
            Optional<QnaEntity> qnaEntityOptional = qnaRepository.findById(qno);

            // QnA 엔티티가 존재하지 않는 경우, 404 Not Found 응답 반환
            if (qnaEntityOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("QnA not found");
            }

            QnaEntity qnaEntity = qnaEntityOptional.get();

            // qIsDeleted 필드를 "Y"로 설정하여 삭제 상태로 변경
            qnaEntity.setQIsDeleted("Y");

            // Q_D_DATE 필드에 현재 시간 설정
            qnaEntity.setQDDate(new Timestamp(System.currentTimeMillis()));

            // 변경된 엔티티를 데이터베이스에 저장
            qnaRepository.save(qnaEntity);

            log.info("QnA가 삭제 처리됨 (qIsDeleted = 'Y', qDDate = {}): {}", qnaEntity.getQDDate(), qno);

            // HTTP 상태 200과 성공 메시지 반환
            return ResponseEntity.ok("QnA marked as deleted");
        } catch (Exception e) {
            // 예외 발생 시 로그를 출력하고 HTTP 500 응답 반환
            log.error("QnA 삭제 처리 중 에러 발생:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error marking QnA as deleted");
        }
    }


    @PutMapping("/update/{qno}")
    public ResponseEntity<?> updateQna(
            @PathVariable String qno,
            @RequestParam("qTitle") String qTitle,
            @RequestParam("qContent") String qContent,
            @RequestParam("qIsSecret") String qIsSecret,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        try {
            log.info("수정 요청 QnA 번호: {}", qno);

            Optional<QnaEntity> qnaEntityOptional = qnaRepository.findById(qno);
            if (qnaEntityOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("QnA not found");
            }

            QnaEntity qnaEntity = qnaEntityOptional.get();

            // 수정 데이터 반영
            qnaEntity.setQTitle(qTitle);
            qnaEntity.setQContent(qContent);
            qnaEntity.setQIsSecret(qIsSecret);

            // 파일 수정 처리
            if (file != null && !file.isEmpty()) {
                String attachmentTitle = "Q_U_" + file.getOriginalFilename();
                Path uploadPath = Paths.get("C:/upload_files");
                Files.copy(file.getInputStream(), uploadPath.resolve(attachmentTitle), StandardCopyOption.REPLACE_EXISTING);

                qnaEntity.setQAttachmentTitle(attachmentTitle);
                qnaEntity.setQAttachmentYN("Y");
                qnaEntity.setQADate(new Timestamp(System.currentTimeMillis())); // 현재 시간 저장
            } else {
                qnaEntity.setQAttachmentTitle(null); // 첨부 파일 제목 제거
                qnaEntity.setQAttachmentYN("N"); // 첨부 파일 여부 N
                qnaEntity.setQADate(null); // 첨부 파일 날짜를 null로 설정
            }

            // 글 수정 여부와 수정 날짜 설정
            qnaEntity.setQUpdateYN("Y"); // 수정 여부를 'Y'로 설정


            qnaEntity.setQUpdateDate(new Timestamp(System.currentTimeMillis())); // 현재 시간 저장

            // 데이터베이스 업데이트
            qnaRepository.save(qnaEntity);
            log.info("QnA가 성공적으로 수정됨: {}", qno);

            return ResponseEntity.ok("QnA updated successfully");
        } catch (Exception e) {
            log.error("QnA 수정 중 에러 발생:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating QnA");
        }
    }




}




































