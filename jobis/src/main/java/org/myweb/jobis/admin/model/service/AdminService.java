package org.myweb.jobis.admin.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.user.jpa.entity.UserEntity;
import org.myweb.jobis.user.jpa.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    public void restrictMember(String uuid, String reason) {
        // 회원 정보 조회
        UserEntity user = userRepository.findById(uuid)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다."));

        // 회원 정보 업데이트
        user.setUserRestrictionStatus("Y"); // 삭제 상태를 'Y'로 설정
        user.setUserRestricationReason(reason); // 삭제 사유를 설정
        userRepository.save(user); // 변경 사항 저장
    }

    public void liftMemberSanction(String uuid) {
        UserEntity user = userRepository.findByUuid(uuid);
        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }

        user.setUserRestrictionStatus("N");
        user.setUserRestricationReason(null);
        userRepository.save(user);
    }

}
