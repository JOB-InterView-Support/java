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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

//    @GetMapping
//    public ResponseEntity<Map<String, Object>> getNoticeList(
//            @RequestParam(value = "page", defaultValue = "1") int page,
//            @RequestParam(value = "size", defaultValue = "10") int size) {
//        log.info("getNoticeList 시작");
//
//        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "noticeWDate"));
//
//        // 서비스 호출
//        Page<NoticeEntity> noticePage = noticeRepository.findByNoticeIsDeleted("N", pageable);
//
//        log.info("QCurrent Page: {}", noticePage.getNumber() + 1);
//        log.info("QTotal Pages: {}", noticePage.getTotalPages());
//        log.info("QTotal Items: {}", noticePage.getTotalElements());
//        log.info("QContent: {}", noticePage.getContent());
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("list", noticePage.getContent());
//        response.put("paging", Map.of(
//                "currentPage", noticePage.getNumber() + 1,
//                "maxPage", noticePage.getTotalPages(),
//                "totalItems", noticePage.getTotalElements(),
//                "startPage", Math.max(1, noticePage.getNumber() + 1 - 2),
//                "endPage", Math.min(noticePage.getTotalPages(), noticePage.getNumber() + 1 + 3)
//        ));
//
//        log.info("Response: {}", response);
//        return ResponseEntity.ok(response);
//    }

//    @GetMapping
//    public ResponseEntity<Map<String, Object>> getNoticeList(
//            @RequestParam(value = "page", defaultValue = "1") int page,
//            @RequestParam(value = "size", defaultValue = "10") int size) {
//        log.info("getNoticeList 시작");
//
//        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "noticeWDate"));
//
//        // isDeleted = "N" 조건 추가하여 데이터 필터링
//        Page<NoticeEntity> noticePage = noticeRepository.findByNoticeIsDeleted("N", pageable);
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("list", noticePage.getContent()); // 필터링된 공지 데이터
//        response.put("paging", Map.of(
//                "currentPage", noticePage.getNumber() + 1,
//                "maxPage", noticePage.getTotalPages(),
//                "totalItems", noticePage.getTotalElements(),
//                "startPage", Math.max(1, noticePage.getNumber() + 1 - 2),
//                "endPage", Math.min(noticePage.getTotalPages(), noticePage.getNumber() + 1 + 3)
//        ));
//
//        return ResponseEntity.ok(response);
//    }

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
@GetMapping("/detail/{noticeNo}")
public Notice getNoticeDetail(@PathVariable String noticeNo) {
    log.info("getNoticeDetail 시작");
    try {
        Notice notice = noticeRepository.findById(noticeNo).get().toDto();
        notice.setNoticeVCount(notice.getNoticeVCount() + 1);
        noticeRepository.save(notice.toEntity());
        return notice;
    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}

    //SDA
//
    @PostMapping("/insert")
    public ResponseEntity<?> insertNotice(
            @RequestParam(value = "uuid", required = false) String uuid,
            @RequestParam(value = "noticeTitle", required = false) String noticeTitle,
            @RequestParam(value = "noticeContent", required = false) String noticeContent,
            @RequestParam(value = "noticeWDate", required = false) String noticeWDate,
            @RequestParam(value = "noticeVCount", defaultValue = "0") int noticeVCount,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        log.info("getNoticeInsert 시작");
        try {
            log.info("noticeTitle: {}", noticeTitle);
            log.info("noticeContent: {}", noticeContent);
            log.info("noticeWDate: {}", noticeWDate);
            log.info("noticeVCount: {}", noticeVCount);
            log.info("Received file: {}", file != null ? file.getOriginalFilename() : "No file");

            String noticeAttachments = null;

            if (file != null && !file.isEmpty()) {
                noticeAttachments = "N_" + file.getOriginalFilename(); // 파일 이름 저장
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
                //Files.copy(file.getInputStream(), uploadPath.resolve(noticeAttachments), StandardCopyOption.REPLACE_EXISTING);
            }

            Notice notice = Notice.builder()
                    .noticeNo(null) //서비스에서 생성
                    .uuid(uuid)
                    .noticeTitle(noticeTitle)
                    .noticeContent(noticeContent)
                    .noticeWDate(new Timestamp(System.currentTimeMillis()))
                    .noticeVCount(noticeVCount)
                    .noticePath(noticeAttachments)
                    .build();

            noticeService.insertNotice(notice);

            return ResponseEntity.status(HttpStatus.CREATED).body(notice);
        } catch (Exception e) {
            log.error("공지 등록 중 에러 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error"); // 실패 응답
        }

    }
//    @PostMapping("/insert")
//    public ResponseEntity<?> insertNotice(
//            @RequestParam("uuid") String uuid,
//            @RequestParam("noticeTitle") String noticeTitle,
//            @RequestParam("noticeContent") String noticeContent,
//            @RequestParam(value = "file", required = false) MultipartFile file) {
//
//        log.info("공지 등록 시작");
//        log.info("UUID: {}", uuid);
//        log.info("공지 제목: {}", noticeTitle);
//        log.info("공지 내용: {}", noticeContent);
//        log.info("첨부 파일: {}", file != null ? file.getOriginalFilename() : "첨부 파일 없음");
//
//        try {
//            // Notice DTO 생성
//            Notice notice = Notice.builder()
//                    .uuid(uuid)
//                    .noticeTitle(noticeTitle)
//                    .noticeContent(noticeContent)
//                    .build();
//
//            // 서비스 호출
//            noticeService.insertNotice(notice, file);
//
//            return ResponseEntity.status(HttpStatus.CREATED).body("공지 등록 성공");
//        } catch (Exception e) {
//            log.error("공지 등록 중 에러 발생", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("공지 등록 실패");
//        }
//    }

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
//    @PostMapping("/insert")
//    public ResponseEntity<?> insertNotice(
//            @RequestParam("uuid") String uuid,
//            @RequestParam("noticeTitle") String noticeTitle,
//            @RequestParam("noticeContent") String noticeContent,
//            @RequestParam(value = "file", required = false) MultipartFile file) {
//
//        log.info("공지 등록 시작");
//        log.info("UUID: {}", uuid);
//        log.info("공지 제목: {}", noticeTitle);
//        log.info("공지 내용: {}", noticeContent);
//        log.info("첨부 파일: {}", file != null ? file.getOriginalFilename() : "첨부 파일 없음");
//
//        try {
//            // Notice DTO 생성
//            Notice notice = Notice.builder()
//                    .uuid(uuid)
//                    .noticeTitle(noticeTitle)
//                    .noticeContent(noticeContent)
//                    .build();
//
//            // 서비스 호출
//            noticeService.insertNotice(notice, file);
//
//            return ResponseEntity.status(HttpStatus.CREATED).body("공지 등록 성공");
//        } catch (Exception e) {
//            log.error("공지 등록 중 에러 발생", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("공지 등록 실패");
//        }
//    }
//

    @GetMapping("/update/{noticeNo}")
    public ResponseEntity<Notice> getNoticeForUpdate(@PathVariable String noticeNo) {
        try {
            NoticeEntity noticeEntity = noticeRepository.findById(noticeNo)
                    .orElseThrow(() -> new IllegalArgumentException("Notice not found"));
            Notice notice = noticeEntity.toDto();
            // Attachments 추가
            List<NoticeAttachment> attachments = noticeEntity.getNoticeAttachments().stream()
                    .map(NoticeAttachmentEntity::toDto)
                    .collect(Collectors.toList());
            notice.setAttachments(attachments);
            return ResponseEntity.ok(notice);
        } catch (IllegalArgumentException e) {
            log.error("공지사항 조회 실패 : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            log.error("공지사항 조회 중 오류 발생 : ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

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
//                    .orElseThrow(() -> new IllegalArgumentException("공지사항을 찾을 수 없습니다."));
//
//            // 제목 및 내용 업데이트
//            noticeEntity.setNoticeTitle(noticeTitle);
//            noticeEntity.setNoticeContent(noticeContent);
//            noticeEntity.setNoticeUDate(new Timestamp(System.currentTimeMillis())); // 수정 시간 갱신
//
//            // 파일 업데이트 처리
//            if (file != null && !file.isEmpty()) {
//                String filePath = "N_" + file.getOriginalFilename();
//                Path uploadPath = Paths.get(uploadDir);
//                if (!Files.exists(uploadPath)) {
//                    Files.createDirectories(uploadPath);
//                }
//
//                // 파일 저장
//                Files.copy(file.getInputStream(), uploadPath.resolve(filePath), StandardCopyOption.REPLACE_EXISTING);
//
//                // 기존 NoticeAttachment 삭제 및 새로운 파일 정보 저장
//                noticeService.updateNoticeAttachment(noticeEntity, filePath);
//            }
//
//            // 업데이트된 공지사항 저장
//            noticeRepository.save(noticeEntity);
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

    try {
        NoticeEntity noticeEntity = noticeRepository.findById(noticeNo)
                .orElseThrow(() -> new IllegalArgumentException("공지사항을 찾을 수 없습니다."));

        // 제목, 내용 업데이트
        noticeEntity.setNoticeTitle(noticeTitle);
        noticeEntity.setNoticeContent(noticeContent);
        noticeEntity.setNoticeUDate(new Timestamp(System.currentTimeMillis()));

        // 파일 업데이트
        if (file != null && !file.isEmpty()) {
            String filePath = "uploads/" + file.getOriginalFilename();
            Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

            // 기존 첨부파일 삭제 및 새 첨부파일 저장
            noticeEntity.getNoticeAttachments().clear();
            NoticeAttachmentEntity newAttachment = NoticeAttachmentEntity.builder()
                    .notice(noticeEntity)
                    .noticeAName(file.getOriginalFilename())
                    .build();
            noticeEntity.getNoticeAttachments().add(newAttachment);
        }

        noticeRepository.save(noticeEntity);
        return ResponseEntity.ok("공지사항 수정 성공");
    } catch (Exception e) {
        log.error("공지사항 수정 중 오류 발생: ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("공지사항 수정 실패");
    }
}


//    @PutMapping("/detail/{noticeNo}")
//    public ResponseEntity<String> getNoticeDelete(@PathVariable String noticeNo) {
//        log.info("getNoticeDelete 시작");
//        try {
//            log.info("삭제 요청 받은 notice 번호: {}", noticeNo);
//
//            Optional<NoticeEntity> notcieEntityOptional = noticeRepository.findById((noticeNo));
//            if (notcieEntityOptional.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("notice not found");
//            }
//
//            NoticeEntity noticeEntity = notcieEntityOptional.get();
//            noticeEntity.setNoticeIsDeleted("Y"); // qIsDeleted 필드를 "Y"로 변경
//            noticeRepository.save(noticeEntity);
//
//            log.info("notice 삭제 처리됨 (qIsDeleted = 'Y'): {}", noticeNo);
//            return ResponseEntity.ok("notice marked as deleted");
//        } catch (Exception e) {
//            log.error("notice 삭제 처리 중 에러 발생:", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error marking notice as deleted");
//        }
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

            noticeRepository.save(noticeEntity);

            log.info("Notice 상태 변경 완료: {} -> {}", noticeNo, newStatus);
            return ResponseEntity.ok("Notice 상태가 '" + newStatus + "'로 변경되었습니다.");
        } catch (Exception e) {
            log.error("Notice 상태 변경 중 오류 발생:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error toggling notice status");
        }
    }

}
