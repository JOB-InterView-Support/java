package org.myweb.jobis.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private String uuid;
    private String userId;
    private String userName;
    private String userDefaultEmail;
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
}
