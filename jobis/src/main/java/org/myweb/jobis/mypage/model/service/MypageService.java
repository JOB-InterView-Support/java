package org.myweb.jobis.mypage.model.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.user.jpa.entity.UserEntity;
import org.myweb.jobis.user.jpa.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MypageService {
    private final UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

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

}
