package org.myweb.jobis.jobposting.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.myweb.jobis.jobposting.jpa.entity.JobFavoritesEntity;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobFavorites {

    private String jobFavoritesNo; // 즐겨찾기 ID
    private String uuid; // 사용자 UUID
    private String jobPostingId; // 공고 ID
    private String jobCreatedDate; // 즐겨찾기 추가 날짜

    // DTO -> Entity 변환 메서드
    public JobFavoritesEntity toEntity() {
        return JobFavoritesEntity.builder()
                .jobFavoritesNo(jobFavoritesNo)
                .uuid(uuid)
                .jobPostingId(jobPostingId)
                .jobCreatedDate(jobCreatedDate != null
                        ? Timestamp.valueOf(jobCreatedDate)
                        : new Timestamp(System.currentTimeMillis()))
                .build();
    }
}
