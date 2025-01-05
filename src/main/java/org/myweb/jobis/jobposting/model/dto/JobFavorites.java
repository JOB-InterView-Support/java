package org.myweb.jobis.jobposting.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.myweb.jobis.jobposting.jpa.entity.JobFavoritesEntity;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class JobFavorites {
    private String jobFavoritesNo; // 즐겨찾기 번호
    private String uuid; // 사용자 UUID
    private String jobPostingId; // 공고 ID
    private LocalDateTime jobCreatedDate; // 즐겨찾기 생성 일자

    // toEntity 메서드 추가
    public JobFavoritesEntity toEntity() {
        return JobFavoritesEntity.builder()
                .jobFavoritesNo(this.jobFavoritesNo)
                .uuid(this.uuid)
                .jobPostingId(this.jobPostingId)
                .jobCreatedDate(this.jobCreatedDate)
                .build();
    }
}
