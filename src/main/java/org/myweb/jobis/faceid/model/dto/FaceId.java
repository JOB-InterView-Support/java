package org.myweb.jobis.faceid.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * FACEID 테이블의 DTO 클래스
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FaceId {

    private String userFaceId; // 고유 페이스 ID
    private String uuid; // 사용자 UUID
    private String imagePath; // 원본 얼굴 이미지 경로
    private LocalDateTime capturedAt; // 얼굴 캡처 시각
    private LocalDateTime updateAt; // 마지막 업데이트 시각
}