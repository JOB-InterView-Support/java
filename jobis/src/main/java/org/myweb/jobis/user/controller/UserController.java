package org.myweb.jobis.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.myweb.jobis.user.model.service.UserService;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin
public class UserController {

    private final UserService userService;

    @PostMapping("/checkuserId")
    public ResponseEntity<String> checkUsername(@RequestBody Map<String, String> requestData) {
        System.out.println("아이디 중복확인");
        String users = requestData.get("users"); // POST 요청 body에서 값 추출
        System.out.println(users);
        boolean isDuplicate = userService.isUsernameDuplicate(users);
        return isDuplicate ? new ResponseEntity<>("dup", HttpStatus.OK)
                : new ResponseEntity<>("ok", HttpStatus.OK);
    }

}
