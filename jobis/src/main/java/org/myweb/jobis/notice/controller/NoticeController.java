package org.myweb.jobis.notice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.notice.jpa.entity.NoticeAttachmentEntity;
import org.myweb.jobis.notice.jpa.entity.NoticeEntity;
import org.myweb.jobis.notice.jpa.repository.NoticeRepository;
import org.myweb.jobis.notice.model.dto.Notice;
import org.myweb.jobis.notice.model.dto.NoticeAttachment;
import org.myweb.jobis.notice.model.service.NoticeService;
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
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j    //log 객체 선언임, 별도의 로그객체 선언 필요없음, 제공되는 레퍼런스는 log 임
@RequiredArgsConstructor
@RestController
@RequestMapping("/notice")
@CrossOrigin
public class NoticeController {

    private final NoticeService noticeService;
    private final NoticeRepository noticeRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getNoticeList(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("getNoticeList 시작");

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "noticeWDate"));
        Page<NoticeEntity> noticePage = noticeRepository.findByNoticeIsDeleted("N", pageable);

        log.info("Content: {}", noticePage.getContent());
        Map<String, Object> response = new HashMap<>();
        response.put("list", noticePage.getContent()); // 여기서 isDeleted = N 데이터만
        response.put("paging", Map.of(
                "currentPage", noticePage.getNumber() + 1,
                "totalPages", noticePage.getTotalPages(),
                "totalItems", noticePage.getTotalElements()
        ));

        return ResponseEntity.ok(response);
    }

//    @GetMapping("/detail/{noticeNo}")
//    public Notice getNoticeDetail(@PathVariable String noticeNo) {
//        log.info("getNoticeDetail 시작");
//        try {
//            Notice notice = noticeRepository.findById(noticeNo).get().toDto();
//            notice.setNoticeVCount(notice.getNoticeVCount() + 1);
//            noticeRepository.save(notice.toEntity());
//            return notice;
//        } catch (Exception e){
//            e.printStackTrace();
//            return null;
//        }
//    }
//    @GetMapping("/detail/{noticeNo}")
//    public Notice getNoticeDetail(@PathVariable String noticeNo) {
//        log.info("getNoticeDetail 시작");
//        try {
//            Notice notice = noticeRepository.findById(noticeNo).get().toDto();
//
//            // noticePath 중복 방지
//            if (notice.getNoticePath() != null && !notice.getNoticePath().startsWith("http://localhost:8080/attachments/")) {
//                notice.setNoticePath("http://localhost:8080/attachments/" + notice.getNoticePath());
//            }
//
//            // 조회수 증가 처리
//            notice.setNoticeVCount(notice.getNoticeVCount() + 1);
//            noticeRepository.save(notice.toEntity());
//
//            log.info("Processed Notice Path: {}", notice.getNoticePath());
//            return notice;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

//    @GetMapping("/detail/{noticeNo}")
//    public Notice getNoticeDetail(@PathVariable String noticeNo) {
//        log.info("getNoticeDetail 시작");
//        try {
//            Notice notice = noticeRepository.findById(noticeNo).get().toDto();
//
//            // noticePath 중복 방지
//            if (notice.getNoticePath() != null && !notice.getNoticePath().contains("http://localhost:8080/attachments/")) {
//                notice.setNoticePath("http://localhost:8080/attachments/" + notice.getNoticePath());
//            }
//
//            // 조회수 증가 처리
//            notice.setNoticeVCount(notice.getNoticeVCount() + 1);
//            noticeRepository.save(notice.toEntity());
//
//            log.info("Processed Notice Path: {}", notice.getNoticePath());
//            return notice;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    @GetMapping("/detail/{noticeNo}")
    public Notice getNoticeDetail(@PathVariable String noticeNo) {
        log.info("getNoticeDetail 시작");
        try {
            Notice notice = noticeRepository.findById(noticeNo).orElseThrow(() ->
                    new IllegalArgumentException("공지사항을 찾을 수 없습니다.")).toDto();

            // noticePath 중복 방지
            String baseUrl = "http://localhost:8080/notice/attachments/";
            if (notice.getNoticePath() != null && !notice.getNoticePath().startsWith(baseUrl)) {
                notice.setNoticePath(baseUrl + notice.getNoticePath());
            }

            // 조회수 증가 처리
            notice.setNoticeVCount(notice.getNoticeVCount() + 1);
            noticeRepository.save(notice.toEntity());

            log.info("Processed Notice Path: {}", notice.getNoticePath());
            return notice;
        } catch (Exception e) {
            log.error("공지사항 상세 조회 중 오류 발생", e);
            return null;
        }
    }

//    @GetMapping("/attachments/{filename}")
//    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
//        try {
//            Path filePath = Paths.get("C:/upload_files").resolve(filename).normalize();
//            Resource resource = new UrlResource(filePath.toUri());
//            if (resource.exists() && resource.isReadable()) {
//                String contentType = Files.probeContentType(filePath);
//                return ResponseEntity.ok()
//                        .contentType(MediaType.parseMediaType(contentType))
//                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
//                        .body(resource);
//            } else {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }

//    @GetMapping("/attachments/{filename}")
//    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
//        try {
//            Path filePath = Paths.get("C:/upload_files").resolve(filename).normalize();
//            Resource resource = new UrlResource(filePath.toUri());
//
//            if (resource.exists() && resource.isReadable()) {
//                String contentType = Files.probeContentType(filePath);
//                if (contentType == null) {
//                    contentType = "application/octet-stream"; // 기본 MIME 타입
//                }
//
//                return ResponseEntity.ok()
//                        .contentType(MediaType.parseMediaType(contentType))
//                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
//                        .body(resource);
//            } else {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }

//    @GetMapping("/attachments/{filename}")
//    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
//        try {
//            Path filePath = Paths.get("C:/upload_files").resolve(filename).normalize();
//            Resource resource = new UrlResource(filePath.toUri());
//
//            if (!resource.exists() || !resource.isReadable()) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//            }
//
//            String contentType = Files.probeContentType(filePath);
//            if (contentType == null) {
//                contentType = "application/octet-stream";
//            }
//
//            return ResponseEntity.ok()
//                    .contentType(MediaType.parseMediaType(contentType))
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
//                    .body(resource);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }

//    @GetMapping("/attachments/{filename}")
//    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
//        try {
//            Path filePath = Paths.get("C:/upload_files").resolve(filename).normalize();
//            Resource resource = new UrlResource(filePath.toUri());
//
//            if (!resource.exists() || !resource.isReadable()) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//            }
//
//            return ResponseEntity.ok()
//                    .contentType(MediaType.parseMediaType(Files.probeContentType(filePath)))
//                    .body(resource);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }

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


//    @PostMapping("/insert")
//    public ResponseEntity<?> insertNotice(
//            @RequestParam(value = "uuid", required = false) String uuid,
//            @RequestParam(value = "noticeTitle", required = false) String noticeTitle,
//            @RequestParam(value = "noticeContent", required = false) String noticeContent,
//            @RequestParam(value = "noticeWDate", required = false) String noticeWDate,
//            @RequestParam(value = "noticeVCount", defaultValue = "0") int noticeVCount,
//            @RequestParam(value = "file", required = false) MultipartFile file) {
//
//        log.info("getNoticeInsert 시작");
//        try {
//            log.info("noticeTitle: {}", noticeTitle);
//            log.info("noticeContent: {}", noticeContent);
//            log.info("noticeWDate: {}", noticeWDate);
//            log.info("noticeVCount: {}", noticeVCount);
//            log.info("Received file: {}", file != null ? file.getOriginalFilename() : "No file");
//
//            String noticeAttachments = null;
//
//            if (file != null && !file.isEmpty()) {
//                noticeAttachments = "N_" + file.getOriginalFilename(); // 파일 이름 저장
//                Path uploadPath = Paths.get("C:/upload_files"); // 파일 저장 경로 (디렉터리)
//
//                // 디렉터리 존재 여부 확인 및 생성
//                if (!Files.exists(uploadPath)) {
//                    try {
//                        Files.createDirectories(uploadPath);
//                        log.info("업로드 디렉터리 생성: {}", uploadPath.toString());
//                    } catch (Exception e) {
//                        log.error("업로드 디렉터리 생성 실패", e);
//                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("업로드 디렉터리 생성 실패");
//                    }
//                }
//                // 파일 저장
//                //Files.copy(file.getInputStream(), uploadPath.resolve(noticeAttachments), StandardCopyOption.REPLACE_EXISTING);
//            }
//
//            Notice notice = Notice.builder()
//                    .noticeNo(null) //서비스에서 생성
//                    .uuid(uuid)
//                    .noticeTitle(noticeTitle)
//                    .noticeContent(noticeContent)
//                    .noticeWDate(new Timestamp(System.currentTimeMillis()))
//                    .noticeVCount(noticeVCount)
//                    .noticePath(noticeAttachments)
//                    .build();
//
//            noticeService.insertNotice(notice);
//
//            return ResponseEntity.status(HttpStatus.CREATED).body(notice);
//        } catch (Exception e) {
//            log.error("공지 등록 중 에러 발생", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error"); // 실패 응답
//        }
//
//    }

    @PostMapping("/insert")
    public ResponseEntity<?> insertNotice(
            @RequestParam(value = "uuid", required = false) String uuid,
            @RequestParam(value = "noticeTitle", required = true) String noticeTitle,
            @RequestParam(value = "noticeContent", required = true) String noticeContent,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        log.info("공지 등록 시작");

        try {
            String noticeAttachments = null;

            if (file != null && !file.isEmpty()) {
                noticeAttachments = "N_" + file.getOriginalFilename();
                Path uploadPath = Paths.get("C:/upload_files").resolve(noticeAttachments);

                if (!Files.exists(uploadPath.getParent())) {
                    Files.createDirectories(uploadPath.getParent());
                }
                Files.copy(file.getInputStream(), uploadPath, StandardCopyOption.REPLACE_EXISTING);
            }

            Notice notice = Notice.builder()
                    .uuid(uuid)
                    .noticeTitle(noticeTitle)
                    .noticeContent(noticeContent)
                    .noticeWDate(new Timestamp(System.currentTimeMillis()))
                    .noticePath(noticeAttachments)
                    .build();

            noticeService.insertNotice(notice);

            return ResponseEntity.status(HttpStatus.CREATED).body(notice);
        } catch (Exception e) {
            log.error("공지 등록 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("공지 등록 실패");
        }
    }


//    @GetMapping("/update/{noticeNo}")
//    public ResponseEntity<Notice> getNoticeForUpdate(@PathVariable String noticeNo) {
//        try {
//            NoticeEntity noticeEntity = noticeRepository.findById(noticeNo)
//                    .orElseThrow(() -> new IllegalArgumentException("공지사항을 찾을 수 없습니다. (get)"));
//            return ResponseEntity.ok(noticeEntity.toDto());
//        } catch (IllegalArgumentException e) {
//            log.error("공지사항 조회 실패 : {}", e.getMessage());
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        } catch (Exception e) {
//            log.error("공지사항 조회 중 오류 발생 : ", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }

//
//    @PutMapping("/update/{noticeNo}")
//    public ResponseEntity<?> updateNotice(
//            @PathVariable String noticeNo,
//            @RequestParam("noticeTitle") String noticeTitle,
//            @RequestParam("noticeContent") String noticeContent,
//            @RequestParam(value = "file", required = false) MultipartFile file) {
//        log.info("공지 수정 시작: {}", noticeNo);
//
//        try {
//            NoticeEntity noticeEntity = noticeRepository.findById(noticeNo)
//                    .orElseThrow(() -> new IllegalArgumentException("공지사항을 찾을 수 없습니다. (put)"));
//
//            // 제목, 내용 업데이트
//            noticeEntity.setNoticeTitle(noticeTitle);
//            noticeEntity.setNoticeContent(noticeContent);
//            noticeEntity.setNoticeUDate(new Timestamp(System.currentTimeMillis())); // 수정 시간 갱신
//
//            // 파일 업데이트
//            if (file != null && !file.isEmpty()) {
//                String noticeAttachments = "N_" + file.getOriginalFilename();
//                Path uploadPath = Paths.get(uploadDir); // 업로드 경로 사용
//                if (!Files.exists(uploadPath)) {
//                    Files.createDirectories(uploadPath);
//                }
//                Files.copy(file.getInputStream(), uploadPath.resolve(noticeAttachments), StandardCopyOption.REPLACE_EXISTING);
//                noticeEntity.setNoticePath(noticeAttachments); // 파일 경로 저장
//            }
//
//            noticeRepository.save(noticeEntity); // 업데이트된 공지 저장
//            log.info("공지사항 수정 완료: {}", noticeNo);
//            return ResponseEntity.ok("공지사항 수정 성공");
//        } catch (IllegalArgumentException e) {
//            log.error("공지사항 수정 실패: {}", e.getMessage());
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("공지사항을 찾을 수 없습니다.");
//        } catch (Exception e) {
//            log.error("공지사항 수정 중 오류 발생:", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("공지사항 수정 중 오류가 발생했습니다.");
//        }
//    }

    @PutMapping("/update/{noticeNo}")
    public ResponseEntity<?> updateNotice(
            @PathVariable String noticeNo,
            @RequestParam("noticeTitle") String noticeTitle,
            @RequestParam("noticeContent") String noticeContent,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        log.info("공지 수정 시작");

        try {
            NoticeEntity noticeEntity = noticeRepository.findById(noticeNo).orElseThrow(() ->
                    new IllegalArgumentException("공지사항을 찾을 수 없습니다."));

            noticeEntity.setNoticeTitle(noticeTitle);
            noticeEntity.setNoticeContent(noticeContent);
            noticeEntity.setNoticeUDate(new Timestamp(System.currentTimeMillis()));

            if (file != null && !file.isEmpty()) {
                String filePath = "C:/upload_files/N_" + file.getOriginalFilename();
                Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
                noticeEntity.setNoticePath(filePath);
            }

            noticeRepository.save(noticeEntity);
            return ResponseEntity.ok("공지 수정 성공");
        } catch (Exception e) {
            log.error("공지 수정 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("공지 수정 실패");
        }
    }
//
//@PutMapping("/update/{noticeNo}")
//public ResponseEntity<?> updateNotice(
//        @PathVariable String noticeNo,
//        @RequestParam("noticeTitle") String noticeTitle,
//        @RequestParam("noticeContent") String noticeContent,
//        @RequestParam(value = "file", required = false) MultipartFile file) {
//
//    try {
//        NoticeEntity noticeEntity = noticeRepository.findById(noticeNo)
//                .orElseThrow(() -> new IllegalArgumentException("공지사항을 찾을 수 없습니다."));
//
//        // 제목, 내용 업데이트
//        noticeEntity.setNoticeTitle(noticeTitle);
//        noticeEntity.setNoticeContent(noticeContent);
//        noticeEntity.setNoticeUDate(new Timestamp(System.currentTimeMillis()));
//
//        // 파일 업데이트
//        if (file != null && !file.isEmpty()) {
//            String filePath = "uploads/" + file.getOriginalFilename();
//            Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
//
//            // 기존 첨부파일 삭제 및 새 첨부파일 저장
//            noticeEntity.getNoticeAttachments().clear();
//            NoticeAttachmentEntity newAttachment = NoticeAttachmentEntity.builder()
//                    .notice(noticeEntity)
//                    .noticeAName(file.getOriginalFilename())
//                    .build();
//            noticeEntity.getNoticeAttachments().add(newAttachment);
//        }
//
//        noticeRepository.save(noticeEntity);
//        return ResponseEntity.ok("공지사항 수정 성공");
//    } catch (Exception e) {
//        log.error("공지사항 수정 중 오류 발생: ", e);
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("공지사항 수정 실패");
//    }
//}

    @PutMapping("/detail/{noticeNo}")
    public ResponseEntity<String> toggleNoticeDelete(@PathVariable String noticeNo) {
        log.info("toggleNoticeDelete 시작");
        try {
            log.info("삭제 요청 받은 notice 번호: {}", noticeNo);

            Optional<NoticeEntity> noticeEntityOptional = noticeRepository.findById(noticeNo);
            if (noticeEntityOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Notice not found");
            }

            NoticeEntity noticeEntity = noticeEntityOptional.get();

            // 기존 값에 따라 상태 변경 (Y ↔ N)
            String newStatus = "N".equals(noticeEntity.getNoticeIsDeleted()) ? "Y" : "N";
            noticeEntity.setNoticeIsDeleted(newStatus);
            noticeEntity.setNoticeDDate(new Timestamp(System.currentTimeMillis()));
            noticeRepository.save(noticeEntity);

            log.info("Notice 상태 변경 완료: {} -> {}", noticeNo, newStatus);
            return ResponseEntity.ok("Notice 상태가 '" + newStatus + "'로 변경되었습니다.");
        } catch (Exception e) {
            log.error("Notice 상태 변경 중 오류 발생:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error toggling notice status");
        }
    }

}
