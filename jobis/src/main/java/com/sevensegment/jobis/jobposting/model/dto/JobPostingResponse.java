package com.sevensegment.jobis.jobposting.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobPostingResponse {
        private Jobs jobs;


        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        @Builder
        public static class Jobs {
                private Integer count;
                private Integer start;
                private String total;
                private List<JobPosting> job;
        }
}