package org.myweb.jobis.jobposting.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobPostingResponse {
        private List<JobPosting> jobs;
        private int totalCount;         // 총 채용공고 수
        private int totalPages;         // 전체 페이지 수
        private int currentPage;        // 현재 페이지
        private int pageSize;           // 페이지 당 항목 수


}