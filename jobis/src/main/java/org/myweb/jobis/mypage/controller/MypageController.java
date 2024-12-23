package org.myweb.jobis.mypage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.mypage.model.service.MypageService;
import org.myweb.jobis.user.jpa.entity.UserEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/mypage")
@RequiredArgsConstructor
@CrossOrigin
public class MypageController {
    private final MypageService mypageService;
    private final PasswordEncoder passwordEncoder;

    // 회원 정보 불러오기
    @GetMapping("/{userId}")
    public ResponseEntity<UserEntity> getUserInfo(@PathVariable String userId) {
        UserEntity userInfo = mypageService.getUserInfo(userId);
        return ResponseEntity.ok(userInfo);
    }

    // 회원 정보 수정 (JSON 형식)
    @PutMapping("/{userId}")
    public ResponseEntity<UserEntity> updateUser(
            @PathVariable String userId,
            @RequestBody UserEntity updatedUser) {
        log.info("Received update request for userId: {}", userId);
        log.info("Received data: {}", updatedUser);

        if (updatedUser == null) {
            log.error("Request body is missing or invalid.");
            return ResponseEntity.badRequest().build();
        }

        UserEntity result;

        // 비밀번호가 변경되었을 경우 처리
        if (updatedUser.getUserPw() != null && !updatedUser.getUserPw().isEmpty()) {
            updatedUser.setUserPw(passwordEncoder.encode(updatedUser.getUserPw()));
            result = mypageService.updateUser(
                    userId,
                    updatedUser.getUserName(),
                    updatedUser.getUserPw(),
                    updatedUser.getUserPhone(),
                    updatedUser.getUserDefaultEmail()
            );
        } else {
            // 비밀번호 변경 없이 업데이트
            result = mypageService.updateUserWithoutPassword(
                    userId,
                    updatedUser.getUserName(),
                    updatedUser.getUserPhone(),
                    updatedUser.getUserDefaultEmail()
            );
        }

        log.info("Updated entity: {}", result);
        return ResponseEntity.ok(result);
    }
}
