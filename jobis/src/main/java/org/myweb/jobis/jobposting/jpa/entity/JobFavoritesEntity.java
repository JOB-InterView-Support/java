package org.myweb.jobis.jobposting.jpa.entity;

import jakarta.persistence.*;
import lombok.*;
import org.myweb.jobis.jobposting.model.dto.JobFavorites;
import org.myweb.jobis.jobposting.model.dto.JobPosting;
import org.myweb.jobis.user.jpa.entity.UserEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "JOB_FAVORITES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobFavoritesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_favorites_no")
    private String jobFavoritesNo; // 즐겨찾기 번호

    @Column(name = "uuid")
    private String uuid; // 사용자 UUID

    @Column(name = "job_posting_id")
    private String jobPostingId; // 사람인 API의 공고 ID와 일치

    @Column(name = "job_created_date")
    private LocalDateTime jobCreatedDate; // 즐겨찾기 생성 일자

    @ManyToOne
    @JoinColumn(name = "uuid", referencedColumnName = "uuid", insertable = false, updatable = false)
    private UserEntity userEntity; // 사용자 엔티티 (외래 키 관계)

    // DTO -> Entity 변환
    public static JobFavoritesEntity fromDto(JobFavorites jobFavorites) {
        return new JobFavoritesEntity(
                jobFavorites.getJobFavoritesNo(),
                jobFavorites.getUuid(),
                jobFavorites.getJobPostingId(), // 사람인 API의 공고 ID 사용
                jobFavorites.getJobCreatedDate(),
                null // UserEntity는 외부에서 설정해야 할 수 있음
        );
    }
}

