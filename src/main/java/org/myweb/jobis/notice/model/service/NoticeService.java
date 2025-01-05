package org.myweb.jobis.notice.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.notice.jpa.entity.NoticeEntity;
import org.myweb.jobis.notice.jpa.repository.NoticeAttachmentRepository;
import org.myweb.jobis.notice.jpa.repository.NoticeRepository;
import org.myweb.jobis.notice.jpa.repository.NoticeRepositoryCustom;
import org.myweb.jobis.notice.model.dto.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final NoticeAttachmentRepository noticeAttachmentRepository;

    public Page<Notice> getPageNotices(Pageable pageable) {
        log.info("페이징 요청: {}", pageable);
        Page<NoticeEntity> notices = noticeRepository.findAll(pageable);
        log.info("페이징 결과: Content: {}, Total Pages: {}, Total Elements: {}",
                notices.getContent(), notices.getTotalPages(), notices.getTotalElements());

        return notices.map(NoticeEntity::toDto);
    }

    public void insertNotice(Notice notice) {
        log.info("공지 등록 요청: {}", notice);

        NoticeEntity noticeEntity = NoticeEntity.builder()
                .noticeNo(notice.getNoticeNo() != null ? notice.getNoticeNo() : "NOTICE_" + System.currentTimeMillis())
                .uuid(notice.getUuid())
                .noticeTitle(notice.getNoticeTitle())
                .noticeContent(notice.getNoticeContent())
                .noticeWDate(notice.getNoticeWDate() != null ? notice.getNoticeWDate() : new Timestamp(System.currentTimeMillis()))
                .noticeUDate(notice.getNoticeUDate())
                .noticeDDate(notice.getNoticeDDate())
                .noticeIsDeleted(notice.getNoticeIsDeleted() != null ? notice.getNoticeIsDeleted() : "N")
                .noticeVCount(Optional.ofNullable(notice.getNoticeVCount()).orElse(0))
                .noticePath(notice.getNoticePath() != null ? notice.getNoticePath() : "")
                .build();

        log.info("생성된 NoticeEntity: {}", noticeEntity);

        noticeRepository.save(noticeEntity);
        log.info("공지 등록 완료: {}", noticeEntity);
    }

//    public void insertNoticeWithFile(Notice notice, MultipartFile file) {
//        log.info("공지 등록 요청: {}, 첨부파일: {}", notice, file != null ? file.getOriginalFilename() : "없음");
//
//        NoticeEntity noticeEntity = NoticeEntity.builder()
//                .noticeNo("NOTICE_" + System.currentTimeMillis())
//                .uuid(notice.getUuid())
//                .noticeTitle(notice.getNoticeTitle())
//                .noticeContent(notice.getNoticeContent())
//                .noticeWDate(new Timestamp(System.currentTimeMillis()))
//                .noticeVCount(0)
//                .noticeIsDeleted("N")
//                .build();
//
//        log.info("생성된 NoticeEntity: {}", noticeEntity);
//
//        if (file != null && !file.isEmpty()) {
//            try {
//                String filePath = saveFile(file);
//                String noticeAno = generateNoticeAno(noticeEntity.getNoticeNo(), 1);
//
//                NoticeAttachmentEntity attachmentEntity = NoticeAttachmentEntity.builder()
//                        .noticeAno(noticeAno)
//                        .notice(noticeEntity)
//                        .noticeAName(file.getOriginalFilename())
//                        .build();
//
//                noticeEntity.getNoticeAttachments().add(attachmentEntity);
//                log.info("첨부파일 저장 및 연관 설정 완료: {}", attachmentEntity);
//
//            } catch (IOException e) {
//                log.error("파일 저장 중 오류 발생", e);
//                throw new RuntimeException("파일 저장 중 오류 발생", e);
//            }
//        }
//
//        noticeRepository.save(noticeEntity);
//        log.info("공지 등록 완료: {}", noticeEntity);
//    }

    private String saveFile(MultipartFile file) throws IOException {
        String uploadDir = "C:/upload_files";
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
            log.info("업로드 디렉터리 생성: {}", uploadPath);
        }

        String filePath = uploadDir + "/" + file.getOriginalFilename();
        Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
        log.info("파일 저장 완료: {}", filePath);
        return filePath;
    }

    private String generateNoticeAno(String noticeNo, int attachmentIndex) {
        String noticeAno = "N_" + noticeNo + "_" + String.format("%02d", attachmentIndex);
        log.info("생성된 NOTICE_ANO: {}", noticeAno);
        return noticeAno;
    }
}
