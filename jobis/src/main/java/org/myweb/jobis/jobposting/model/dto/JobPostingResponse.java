package org.myweb.jobis.jobposting.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class JobPostingResponse {
    private List<JobPosting> jobs;
}
