package org.myweb.jobis.user.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.user.jpa.entity.UserEntity;
import org.myweb.jobis.user.model.dto.User;
import org.myweb.jobis.user.jpa.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder; // 암호화 라이브러리 추가
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // PasswordEncoder 주입

    /**
     * 아이디 중복 확인
     * @param username 사용자 아이디
     * @return 중복 여부
     */
    public boolean isUsernameDuplicate(String username) {
        log.debug("아이디 중복 확인 요청: {}", username);
        return userRepository.existsByUserName(username);
    }

    /**
     * 전화번호 중복 확인
     * @param phoneNumber 전화번호
     * @return 중복 여부
     */
    public boolean isPhoneNumberDuplicate(String phoneNumber) {
        log.debug("전화번호 중복 확인 요청: {}", phoneNumber);
        return userRepository.existsByUserPhone(phoneNumber);
    }

    /**
     * 이메일 중복 확인
     * @param email 이메일 주소
     * @return 중복 여부
     */
    public boolean isEmailDuplicate(String email) {
        log.debug("이메일 중복 확인 요청: {}", email);
        return userRepository.existsByUserDefaultEmail(email);
    }

    /**
     * 회원가입 처리
     * @param user 회원 정보
     * @return 성공 여부
     */
    public boolean insertUser(User user) {
        try {
            // 디버깅: 회원 정보 확인
            log.debug("회원가입 요청 데이터: {}", user);

            // 필수 데이터 검증
            if (user.getUuid() == null) {
                user.setUuid(generateUuid()); // UUID 생성
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
            log.debug("변환된 UserEntity 데이터: {}", userEntity);

            userRepository.save(userEntity);
            log.info("회원가입 성공: {}", user.getUserId());

            return true;
        } catch (Exception e) {
            log.error("회원가입 중 오류 발생: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * UUID 생성
     * @return UUID 문자열
     */
    private String generateUuid() {
        return java.util.UUID.randomUUID().toString();
    }
}
