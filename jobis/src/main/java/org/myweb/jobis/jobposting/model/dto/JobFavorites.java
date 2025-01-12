package org.myweb.jobis.jobposting.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.myweb.jobis.jobposting.jpa.entity.JobFavoritesEntity;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class JobFavorites {
    private String jobFavoritesNo; // 즐겨찾기 번호
    private String uuid; // 사용자 UUID
    private String jobPostingId; // 공고 ID
    private LocalDateTime jobCreatedDate; // 즐겨찾기 생성 일자
    private JobPosting jobPosting;  // 채용공고 정보 추가

    // JobPosting을 제외한 생성자 추가
    public JobFavorites(String jobFavoritesNo, String uuid, String jobPostingId, LocalDateTime jobCreatedDate) {
        this.jobFavoritesNo = jobFavoritesNo;
        this.uuid = uuid;
        this.jobPostingId = jobPostingId;
        this.jobCreatedDate = jobCreatedDate;
    }

    public JobFavoritesEntity toEntity() {
        return JobFavoritesEntity.builder()
                .jobFavoritesNo(this.jobFavoritesNo)
                .uuid(this.uuid)
                .jobPostingId(this.jobPostingId)
                .jobCreatedDate(this.jobCreatedDate)
                .build();
    }


    // Entity -> DTO 변환
    public static JobFavorites fromEntity(JobFavoritesEntity entity, JobPosting jobPosting) {
        return new JobFavorites(
                entity.getJobFavoritesNo(),
                entity.getUuid(),
                entity.getJobPostingId(),
                entity.getJobCreatedDate(),
                jobPosting  // 채용공고 정보 포함
        );
    }
}
