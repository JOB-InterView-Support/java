package org.myweb.jobis.jobposting.jpa.entity;

import jakarta.persistence.*;
import lombok.*;
import org.myweb.jobis.user.jpa.entity.UserEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "JOB_FAVORITES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobFavoritesEntity {
    @Id
    @Column(name = "JOB_FAVORITES_NO", nullable = false)
    private String jobFavoritesNo; // PK로 매핑

    @Column(name = "UUID", nullable = false)
    private String uuid;

    @Column(name = "JOB_POSTING_ID", nullable = false)
    private String jobPostingId;

    @Column(name = "JOB_CREATED_DATE", nullable = false)
    private LocalDateTime jobCreatedDate;

    @ManyToOne
    @JoinColumn(name = "UUID", referencedColumnName = "uuid", insertable = false, updatable = false)
    private UserEntity userEntity;

    public static JobFavoritesEntity fromDto(String jobFavoritesNo, String uuid, String jobPostingId) {
        JobFavoritesEntity entity = new JobFavoritesEntity();
        entity.setJobFavoritesNo(jobFavoritesNo);
        entity.setUuid(uuid);
        entity.setJobPostingId(jobPostingId);
        entity.setJobCreatedDate(LocalDateTime.now());
        return entity;
    }
}
