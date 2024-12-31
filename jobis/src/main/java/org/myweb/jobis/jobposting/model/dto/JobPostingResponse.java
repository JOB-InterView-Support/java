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
    private List<JobPosting> jobs; // 빈 리스트로 초기화
    private int totalPages;
    private int totalElements;
    private int currentPage;
    private int size;




}