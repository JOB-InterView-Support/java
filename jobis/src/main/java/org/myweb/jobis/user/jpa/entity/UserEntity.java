package org.myweb.jobis.user.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.myweb.jobis.user.model.dto.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "USERS")
@Entity
public class UserEntity {
    @Id
    @Column(name = "UUID", length = 50, nullable = false)
    private String uuid;

    @Column(name = "USER_ID", length = 20, nullable = false)
    private String userId;

    @Column(name = "USER_PW", length = 20, nullable = false)
    private String userPw;

    @Column(name = "USER_NAME", length = 50, nullable = false)
    private String userName;

    @Column(name = "USER_DEFAULT_EMAIL", length = 50, nullable = false)
    private String userDefaultEmail;

    @Column(name = "USER_BIRTHDAY", nullable = false)
    private LocalDate userBirthday;

    @Column(name = "USER_PHONE", length = 20, nullable = false)
    private String userPhone;

    @Column(name = "USER_GENDER", length = 1, nullable = false)
    private String userGender;

    @Column(name = "USER_CREATE_AT", nullable = false)
    private LocalDateTime userCreateAt;

    @Column(name = "USER_UPDATE_AT")
    private LocalDateTime userUpdateAt;

    @Column(name = "USER_RESTRICTION_STATUS", length = 1, nullable = false)
    private String userRestrictionStatus = "N";

    @Column(name = "USER_DELETION_STATUS", length = 1, nullable = false)
    private String userDeletionStatus = "N";

    @Column(name = "USER_DELETION_REASON", length = 1000)
    private String userDeletionReason;

    @Column(name = "USER_DELETION_DATE")
    private LocalDateTime userDeletionDate;

    @Column(name = "USER_KAKAO_EMAIL", length = 20)
    private String userKakaoEmail;

    @Column(name = "USER_GOOGLE_EMAIL", length = 20)
    private String userGoogleEmail;

    @Column(name = "USER_NAVER_EMAIL", length = 20)
    private String userNaverEmail;

    @Column(name = "USER_REFRESH_TOKEN", length = 512)
    private String userRefreshToken;

    @Column(name = "USER_FACEID_STATUS", length = 1, nullable = false)
    private String userFaceIdStatus = "N";

    public User toDto(){
        return User.builder()
                .uuid(this.uuid)
                .userId(this.userId)
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
                .build();
    }
}
