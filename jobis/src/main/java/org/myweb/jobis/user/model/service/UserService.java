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
    public boolean isUsernameDuplicate(String userId) {
        log.debug("아이디 중복 확인 요청: {}", userId);
        return userRepository.existsByUserId(userId);
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

    @Transactional
    public boolean snsSignup(User user, String snsType) {
        try {
            // UUID 생성 및 기본 값 설정
            if (user.getUuid() == null) {
                user.setUuid(generateUuid());
                log.debug("UUID 생성: {}", user.getUuid());
            }

            // 중복 검사: userId
            if (userRepository.existsByUserId(user.getUserId())) {
                log.error("중복된 아이디로 SNS 회원가입 요청: {}", user.getUserId());
                throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
            }

            // 중복 검사: 이메일
            if (userRepository.existsByUserDefaultEmail(user.getUserDefaultEmail())) {
                log.error("중복된 이메일로 SNS 회원가입 요청: {}", user.getUserDefaultEmail());
                throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
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

            // DTO -> Entity 변환
            UserEntity userEntity = user.toEntity();

            // SNS 타입에 따라 이메일 저장
            switch (snsType.toLowerCase()) {
                case "kakao":
                    userEntity.setUserKakaoEmail(user.getUserDefaultEmail());
                    break;
                case "naver":
                    userEntity.setUserNaverEmail(user.getUserDefaultEmail());
                    break;
                case "google":
                    userEntity.setUserGoogleEmail(user.getUserDefaultEmail());
                    break;
                default:
                    log.error("지원하지 않는 SNS 타입 요청: {}", snsType);
                    throw new IllegalArgumentException("지원하지 않는 SNS 타입입니다: " + snsType);
            }

            // UserEntity 저장
            userRepository.save(userEntity);

            log.info("SNS 회원가입 성공: {}", user.getUserId());
            return true;

        } catch (IllegalArgumentException e) {
            // 잘못된 요청 처리
            log.error("유효하지 않은 요청: {}", e.getMessage());
            return false;

        } catch (Exception e) {
            // 일반적인 예외 처리
            log.error("SNS 회원가입 중 오류 발생: {}", e.getMessage(), e);
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

    // faceLogin
    public User getUserByUuid(String uuid) {
        // UUID로 UserEntity 조회
        UserEntity userEntity = userRepository.findByUuid(uuid);
        if (userEntity != null) {
            // Entity를 DTO로 변환
            return userEntity.toDto();
        }
        return null;
    }

    public String findUserIdByEmail(String email) {
        UserEntity userEntity = userRepository.findByUserDefaultEmail(email);
        return userEntity != null ? userEntity.getUserId() : null;
    }

    public void updatePassword(String userId, String newPassword) {
        UserEntity user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + userId));

        String encryptedPassword = passwordEncoder.encode(newPassword);
        user.setUserPw(encryptedPassword);
        userRepository.save(user);
    }
}
