package com.sevensegment.jobis.jobposting.jpa.entity;

import jakarta.persistence.*;
import lombok.*;
import com.sevensegment.jobis.jobposting.model.dto.JobFavorites;
import com.sevensegment.jobis.user.jpa.entity.UserEntity;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "JOB_FAVORITES")
public class JobFavoritesEntity {
    @Id
    @Column(name = "job_favorites_no")
    private String jobFavoritesNo;

    @Column(name = "UUID", length = 50, nullable = false)
    private String uuid;

    @Column(name = "job_posting_id", nullable = false)
    private String jobPostingId;

    @Column(name = "job_created_date", nullable = false)
    private LocalDateTime jobCreatedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UUID", referencedColumnName = "UUID", insertable = false, updatable = false)
    private UserEntity user;

    @PrePersist
    protected void onCreate() {
        jobCreatedDate = LocalDateTime.now();
    }

    // JobFavorites DTO를 Entity로 변환
    public static JobFavoritesEntity toEntity(JobFavorites dto) {
        return JobFavoritesEntity.builder()
                .jobFavoritesNo(dto.getJobFavoritesNo())
                .uuid(dto.getUuid())
                .jobPostingId(dto.getJobPostingId())
                .jobCreatedDate(dto.getJobCreatedDate())
                .build();
    }

    // toDto 메서드 추가
    public JobFavorites toDto() {
        return new JobFavorites(
                this.jobFavoritesNo,
                this.uuid,
                this.jobPostingId,
                this.jobCreatedDate
        );
    }

}