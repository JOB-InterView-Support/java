package org.myweb.jobis.jobposting.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class ApiResponse {
        private String status; // API 응답 상태 (success 등)
        private List<JobPosting> data; // 채용 공고 목록
}

