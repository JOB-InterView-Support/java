package org.myweb.jobis.notice.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.notice.jpa.entity.NoticeAttachmentEntity;
import org.myweb.jobis.notice.jpa.entity.NoticeEntity;
import org.myweb.jobis.notice.jpa.repository.NoticeAttachmentRepository;
import org.myweb.jobis.notice.jpa.repository.NoticeRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class NoticeService {
    private final NoticeRepository noticeRepository;

    public Page<Notice> getPageNotices(Pageable pageable) {
        Page<NoticeEntity> notices = noticeRepository.findAll(pageable);
        log.info("Notices 페이징 데이터 확인 - Content: {}, Total Pages: {}, Total Elements: {}",
                notices.getContent(), notices.getTotalPages(), notices.getTotalElements());

        return noticeRepository.findAll(pageable).map(NoticeEntity::toDto);
        }
/*        Page<Notice> notice = notices.map(NoticeEntity::toDto);
        log.info("notice 페이징 데이터 확인");*/

/*    private ArrayList<Notice> toList(List<NoticeEntity> entityList) {
        //컨트롤러로 리턴할 ArrayList<Notice> 타입으로 변경 처리 필요함
        ArrayList<Notice> list = new ArrayList<>();
        //Page 안의 NoticeEntity 를 Notice 로 변환해서 리스트에 추가 처리함
        for(NoticeEntity entity : entityList){
            list.add(entity.toDto());
        }
        return list;
    }*/

    public void insertNotice(Notice notice) {
        NoticeEntity noticeEntity = NoticeEntity.builder()
                .noticeNo(notice.getNoticeNo() != null ? notice.getNoticeNo() : "NOTICE_" + System.currentTimeMillis())
                .uuid(notice.getUuid())
                .noticeTitle(notice.getNoticeTitle())
                .noticeContent(notice.getNoticeContent())
                .noticeWDate(notice.getNoticeWDate() != null ? notice.getNoticeWDate() : new Timestamp(System.currentTimeMillis()))
                .noticeUDate(notice.getNoticeUDate()) // 수정날짜는 nullable
                .noticeDDate(notice.getNoticeDDate()) // 삭제날짜는 nullable
                .noticeIsDeleted(notice.getNoticeIsDeleted() != null ? notice.getNoticeIsDeleted() : "N") // 삭제 여부 기본값 "N"
                .noticeVCount(Optional.ofNullable(notice.getNoticeVCount()).orElse(0))// 조회수 기본값 0
                .noticePath(notice.getNoticePath() != null ? notice.getNoticePath() : "") // 첨부파일 경로 기본값 ""
                .build();

        noticeRepository.save(noticeEntity);
        log.info("공지사항 등록 완료: {}", noticeEntity);
    }

//    @Transactional
//    public void insertNotice(Notice notice, MultipartFile file) {
//        // NoticeEntity 생성
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
//        // 첨부파일 처리
//        if (file != null && !file.isEmpty()) {
//            try {
//                // 파일 저장
//                String filePath = saveFile(file);
//
//                // NoticeAttachmentEntity 생성
//                NoticeAttachmentEntity attachmentEntity = NoticeAttachmentEntity.builder()
//                        .notice(noticeEntity) // 부모 NoticeEntity 설정
//                        .noticeAName(file.getOriginalFilename())
//                        .build();
//
//                // NoticeEntity에 첨부파일 추가
//                noticeEntity.getNoticeAttachments().add(attachmentEntity);
//            } catch (IOException e) {
//                throw new RuntimeException("파일 저장 중 오류 발생", e);
//            }
//        }
//
//        // NoticeEntity 저장
//        noticeRepository.save(noticeEntity);
//    }

//    @Transactional
//    public void insertNotice(Notice notice, MultipartFile file) {
//        // NoticeEntity 생성
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
//        // 첨부파일 처리
//        if (file != null && !file.isEmpty()) {
//            try {
//                // 파일 저장
//                String filePath = saveFile(file);
//
//                // NoticeAttachmentEntity 생성 및 NoticeEntity에 추가
//                NoticeAttachmentEntity attachmentEntity = NoticeAttachmentEntity.builder()
//                        .notice(noticeEntity)
//                        .noticeAName(file.getOriginalFilename())
//                        .build();
//
//                noticeEntity.getNoticeAttachments().add(attachmentEntity);
//            } catch (IOException e) {
//                throw new RuntimeException("파일 저장 중 오류 발생", e);
//            }
//        }
//
//        // NoticeEntity 저장
//        noticeRepository.save(noticeEntity);
//    }


//    private String saveFile(MultipartFile file) throws IOException {
//        String filePath = "C:/upload_files/" + file.getOriginalFilename(); // 파일 저장 경로
//        Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
//        return filePath;
//    }

//    private String saveFile(MultipartFile file) throws IOException {
//        String uploadDir = "C:/upload_files";
//        Path uploadPath = Paths.get(uploadDir);
//
//        if (!Files.exists(uploadPath)) {
//            Files.createDirectories(uploadPath); // 디렉토리 생성
//        }
//
//        String filePath = uploadDir + "/" + file.getOriginalFilename();
//        Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
//        return filePath;
//    }
//
    public void saveNotice(NoticeEntity noticeEntity) {
        noticeRepository.save(noticeEntity);
        log.info("공지사항과 첨부파일 저장 완료: {}", noticeEntity);
    }

      private final NoticeAttachmentRepository noticeAttachmentRepository; // 추가
//
//    @Transactional
//    public void updateNoticeAttachment(NoticeEntity noticeEntity, String filePath) {
//        // 기존 NoticeAttachment 삭제
//        List<NoticeAttachmentEntity> attachments = noticeEntity.getNoticeAttachments();
//        if (attachments != null && !attachments.isEmpty()) {
//            for (NoticeAttachmentEntity attachment : attachments) {
//                noticeAttachmentRepository.delete(attachment); // 기존 첨부파일 삭제
//            }
//        }
//
//        // 새로운 NoticeAttachment 저장
//        NoticeAttachmentEntity newAttachment = NoticeAttachmentEntity.builder()
//                .notice(noticeEntity)
//                .noticeAName(filePath)
//                .build();
//        noticeAttachmentRepository.save(newAttachment); // 새 첨부파일 저장
//    }
//@Transactional
//public void updateNotice(NoticeEntity noticeEntity, MultipartFile file) {
//    if (file != null && !file.isEmpty()) {
//        try {
//            // 기존 첨부파일 삭제
//            List<NoticeAttachmentEntity> attachments = noticeEntity.getNoticeAttachments();
//            if (attachments != null && !attachments.isEmpty()) {
//                attachments.forEach(attachment -> noticeAttachmentRepository.delete(attachment));
//            }
//
//            // 새 첨부파일 저장
//            String filePath = saveFile(file);
//            NoticeAttachmentEntity newAttachment = NoticeAttachmentEntity.builder()
//                    .notice(noticeEntity)
//                    .noticeAName(file.getOriginalFilename())
//                    .build();
//
//            noticeEntity.getNoticeAttachments().add(newAttachment);
//        } catch (IOException e) {
//            throw new RuntimeException("파일 저장 중 오류 발생", e);
//        }
//    }

    // NoticeEntity 업데이트
//    noticeRepository.save(noticeEntity);
//}



}