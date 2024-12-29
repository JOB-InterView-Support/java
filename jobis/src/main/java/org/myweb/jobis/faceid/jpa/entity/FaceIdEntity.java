package org.myweb.jobis.faceid.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.myweb.jobis.faceid.model.dto.FaceId;

import java.time.LocalDateTime;

/**
 * FACEID 테이블의 Entity 클래스
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "FACEID")
public class FaceIdEntity {

    @Id
    @Column(name = "USER_FACE_ID", length = 50, nullable = false)
    private String userFaceId;

    @Column(name = "UUID", length = 50, nullable = false)
    private String uuid;

    @Column(name = "IMAGE_PATH", length = 255, nullable = false)
    private String imagePath;

    @Column(name = "CAPTURED_AT", nullable = false)
    private LocalDateTime capturedAt;

    @Column(name = "UPDATE_AT")
    private LocalDateTime updateAt;

    @PrePersist
    protected void onCreate() {
        this.capturedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateAt = LocalDateTime.now();
    }

    /**
     * Entity -> DTO 변환 메서드
     */
    public FaceId toDto() {
        return FaceId.builder()
                .userFaceId(this.userFaceId)
                .uuid(this.uuid)
                .imagePath(this.imagePath)
                .capturedAt(this.capturedAt)
                .updateAt(this.updateAt)
                .build();
    }
}
