package org.myweb.jobis.user.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.myweb.jobis.user.model.dto.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * USERS 테이블의 Entity 클래스
 * 데이터베이스 USERS 테이블의 각 컬럼을 매핑합니다.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "USERS")
@Entity
public class UserEntity {

    // UUID - 고유 식별자
    @Id
    @Column(name = "UUID", length = 50, nullable = false)
    private String uuid;

    // 아이디
    @Column(name = "USER_ID", length = 20, nullable = false)
    private String userId;

    // 비밀번호
    @Column(name = "USER_PW", length = 20, nullable = false)
    private String userPw;

    // 이름
    @Column(name = "USER_NAME", length = 50, nullable = false)
    private String userName;

    // 기본 이메일
    @Column(name = "USER_DEFAULT_EMAIL", length = 50, nullable = false)
    private String userDefaultEmail;

    // 생년월일
    @Column(name = "USER_BIRTHDAY", nullable = false)
    private LocalDate userBirthday;

    // 전화번호
    @Column(name = "USER_PHONE", length = 20, nullable = false)
    private String userPhone;

    // 성별
    @Column(name = "USER_GENDER", length = 1, nullable = false)
    private String userGender;

    // 생성 날짜
    @Column(name = "USER_CREATE_AT", nullable = false)
    private LocalDateTime userCreateAt;

    // 마지막 업데이트 날짜
    @Column(name = "USER_UPDATE_AT")
    private LocalDateTime userUpdateAt;

    // 정지 여부 ('Y' 정지, 'N' 정지 아님)
    @Column(name = "USER_RESTRICTION_STATUS", length = 1, nullable = false)
    private String userRestrictionStatus = "N";

    // 탈퇴 여부 ('Y' 탈퇴, 'N' 탈퇴 아님)
    @Column(name = "USER_DELETION_STATUS", length = 1, nullable = false)
    private String userDeletionStatus = "N";

    // 탈퇴 사유
    @Column(name = "USER_DELETION_REASON", length = 1000)
    private String userDeletionReason;

    // 탈퇴 신청 날짜
    @Column(name = "USER_DELETION_DATE")
    private LocalDateTime userDeletionDate;

    // 카카오 이메일
    @Column(name = "USER_KAKAO_EMAIL", length = 20)
    private String userKakaoEmail;

    // 구글 이메일
    @Column(name = "USER_GOOGLE_EMAIL", length = 20)
    private String userGoogleEmail;

    // 네이버 이메일
    @Column(name = "USER_NAVER_EMAIL", length = 20)
    private String userNaverEmail;

    // 리프레시 토큰
    @Column(name = "USER_REFRESH_TOKEN", length = 512)
    private String userRefreshToken;

    // 페이스 아이디 여부 ('Y' 사용, 'N' 미사용)
    @Column(name = "USER_FACEID_STATUS", length = 1, nullable = false)
    private String userFaceIdStatus = "N";

    // 관리자 여부 ('Y' 관리자, 'N' 일반 사용자)
    @Column(name = "ADMIN_YN", length = 1, nullable = false)
    private String adminYn = "N";

    @PrePersist
    protected void onCreate() {
        this.userCreateAt = LocalDateTime.now(); // 현재 시간 자동 설정
    }

    /**
     * Entity를 DTO로 변환하는 메서드
     *
     * @return User DTO 객체
     */
    public User toDto() {
        return User.builder()
                .uuid(this.uuid)
                .userId(this.userId)
                .userPw(this.userPw)
                .userName(this.userName)
                .userDefaultEmail(this.userDefaultEmail)
                .userBirthday(this.userBirthday)
                .userPhone(this.userPhone)
                .userGender(this.userGender)
                .userCreateAt(this.userCreateAt)
                .userUpdateAt(this.userUpdateAt)
                .userRestrictionStatus(this.userRestrictionStatus)
                .userDeletionStatus(this.userDeletionStatus)
                .userDeletionReason(this.userDeletionReason)
                .userDeletionDate(this.userDeletionDate)
                .userKakaoEmail(this.userKakaoEmail)
                .userGoogleEmail(this.userGoogleEmail)
                .userNaverEmail(this.userNaverEmail)
                .userRefreshToken(this.userRefreshToken)
                .userFaceIdStatus(this.userFaceIdStatus)
                .adminYn(this.adminYn)
                .build();
    }
}
