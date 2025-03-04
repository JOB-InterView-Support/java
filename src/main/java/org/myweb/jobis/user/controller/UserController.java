package org.myweb.jobis.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.email.model.service.EmailService;
import org.myweb.jobis.user.model.dto.LoginRequest;
import org.myweb.jobis.user.model.dto.SignupRequest;
import org.myweb.jobis.user.model.dto.User;
import org.myweb.jobis.user.model.dto.VerificationCode;
import org.myweb.jobis.user.model.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import org.myweb.jobis.security.jwt.JWTUtil;


@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin
public class UserController {

    private final UserService userService;
    private final JWTUtil JWTUtil;

    // 아이디 중복 확인
    @PostMapping("/checkuserId")
    public ResponseEntity<String> checkUsername(@RequestBody Map<String, String> requestData) {
        System.out.println("아이디 중복확인");
        String users = requestData.get("users"); // POST 요청 body에서 값 추출
        System.out.println(users);
        boolean isDuplicate = userService.isUsernameDuplicate(users);
        return isDuplicate ? new ResponseEntity<>("dup", HttpStatus.OK)
                : new ResponseEntity<>("ok", HttpStatus.OK);
    }

    // 전화번호 중복 확인
    @PostMapping("/checkPhoneNumber")
    public ResponseEntity<String> checkPhoneNumber(@RequestBody Map<String, String> request) {
        String phoneNumber = request.get("phoneNumber");

        // 전화번호 중복 확인 로직
        boolean isDuplicate = userService.isPhoneNumberDuplicate(phoneNumber);

        if (isDuplicate) {
            return ResponseEntity.ok("dup"); // 중복
        } else {
            return ResponseEntity.ok("ok"); // 사용 가능
        }
    }

    // 이메일 중복 확인
    @PostMapping("/checkEmail")
    public ResponseEntity<String> checkEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        boolean isDuplicate = userService.isEmailDuplicate(email);

        if (isDuplicate) {
            return ResponseEntity.ok("dup"); // 중복된 이메일
        } else {
            return ResponseEntity.ok("ok"); // 사용 가능한 이메일
        }
    }

    // Email --------------------------------

    private final EmailService emailService;
    private final Map<String, VerificationCode> verificationCodes = new HashMap<>(); // 이메일-코드 매핑 저장

    // 인증 코드 전송
    @PostMapping("/sendVerificationEmail")
    public ResponseEntity<String> sendVerificationEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        try {
            String verificationCode = emailService.generateVerificationCode();
            emailService.sendEmail(email, "Jobis 인증 코드", "인증 코드는 다음과 같습니다: " + verificationCode);

            // 인증 코드와 생성 시간을 저장
            verificationCodes.put(email, new VerificationCode(verificationCode));

            return ResponseEntity.ok("인증 코드가 이메일로 전송되었습니다.");
        } catch (Exception e) {
            System.err.println("이메일 전송 실패: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("이메일 전송 중 오류가 발생했습니다.");
        }
    }

    // 인증번호 확인
    @PostMapping("/verifyCode")
    public ResponseEntity<String> verifyCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = request.get("code");

        if (email == null || code == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이메일 또는 인증 코드가 누락되었습니다.");
        }

        VerificationCode verificationCode = verificationCodes.get(email);

        if (verificationCode == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("인증 코드가 존재하지 않습니다.");
        }

        // 유효 시간 검증 (예: 3분 이내)
        long minutesElapsed = java.time.Duration.between(verificationCode.getCreatedAt(), java.time.LocalDateTime.now()).toMinutes();
        if (minutesElapsed > 3) {
            verificationCodes.remove(email); // 만료된 코드 삭제
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("인증 코드가 만료되었습니다.");
        }

        // 코드 일치 여부 확인
        if (verificationCode.getCode().equals(code)) {
            verificationCodes.remove(email); // 인증 성공 후 코드 삭제
            return ResponseEntity.ok("인증 성공");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("인증 실패: 인증번호가 틀립니다.");
        }
    }

    // 회원가입
    @PostMapping("/signup")
    public String signup(@RequestBody User user) {
        boolean isInserted = userService.insertUser(user);

        if (isInserted) {
            return "success";
        } else {
            return "fail";
        }
    }

    @PostMapping("/snsSignup")
    public ResponseEntity<String> snsSignup(@RequestBody SignupRequest signupRequest) {
        User user = signupRequest.getUser();
        String snsType = signupRequest.getSnsType();

        try {
            boolean result = userService.snsSignup(user, snsType);
            if (result) {
                return ResponseEntity.ok("success");
            } else {
                return ResponseEntity.status(400).body("회원가입 실패");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }


}
