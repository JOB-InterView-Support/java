package com.sevensegment.jobis.jobposting.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.sevensegment.jobis.jobposting.jpa.entity.JobFavoritesEntity;

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

    private String title;
    private String company;  // 회사명
    private String industry; // 산업 분야
    private String location; // 직무 위치
    private String salary;   // 급여


    // JobPosting을 제외한 생성자 추가
    public JobFavorites(String jobFavoritesNo, String uuid, String jobPostingId, LocalDateTime jobCreatedDate) {
        this.jobFavoritesNo = jobFavoritesNo;
        this.uuid = uuid;
        this.jobPostingId = jobPostingId;
        this.jobCreatedDate = jobCreatedDate;
    }

    public JobFavorites(String jobFavoritesNo, String uuid, String jobPostingId, LocalDateTime jobCreatedDate, String title, String company, String industry, String location, String salary, JobPosting jobPosting) {
        this.jobFavoritesNo = jobFavoritesNo;
        this.uuid = uuid;
        this.jobPostingId = jobPostingId;
        this.jobCreatedDate = jobCreatedDate;
        this.title = title;
        this.company = company;
        this.industry = industry;
        this.location = location;
        this.salary = salary;
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
                entity.toDto().getTitle(),
                entity.toDto().getCompany(),
                entity.toDto().getIndustry(),
                entity.toDto().getLocation(),
                entity.toDto().getSalary(),
                jobPosting  // 채용공고 정보 포함
        );
    }
}
