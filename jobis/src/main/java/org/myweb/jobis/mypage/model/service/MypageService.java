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

        if (userName != null && !userName.isEmpty()) {
            existingUser.setUserName(userName);
        }
        if (userPw != null && !userPw.isEmpty()) {
            existingUser.setUserPw(userPw);
        }
        if (userPhone != null && !userPhone.isEmpty()) {
            existingUser.setUserPhone(userPhone);
        }
        if (userDefaultEmail != null && !userDefaultEmail.isEmpty()) {
            existingUser.setUserDefaultEmail(userDefaultEmail);
        }

        userRepository.save(existingUser);
        log.info("After update - Email: {}, Phone: {}", existingUser.getUserDefaultEmail(), existingUser.getUserPhone());

        return existingUser;
    }

    public UserEntity updateUserWithoutPassword(String userId, String userName, String userPhone, String userDefaultEmail) {
        UserEntity existingUser = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        log.info("Before update (no password) - Email: {}, Phone: {}", existingUser.getUserDefaultEmail(), existingUser.getUserPhone());

        if (userName != null && !userName.isEmpty()) {
            existingUser.setUserName(userName);
        }
        if (userPhone != null && !userPhone.isEmpty()) {
            existingUser.setUserPhone(userPhone);
        }
        if (userDefaultEmail != null && !userDefaultEmail.isEmpty()) {
            existingUser.setUserDefaultEmail(userDefaultEmail);
        }

        userRepository.save(existingUser);
        entityManager.flush();

        log.info("After update (no password) - Email: {}, Phone: {}", existingUser.getUserDefaultEmail(), existingUser.getUserPhone());

        return existingUser;
    }
}
