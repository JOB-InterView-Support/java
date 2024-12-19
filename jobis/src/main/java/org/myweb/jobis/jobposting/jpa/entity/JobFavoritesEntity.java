package org.myweb.jobis.jobposting.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.myweb.jobis.jobposting.model.dto.JobFavorites;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "JOB_FAVORITES")
public class JobFavoritesEntity {

    @Id
    @Column(name = "job_favorites_no", nullable = false, length = 50)
    private String jobFavoritesNo;  // 즐겨찾기 ID

    @Column(name = "uuid", nullable = false, length = 50)
    private String uuid;  // UUID

    @Column(name = "job_posting_id", nullable = false, length = 50)
    private String jobPostingId;  // 공고 ID

    @Column(name = "job_created_date", nullable = false)
    private LocalDateTime jobCreatedDate;  // 즐겨찾기 추가 날짜

    // Entity에서 DTO로 변환
    public JobFavorites toDto() {
        return JobFavorites.builder()
                .jobFavoritesNo(jobFavoritesNo)
                .uuid(uuid)
                .jobPostingId(jobPostingId)
                .jobCreatedDate(jobCreatedDate)
                .build();
    }

    // DTO에서 Entity로 변환
    public static JobFavoritesEntity fromDto(JobFavorites dto) {
        return JobFavoritesEntity.builder()
                .jobFavoritesNo(dto.getJobFavoritesNo())
                .uuid(dto.getUuid())
                .jobPostingId(dto.getJobPostingId())
                .jobCreatedDate(dto.getJobCreatedDate())
                .build();
    }
}
