package org.myweb.jobis.mypage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.mypage.model.service.MypageService;
import org.myweb.jobis.user.jpa.entity.UserEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/mypage")
@RequiredArgsConstructor
@CrossOrigin
public class MypageController {
    private final MypageService mypageService;

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
        UserEntity result = mypageService.updateUser(userId, updatedUser.getUserName(), updatedUser.getUserPw(), updatedUser.getUserPhone(), updatedUser.getUserDefaultEmail());
        return ResponseEntity.ok(result);
    }
}
