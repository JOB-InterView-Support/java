package org.myweb.jobis.jobposting.controller;

import org.myweb.jobis.jobposting.model.dto.ApiResponse;
import org.myweb.jobis.jobposting.model.dto.JobPosting;
import org.myweb.jobis.jobposting.model.service.JobPostingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/jobPosting")
public class JobPostingController {

    private final JobPostingService jobPostingService = new JobPostingService();

    // 채용 공고 목록을 가져오는 엔드포인트
    @GetMapping("/job-postings")
    public ApiResponse getJobPostings(
            @RequestParam String keyword,
            @RequestParam(required = false) String jobType,
            @RequestParam(required = false) String location) {
        return jobPostingService.getJobPostings(keyword, jobType, location);
    }
}