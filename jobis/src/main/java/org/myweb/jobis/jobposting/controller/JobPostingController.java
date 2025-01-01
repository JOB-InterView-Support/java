package org.myweb.jobis.jobposting.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.jobposting.model.dto.JobPosting;
import org.myweb.jobis.jobposting.model.dto.JobPostingResponse;
import org.myweb.jobis.jobposting.model.service.JobPostingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jobposting")
@RequiredArgsConstructor
@Slf4j
public class JobPostingController {

    private final JobPostingService jobPostingService;

    // 채용공고 목록 검색 API
    @GetMapping("/search")
    public JobPostingResponse searchJobPostings(
            @RequestParam(required = false) String jobType,
            @RequestParam(required = false) String locMcd,
            @RequestParam(required = false) String eduLv,
            @RequestParam(required = false) String jobCd,
            @RequestParam(defaultValue = "10") int count,
            @RequestParam(defaultValue = "1") int start,
            @RequestParam(defaultValue = "desc") String sort,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return jobPostingService.searchJobPostings(jobType, locMcd, eduLv, jobCd, count, start, sort, page, size);
    }

    // 채용공고 목록 가져오기 (기본)
    @GetMapping
    public JobPostingResponse getJobPostings(@RequestParam(defaultValue = "1") int page,
                                             @RequestParam(defaultValue = "10") int size) {
        return jobPostingService.getJobPostings(page, size);
    }

    // 채용공고 상세보기
    @GetMapping("/{id}")
    public JobPosting getJobPostingById(@PathVariable Long id) {
        return jobPostingService.getJobPostingById(id);
    }
}