package org.myweb.jobis.user.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.security.jwt.JWTUtil;
import org.myweb.jobis.user.jpa.entity.UserEntity;
import org.myweb.jobis.user.model.dto.LoginRequest;
import org.myweb.jobis.user.model.dto.User;
import org.myweb.jobis.user.jpa.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 아이디 중복 확인
    public boolean isUsernameDuplicate(String username) {
        log.debug("아이디 중복 확인 요청: {}", username);
        return userRepository.existsByUserName(username);
    }

    // 전화번호 중복 확인
    public boolean isPhoneNumberDuplicate(String phoneNumber) {
        log.debug("전화번호 중복 확인 요청: {}", phoneNumber);
        return userRepository.existsByUserPhone(phoneNumber);
    }

    // 이메일 중복 확인
    public boolean isEmailDuplicate(String email) {
        log.debug("이메일 중복 확인 요청: {}", email);
        return userRepository.existsByUserDefaultEmail(email);
    }

    // 회원가입 처리
    public boolean insertUser(User user) {
        try {
            log.debug("회원가입 요청 데이터: {}", user);

            // UUID 생성 및 기본 값 설정
            if (user.getUuid() == null) {
                user.setUuid(generateUuid());
                log.debug("UUID 생성: {}", user.getUuid());
            }

            if (user.getUserPw() == null || user.getUserPw().isEmpty()) {
                log.error("비밀번호가 설정되지 않았습니다.");
                throw new IllegalArgumentException("비밀번호는 필수 항목입니다.");
            }

            // 비밀번호 암호화
            String encryptedPassword = passwordEncoder.encode(user.getUserPw());
            user.setUserPw(encryptedPassword);
            log.debug("암호화된 비밀번호: {}", encryptedPassword);

            // 기본 값 설정
            user.setUserCreateAt(LocalDateTime.now());
            user.setUserRestrictionStatus("N");
            user.setUserDeletionStatus("N");
            user.setUserFaceIdStatus("N");
            user.setAdminYn("N");

            // DTO -> Entity 변환 및 저장
            UserEntity userEntity = user.toEntity();
            userRepository.save(userEntity);

            log.info("회원가입 성공: {}", user.getUserId());
            return true;
        } catch (Exception e) {
            log.error("회원가입 중 오류 발생: {}", e.getMessage(), e);
            return false;
        }
    }

    // 사용자 인증
    public User authenticate(String userId, String userPw) {
        UserEntity userEntity = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("아이디가 존재하지 않습니다."));

        if (!passwordEncoder.matches(userPw, userEntity.getUserPw())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return userEntity.toDto();
    }

    // RefreshToken 저장
    public void saveRefreshToken(String userId, String refreshToken) {
        log.info("Saving new RefreshToken for userId: {}", userId);
        userRepository.updateRefreshToken(userId, refreshToken); // 매개변수 두 개 전달
        log.info("RefreshToken 저장 완료: userId = {}", userId);
    }

    // RefreshToken 삭제
    public void clearRefreshToken(String userId) {
        log.info("Clearing RefreshToken for userId: {}", userId);
        userRepository.clearRefreshTokenQuery(userId); // null로 설정
        log.info("RefreshToken 제거 완료: userId = {}", userId);
    }


    // 사용자 조회
    public User selectMember(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return userEntity.toDto();
    }

    // UUID 생성
    private String generateUuid() {
        return java.util.UUID.randomUUID().toString();
    }
}
