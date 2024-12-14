package org.myweb.jobis.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.myweb.jobis.user.jpa.entity.UserEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private String uuid;
    private String userId;
    private String userPw;
    private String userName;
    private String userDefaultEmail;
    @DateTimeFormat(pattern = "yyyyMMdd")
    private LocalDate userBirthday;
    private String userPhone;
    private String userGender;
    private LocalDateTime userCreateAt;
    private LocalDateTime userUpdateAt;
    private String userRestrictionStatus;
    private String userDeletionStatus;
    private String userDeletionReason;
    private LocalDateTime userDeletionDate;
    private String userKakaoEmail;
    private String userGoogleEmail;
    private String userNaverEmail;
    private String userRefreshToken;
    private String userFaceIdStatus;
    private String adminYn;

    public UserEntity toEntity() {
        return UserEntity.builder()
                .uuid(this.uuid)
                .userId(this.userId)
                .userPw(this.userPw) // 비밀번호 추가
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
