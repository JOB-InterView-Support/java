package org.myweb.jobis.user.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.security.JwtTokenProvider;
import org.myweb.jobis.user.jpa.entity.UserEntity;
import org.myweb.jobis.user.model.dto.LoginRequest;
import org.myweb.jobis.user.model.dto.User;
import org.myweb.jobis.user.jpa.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 아이디 중복 확인
     *
     * @param username 사용자 아이디
     * @return 중복 여부
     */
    public boolean isUsernameDuplicate(String username) {
        log.debug("아이디 중복 확인 요청: {}", username);
        return userRepository.existsByUserName(username);
    }

    /**
     * 전화번호 중복 확인
     *
     * @param phoneNumber 전화번호
     * @return 중복 여부
     */
    public boolean isPhoneNumberDuplicate(String phoneNumber) {
        log.debug("전화번호 중복 확인 요청: {}", phoneNumber);
        return userRepository.existsByUserPhone(phoneNumber);
    }

    /**
     * 이메일 중복 확인
     *
     * @param email 이메일 주소
     * @return 중복 여부
     */
    public boolean isEmailDuplicate(String email) {
        log.debug("이메일 중복 확인 요청: {}", email);
        return userRepository.existsByUserDefaultEmail(email);
    }

    /**
     * 회원가입 처리
     *
     * @param user 회원 정보
     * @return 성공 여부
     */
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

    /**
     * 로그인 처리
     *
     * @param loginRequest 로그인 요청 정보
     * @return AccessToken, RefreshToken
     */
    public Map<String, String> login(LoginRequest loginRequest) {
        log.debug("로그인 요청 데이터: {}", loginRequest);

        // 사용자 인증
        User user = authenticate(loginRequest.getUserId(), loginRequest.getUserPw());

        // JWT 발급
        String accessToken = jwtTokenProvider.createAccessToken(
                user.getUuid(),
                user.getUserId(),
                user.getAdminYn(),
                user.getUserName()
        );
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getUuid());

        // 리프레시 토큰 저장
        saveRefreshToken(user.getUserId(), refreshToken);

        log.info("AccessToken 발급 완료: {}", accessToken);
        log.info("RefreshToken 발급 완료: {}", refreshToken);

        return Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken
        );
    }

    /**
     * 사용자 인증
     *
     * @param userId 사용자 ID
     * @param userPw 사용자 비밀번호
     * @return 인증된 사용자 정보
     */
    public User authenticate(String userId, String userPw) {
        UserEntity userEntity = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("아이디가 존재하지 않습니다."));

        if (!passwordEncoder.matches(userPw, userEntity.getUserPw())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return userEntity.toDto();
    }

    /**
     * 리프레시 토큰 저장
     *
     * @param userId 사용자 ID
     * @param refreshToken 리프레시 토큰
     */
    public void saveRefreshToken(String userId, String refreshToken) {
        UserEntity userEntity = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        userEntity.setUserRefreshToken(refreshToken); // 리프레시 토큰 설정
        userRepository.save(userEntity); // 업데이트된 사용자 엔티티 저장
        log.info("리프레시 토큰이 데이터베이스에 저장되었습니다. UserId: {}", userId);
    }

    /**
     * UUID 생성
     *
     * @return UUID 문자열
     */
    private String generateUuid() {
        return java.util.UUID.randomUUID().toString();
    }

    // 로그아웃 - RefreshToken clear
    @Transactional
    public void clearRefreshToken(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        userEntity.setUserRefreshToken(null); // 리프레시 토큰 제거
        userRepository.save(userEntity);
        log.info("Refresh Token 제거 완료: User ID = {}", userId);
    }

}
