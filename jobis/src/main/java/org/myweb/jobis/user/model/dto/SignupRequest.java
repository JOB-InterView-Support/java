package org.myweb.jobis.user.model.dto;

import lombok.Data;

@Data
public class SignupRequest {
    private User user; // User DTO 객체
    private String snsType; // snsType 필드
}
