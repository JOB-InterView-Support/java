package org.myweb.jobis.user.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.myweb.jobis.user.jpa.entity.UserEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * USERS 테이블의 DTO 클래스
 * Entity와 상호 변환 가능하며 필요한 데이터를 매핑합니다.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private String uuid;
    private String userId;
    private String userPw; // 비밀번호
    private String userName; // 이름
    private String userDefaultEmail; // 기본 이메일
    @DateTimeFormat(pattern = "yyyy-MM-dd") // 생년월일 포맷 지정
    private LocalDate userBirthday; // 생년월일
    private String userPhone; // 전화번호
    private String userGender; // 성별
    private LocalDateTime userCreateAt; // 생성일
    private LocalDateTime userUpdateAt; // 업데이트일
    private String userRestrictionStatus; // 정지 여부
    private String userRestrictionReason; // 정지 사유 추가
    private String userDeletionStatus; // 탈퇴 여부
    private String userDeletionReason; // 탈퇴 사유
    private LocalDateTime userDeletionDate; // 탈퇴일
    private String userKakaoEmail; // 카카오 이메일
    private String userGoogleEmail; // 구글 이메일
    private String userNaverEmail; // 네이버 이메일
    private String userRefreshToken; // 리프레시 토큰
    private String userFaceIdStatus; // 페이스 ID 여부
    private String adminYn; // 관리자 여부

    /**
     * DTO -> Entity 변환 메서드
     *
     * @return UserEntity
     */
    public UserEntity toEntity() {
        return UserEntity.builder()
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
                .userRestrictionReason(this.userRestrictionReason) // 정지 사유 추가
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