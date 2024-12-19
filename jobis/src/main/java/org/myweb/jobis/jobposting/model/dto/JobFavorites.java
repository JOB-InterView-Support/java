package org.myweb.jobis.jobposting.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.myweb.jobis.jobposting.jpa.entity.JobFavoritesEntity;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobFavorites {

    @NotBlank
    private String jobFavoritesNo;

    @NotBlank
    private String uuid;

    @NotBlank
    private String jobPostingId;

    private LocalDateTime jobCreatedDate;

    // Entity 객체로 변환하는 메서드
    public JobFavoritesEntity toEntity() {
        return JobFavoritesEntity.builder()
                .jobFavoritesNo(jobFavoritesNo)
                .uuid(uuid)
                .jobPostingId(jobPostingId)
                .jobCreatedDate(jobCreatedDate)
                .build();
    }

    // Entity 객체로부터 DTO 생성하는 메서드
    public static JobFavorites fromEntity(JobFavoritesEntity entity) {
        return JobFavorites.builder()
                .jobFavoritesNo(entity.getJobFavoritesNo())
                .uuid(entity.getUuid())
                .jobPostingId(entity.getJobPostingId())
                .jobCreatedDate(entity.getJobCreatedDate())
                .build();
    }
}
