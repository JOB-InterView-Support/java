package org.myweb.jobis.jobposting.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.myweb.jobis.jobposting.model.dto.JobFavorites;
import org.myweb.jobis.user.jpa.entity.UserEntity;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "JOB_FAVORITES")
public class JobFavoritesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 또는 GenerationType.AUTO
    @Column(name = "JOB_FAVORITES_NO", length = 50, nullable = false)
    private String jobFavoritesNo; // 즐겨찾기 ID

    @Column(name = "uuid", length = 50, nullable = false)
    private String uuid; // 사용자 UUID

    @Column(name = "JOB_POSTING_ID", length = 50, nullable = false)
    private String jobPostingId; // 공고 ID

    @Column(name = "JOB_CREATED_DATE", nullable = false)
    private Timestamp jobCreatedDate; // 즐겨찾기 추가 날짜

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UUID", referencedColumnName = "UUID", insertable = false, updatable = false)
    private UserEntity user;

    // Entity -> DTO 변환 메서드
    public JobFavorites toDto() {
        return JobFavorites.builder()
                .jobFavoritesNo(jobFavoritesNo)
                .uuid(uuid)
                .jobPostingId(jobPostingId)
                .jobCreatedDate(jobCreatedDate.toString())
                .build();
    }

    // DTO -> Entity 변환 메서드
    public static JobFavoritesEntity fromDto(JobFavorites jobFavorites) {
        return JobFavoritesEntity.builder()
                .jobFavoritesNo(jobFavorites.getJobFavoritesNo())
                .uuid(jobFavorites.getUuid())
                .jobPostingId(jobFavorites.getJobPostingId())
                .jobCreatedDate(jobFavorites.getJobCreatedDate() != null
                        ? Timestamp.valueOf(jobFavorites.getJobCreatedDate())
                        : new Timestamp(System.currentTimeMillis()))
                .build();
    }
}
