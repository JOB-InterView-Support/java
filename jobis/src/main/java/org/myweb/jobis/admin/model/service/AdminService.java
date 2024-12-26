package org.myweb.jobis.admin.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.user.jpa.entity.UserEntity;
import org.myweb.jobis.user.jpa.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    public Page<UserEntity> getPagedUsers(Pageable pageable) {
        // count 쿼리와 페이징 데이터 확인
        Page<UserEntity> users = userRepository.findAll(pageable);

        log.info("== 사용자 페이징 데이터 확인 ==");
        log.info("Total Elements (총 사용자 수): {}", users.getTotalElements());
        log.info("Total Pages (총 페이지 수): {}", users.getTotalPages());
        log.info("Page Content (현재 페이지 데이터): {}", users.getContent());

        return users;
    }

    public UserEntity findUserByUuid(String uuid) {
        return userRepository.findByUuid(uuid); // Repository에서 조회
    }


    @Transactional
    public void restrictMember(String uuid, String reason) {
        UserEntity user = userRepository.findById(uuid)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다."));
        log.info("제제 사유 어드민 서비스");
        log.info(uuid);
        log.info(reason);
        user.setUserRestrictionStatus("Y");
        user.setUserRestrictionReason(reason);
        userRepository.save(user);
    }

    public void liftMemberSanction(String uuid) {
        UserEntity user = userRepository.findByUuid(uuid);
        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }

        user.setUserRestrictionStatus("N");
        user.setUserRestrictionReason(null);
        userRepository.save(user);
    }



    @Transactional
    public void liftUnsubscribeStatus(String uuid) {
        UserEntity user = userRepository.findByUuid(uuid);
        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }

        // 탈퇴 상태 해제 처리
        user.setUserDeletionStatus("N"); // 탈퇴 상태 해제
        user.setUserDeletionReason(null); // 탈퇴 사유 초기화
        user.setUserDeletionDate(null); // 탈퇴 날짜 초기화

        userRepository.save(user);
    }

    @Transactional
    public void deleteMember(String uuid) {
        log.info("회원삭제 서비스 시작");
        try {
            UserEntity user = userRepository.findByUuid(uuid);
            if (user == null) {
                throw new RuntimeException("사용자를 찾을 수 없습니다.");
            }

            userRepository.delete(user); // 회원 삭제
            log.info("회원 삭제 완료");
        } catch (Exception e) {
            log.error("회원 삭제 중 예외 발생: ", e);
            throw e; // 예외를 다시 던져 컨트롤러로 전달
        }
    }


    public void promoteToAdmin(String uuid) throws Exception {
        UserEntity user = userRepository.findById(uuid).orElseThrow(() -> new Exception("사용자를 찾을 수 없습니다."));
        user.setAdminYn("Y");
        userRepository.save(user);
    }

    public void demoteToUser(String uuid) throws Exception {
        UserEntity user = userRepository.findById(uuid).orElseThrow(() -> new Exception("사용자를 찾을 수 없습니다."));
        user.setAdminYn("N");
        userRepository.save(user);
    }

}
