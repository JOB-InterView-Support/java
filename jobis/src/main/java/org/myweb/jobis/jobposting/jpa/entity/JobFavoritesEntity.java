package org.myweb.jobis.jobposting.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.myweb.jobis.jobposting.model.dto.JobFavoriteResponse;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobFavoritesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uuid;
    private String jobPostingId;

    public JobFavoriteResponse toDto() {
        return JobFavoriteResponse.builder()
                .jobPostingId(jobPostingId)
                .build();
    }
}
