package org.myweb.jobis.jobposting.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobPosting {
    private String id;
    private String title;
    private String company;
    private String location;
    private String jobType;
    private String eduLevel;
    private String description;
    private String url;
}
