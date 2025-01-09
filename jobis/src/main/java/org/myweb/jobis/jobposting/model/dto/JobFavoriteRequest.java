package org.myweb.jobis.jobposting.model.dto;

import lombok.Data;

@Data
public class JobFavoriteRequest {
    private String uuid;
    private String jobPostingId;
}
