package org.myweb.jobis.jobposting.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.myweb.jobis.jobposting.model.dto.JobFavorites;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "JOB_FAVORITES")
public class JobFavoritesEntity {

    @Id
    @Column(name = "JOB_FAVORITS_NO", nullable = false, length = 50)
    private String jobFavoritesNo;  // 즐겨찾기 ID
    @Column(name = "UUID", nullable = false, length = 50)
    private String uuid;  // UUID
    @Column(name = "JOB_POSTING_ID", nullable = false, length = 50)
    private String jobPostingId;  // 공고 ID
    @Column(name = "JOB_CREATED_DATE", nullable = false)
    private Timestamp jobCreatedDate;  // 즐겨찾기 추가 날짜

    @PrePersist
    public void onPrePersist() {
        if (jobCreatedDate == null) {
            this.jobCreatedDate = new Timestamp(System.currentTimeMillis());  // Set current date + time
        }
    }

    // Entity에서 DTO로 변환
    public JobFavorites toDto() {
        return JobFavorites.builder()
                .jobFavoritesNo(jobFavoritesNo)
                .uuid(uuid)
                .jobPostingId(jobPostingId)
                .jobCreatedDate(jobCreatedDate)
                .build();
    }
}

