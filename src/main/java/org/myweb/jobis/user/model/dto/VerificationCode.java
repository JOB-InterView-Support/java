package org.myweb.jobis.user.model.dto;

import java.time.LocalDateTime;

public class VerificationCode {
    private String code;
    private LocalDateTime createdAt;

    public VerificationCode(String code) {
        this.code = code;
        this.createdAt = LocalDateTime.now();
    }

    public String getCode() {
        return code;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
