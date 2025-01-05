package org.myweb.jobis.jobposting.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.myweb.jobis.jobposting.jpa.entity.JobFavoritesEntity;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobFavorites {
    private String jobFavoritesNo; // 즐겨찾기 번호
    private String uuid; // 사용자 UUID
    private String jobPostingId; // 공고 ID (사람인 API의 공고 ID와 일치)
    private LocalDateTime jobCreatedDate; // 즐겨찾기 생성 일자

    public JobFavorites(String jobFavoritesNo, String uuid, String jobPostingId, LocalDateTime jobCreatedDate,
                        String title, String companyName, String location, String job_type,
                        String loc_mcd, String job_mid_cd, String edu_lv) {
        this.jobFavoritesNo = jobFavoritesNo;
        this.uuid = uuid;
        this.jobPostingId = jobPostingId;
        this.jobCreatedDate = jobCreatedDate;
    }

    // Entity -> DTO 변환
    public static JobFavoritesEntity toEntity(JobFavorites jobFavorites) {
        return new JobFavoritesEntity(
                jobFavorites.getJobFavoritesNo(),
                jobFavorites.getUuid(),
                jobFavorites.getJobPostingId(),
                jobFavorites.getJobCreatedDate(),
                null // UserEntity는 외부에서 설정해야 할 수 있음
        );
    }

    // DTO -> Entity 변환
    public static JobFavorites toDto(JobFavoritesEntity entity, JobPostingResponse.Job job) {
        return new JobFavorites(
                entity.getJobFavoritesNo(),
                entity.getUuid(),
                entity.getJobPostingId(),
                entity.getJobCreatedDate()
                // 여기서 JobPostingResponse.Job 객체를 사용하여 필요한 데이터 설정 가능
        );
    }
}