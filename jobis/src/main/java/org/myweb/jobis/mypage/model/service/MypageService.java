package org.myweb.jobis.mypage.model.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.user.jpa.entity.UserEntity;
import org.myweb.jobis.user.jpa.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MypageService {
    private final UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager; // 엔티티 매니저 추가

    public UserEntity getUserInfo(String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
    }

    // 회원정보 수정 (비밀번호 포함)
    public UserEntity updateUser(String userId, String userName, String userPw, String userPhone, String userDefaultEmail) {
        UserEntity existingUser = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        log.info("Before update - Email: {}, Phone: {}", existingUser.getUserDefaultEmail(), existingUser.getUserPhone());

        // 데이터 업데이트
        existingUser.setUserName(userName);
        existingUser.setUserPw(userPw); // 암호화된 비밀번호를 저장
        existingUser.setUserPhone(userPhone);
        existingUser.setUserDefaultEmail(userDefaultEmail);

        // 저장
        userRepository.save(existingUser);

        // 최신 상태의 데이터를 다시 로드
        UserEntity updatedUser = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Failed to reload user data"));

        log.info("After update - Email: {}, Phone: {}", updatedUser.getUserDefaultEmail(), updatedUser.getUserPhone());

        return updatedUser; // 최신 데이터를 반환
    }

    // 회원정보 수정 (비밀번호 제외)
    public UserEntity updateUserWithoutPassword(String userId, String userName, String userPhone, String userDefaultEmail) {
        UserEntity existingUser = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        log.info("Before update (no password) - Email: {}, Phone: {}", existingUser.getUserDefaultEmail(), existingUser.getUserPhone());

        // 비밀번호 제외하고 데이터 업데이트
        existingUser.setUserName(userName);
        existingUser.setUserPhone(userPhone);
        existingUser.setUserDefaultEmail(userDefaultEmail);

        // 저장
        userRepository.save(existingUser);
        entityManager.flush(); // 동기화 강제

        // 최신 상태의 데이터를 다시 로드
        UserEntity updatedUser = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Failed to reload user data"));

        log.info("After update (no password) - Email: {}, Phone: {}", updatedUser.getUserDefaultEmail(), updatedUser.getUserPhone());

        return updatedUser; // 최신 데이터를 반환
    }
}
