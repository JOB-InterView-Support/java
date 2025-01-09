package org.myweb.jobis.jobposting.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JobFavoriteResponse {
    private String jobPostingId;
}
