package org.myweb.jobis.jobposting.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

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
    private String jobFavoritesNo;
    private String uuid;
    private String jobPostingId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Timestamp jobCreatedDate;

    // Entity 객체로 변환하는 메서드
    public JobFavoritesEntity toEntity() {
        return JobFavoritesEntity.builder()
                .jobFavoritesNo(jobFavoritesNo)
                .uuid(uuid)
                .jobPostingId(jobPostingId)
                .jobCreatedDate(jobCreatedDate)
                .build();

    }
}
