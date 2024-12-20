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
    private final PasswordEncoder passwordEncoder; // 비밀번호 암호화를 위한 Bean

    // 회원 정보 불러오기
    @GetMapping("/{userId}")
    public ResponseEntity<UserEntity> getUserInfo(@PathVariable String userId) {
        UserEntity userInfo = mypageService.getUserInfo(userId);
        return ResponseEntity.ok(userInfo);
    }

    // 회원 정보 수정
    @PutMapping("/{userId}")
    public ResponseEntity<UserEntity> updateUser(
            @PathVariable String userId,
            @RequestBody UserEntity updatedUser) {
        UserEntity result;

        if (updatedUser.getUserPw() != null && !updatedUser.getUserPw().isEmpty()) {
            // 비밀번호 암호화
            updatedUser.setUserPw(passwordEncoder.encode(updatedUser.getUserPw()));
            result = mypageService.updateUser(
                    userId, updatedUser.getUserName(), updatedUser.getUserPw(),
                    updatedUser.getUserPhone(), updatedUser.getUserDefaultEmail());
        } else {
            // 비밀번호를 제외한 데이터 업데이트
            result = mypageService.updateUserWithoutPassword(
                    userId, updatedUser.getUserName(), updatedUser.getUserPhone(),
                    updatedUser.getUserDefaultEmail());
        }

        // 응답 데이터에서 비밀번호 제거
        result.setUserPw(null); // 비밀번호 필드를 제거하여 클라이언트로 전송
        log.info("Returning updated user data: {}", result);

        return ResponseEntity.ok(result);
    }
}
