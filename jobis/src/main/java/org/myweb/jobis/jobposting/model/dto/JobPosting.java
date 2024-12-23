package org.myweb.jobis.jobposting.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobPosting {
    private String jobId;               // 채용 공고 ID
    private String jobTitle;            // 채용 공고 제목
    private String company;             // 회사 이름
    private String location;            // 근무지
    private String jobType;             // 직무 종류
    private String experienceRequired;  // 요구 경력
    private String educationRequired;   // 요구 학력
}
