package org.myweb.jobis.mypage.model.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.mypage.jpa.entity.SelfIntroduceEntity;
import org.myweb.jobis.mypage.jpa.repository.SelfIntroduceRepository;
import org.myweb.jobis.mypage.model.dto.SelfIntroduce;
import org.myweb.jobis.products.jpa.repository.ProductsRepository;
import org.myweb.jobis.ticket.jpa.entity.TicketEntity;
import org.myweb.jobis.ticket.jpa.repository.TicketRepository;
import org.myweb.jobis.user.jpa.entity.UserEntity;
import org.myweb.jobis.user.jpa.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MypageService {
    private final UserRepository userRepository;
    private final SelfIntroduceRepository selfIntroduceRepository;
    private final TicketRepository ticketRepository;

    @PersistenceContext
    private EntityManager entityManager;

    // 사용자 정보
    public UserEntity getUserInfo(String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
    }

    public UserEntity updateUser(String userId, String userName, String userPw, String userPhone, String userDefaultEmail) {
        UserEntity existingUser = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        log.info("Before update - Email: {}, Phone: {}", existingUser.getUserDefaultEmail(), existingUser.getUserPhone());

        // 이름 업데이트
        if (userName != null && !userName.isEmpty()) {
            existingUser.setUserName(userName);
        }

        // 비밀번호 업데이트 (입력된 경우에만)
        if (userPw != null && !userPw.isEmpty()) {
            log.info("Updating password for user: {}", userId);
            existingUser.setUserPw(userPw); // PasswordEncoder 처리된 값을 Controller에서 전달
        }

        // 전화번호 업데이트
        if (userPhone != null && !userPhone.isEmpty()) {
            existingUser.setUserPhone(userPhone);
        }

        // 이메일 업데이트
        if (userDefaultEmail != null && !userDefaultEmail.isEmpty()) {
            existingUser.setUserDefaultEmail(userDefaultEmail);
        }

        userRepository.save(existingUser);
        entityManager.flush(); // 영속성 컨텍스트 동기화

        log.info("After update - Email: {}, Phone: {}", existingUser.getUserDefaultEmail(), existingUser.getUserPhone());

        return existingUser;
    }

    public UserEntity updateUserWithoutPassword(String userId, String userName, String userPhone, String userDefaultEmail) {
        UserEntity existingUser = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        log.info("Before update (no password) - Email: {}, Phone: {}", existingUser.getUserDefaultEmail(), existingUser.getUserPhone());

        // 이름 업데이트
        if (userName != null && !userName.isEmpty()) {
            existingUser.setUserName(userName);
        }

        // 전화번호 업데이트
        if (userPhone != null && !userPhone.isEmpty()) {
            existingUser.setUserPhone(userPhone);
        }

        // 이메일 업데이트
        if (userDefaultEmail != null && !userDefaultEmail.isEmpty()) {
            existingUser.setUserDefaultEmail(userDefaultEmail);
        }

        // 비밀번호는 수정하지 않음 (명시적으로 유지)
        log.info("비밀번호 미변경 회원 정보 : {}", userId);

        userRepository.save(existingUser);
        return existingUser;
    }

    public void updateUserSecessionStatus(String userId, String userDeletionReason) {
        UserEntity existingUser = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        log.info("Before secession update - userId: {}", userId);

        // 탈퇴 상태 및 사유 설정
        existingUser.setUserDeletionStatus("Y");
        existingUser.setUserDeletionReason(userDeletionReason); // null 허용
        existingUser.setUserDeletionDate(java.time.LocalDateTime.now());

        // 변경사항 저장
        userRepository.save(existingUser);
        entityManager.flush(); // 영속성 컨텍스트 동기화

        log.info("After secession update - userId: {}, deletion status: {}, reason: {}, date: {}",
                userId,
                existingUser.getUserDeletionStatus(),
                existingUser.getUserDeletionReason(),
                existingUser.getUserDeletionDate());

        log.info("Returning response with status: 200");
    }


    public List<SelfIntroduceEntity> getIntroList(String uuid) {
        return selfIntroduceRepository.findByUuidAndIntroIsDeleted(uuid, "N");
    }

    public List<SelfIntroduceEntity> getIntroListFiltered(String uuid, String introIsEdited) {
        return selfIntroduceRepository.findByUuidAndIntroIsDeletedAndIntroIsEdited(uuid, "N", introIsEdited);
    }

    public boolean canCreateMoreIntroductions(String uuid, int maxCount) {
        long currentCount = selfIntroduceRepository.countByUuidAndIntroIsDeletedAndIntroIsEdited(uuid, "N", "N");
        return currentCount < maxCount;
    }

    public SelfIntroduce getIntroDetailByIntroNo(String introNo) {
        // Repository에서 introNo로 데이터 조회
        SelfIntroduceEntity entity = selfIntroduceRepository.findByIntroNo(introNo)
                .orElseThrow(() -> new RuntimeException("자기소개서를 찾을 수 없습니다. introNo: " + introNo));

        return entity.toDto(); // Entity를 DTO로 변환하여 반환
    }

    public void createIntroduction(SelfIntroduce newIntro) {
        SelfIntroduceEntity entity = new SelfIntroduceEntity();
        entity.setIntroTitle(newIntro.getIntroTitle());
        entity.setApplicationCompanyName(newIntro.getApplicationCompanyName());
        entity.setWorkType(newIntro.getWorkType());
        entity.setCertificate(newIntro.getCertificate());
        entity.setIntroContents(newIntro.getIntroContents());
        entity.setUuid(newIntro.getUuid()); // UUID 설정
        entity.setIntroDate(java.time.LocalDateTime.now()); // 작성일 설정
        selfIntroduceRepository.save(entity);
        log.info("새 자기소개서가 저장되었습니다. Entity: {}", entity);
    }

    public void updateIntroduction(String introNo, SelfIntroduce updateIntro) {
        SelfIntroduceEntity existingEntity = selfIntroduceRepository.findByIntroNo(introNo)
                .orElseThrow(() -> new RuntimeException("자기소개서를 찾을 수 없습니다. " + introNo));
        // 업데이트할 데이터 설정
        if (updateIntro.getIntroTitle() != null) {
            existingEntity.setIntroTitle(updateIntro.getIntroTitle());
        }
        if (updateIntro.getApplicationCompanyName() != null) {
            existingEntity.setApplicationCompanyName(updateIntro.getApplicationCompanyName());
        }
        if (updateIntro.getWorkType() != null) {
            existingEntity.setWorkType(updateIntro.getWorkType());
        }
        if (updateIntro.getCertificate() != null) {
            existingEntity.setCertificate(updateIntro.getCertificate());
        }
        if (updateIntro.getIntroContents() != null) {
            existingEntity.setIntroContents(updateIntro.getIntroContents());
        }

        existingEntity.setIntroDate(java.time.LocalDateTime.now());

        selfIntroduceRepository.save(existingEntity);
    }
    public void deleteSelfIntroduction(String introNo){
        SelfIntroduceEntity existingEntity = selfIntroduceRepository.findByIntroNo(introNo)
                .orElseThrow(() -> new RuntimeException("자기소개서를 찾을 수 없습니다. " + introNo));

        existingEntity.setIntroIsDeleted("Y");
        existingEntity.setIntroDeletedDate(java.time.LocalDateTime.now());

        selfIntroduceRepository.save(existingEntity);
        entityManager.flush();
    }

    @Transactional
    public void updateFaceIdStatusToN(String uuid) {
        // findByUuid 직접 사용
        UserEntity user = userRepository.findByUuid(uuid);
        if (user == null) {
            throw new IllegalArgumentException("User not found with UUID: " + uuid);
        }

        user.setUserFaceIdStatus("N");
    }

    public List<TicketEntity> getTicketsByUuidSortedByPurchaseDate(String uuid) {
        List<TicketEntity> tickets = ticketRepository.findAllByUuidOrderByTicketStartDateDesc(uuid);
        if (tickets == null || tickets.isEmpty()) {
            log.info("티켓 데이터가 없습니다. UUID: {}", uuid);
            return List.of(); // 빈 리스트 반환
        }
        return tickets;
    }


}
