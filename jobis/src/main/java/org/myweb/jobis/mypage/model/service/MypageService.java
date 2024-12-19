package org.myweb.jobis.mypage.model.service;

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

    public UserEntity getUserInfo(String userId){
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
    }


    //회원정보 수정
    public UserEntity updateUser(String userId, String userName, String userPw, String userPhone, String userDefaultEmail) {
        // 사용자 조회
        UserEntity existingUser = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // 수정 가능한 필드 업데이트
        existingUser.setUserName(userName);
        existingUser.setUserPw(userPw);
        existingUser.setUserPhone(userPhone);
        existingUser.setUserDefaultEmail(userDefaultEmail);

        // 마지막 업데이트 시간 설정
        existingUser.setUserUpdateAt(LocalDateTime.now());

        // 변경된 사용자 저장
        return userRepository.save(existingUser);
    }


}
