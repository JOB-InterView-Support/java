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

        UserEntity currentUser = mypageService.getUserInfo(userId); // 현재 유저 정보 가져오기
        String userPw = updatedUser.getUserPw();

        UserEntity result;
        log.info("입력받은 userPw: {}", userPw);

        // userPw가 null 또는 빈 문자열인지 확인
        if (userPw != null && !userPw.trim().isEmpty()) {
            if (!passwordEncoder.matches(userPw, currentUser.getUserPw())) {
                // userPw가 암호화된 값인지 확인
                if (userPw.startsWith("$2a$")) { // 암호화된 비밀번호로 간주
                    log.info("암호화된 비밀번호가 전달됨. 기존 비밀번호 유지.");
                    result = mypageService.updateUserWithoutPassword(
                            userId,
                            updatedUser.getUserName(),
                            updatedUser.getUserPhone(),
                            updatedUser.getUserDefaultEmail()
                    );
                } else {
                    // 비밀번호가 새로 입력된 경우
                    log.info("비밀번호 포함 회원정보 수정");
                    updatedUser.setUserPw(passwordEncoder.encode(userPw));
                    result = mypageService.updateUser(
                            userId,
                            updatedUser.getUserName(),
                            updatedUser.getUserPw(),
                            updatedUser.getUserPhone(),
                            updatedUser.getUserDefaultEmail()
                    );
                }
            } else {
                log.info("비밀번호가 기존 비밀번호와 동일합니다. 수정되지 않습니다.");
                result = mypageService.updateUserWithoutPassword(
                        userId,
                        updatedUser.getUserName(),
                        updatedUser.getUserPhone(),
                        updatedUser.getUserDefaultEmail()
                );
            }
        } else {
            // 비밀번호가 입력되지 않은 경우
            log.info("비밀번호 미입력, 비밀번호 제외 회원정보 수정");
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
