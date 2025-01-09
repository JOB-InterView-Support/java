package org.myweb.jobis.jobposting.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.jobposting.model.dto.JobPosting;
import org.myweb.jobis.jobposting.model.service.JobPostingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jobposting")
@RequiredArgsConstructor
@Slf4j
public class JobPostingController {

    private final JobPostingService jobPostingService;

    @GetMapping("/search")
    public ResponseEntity<Object> searchJobPostings(
            @RequestParam(required = false) String jobType, // 산업 코드
            @RequestParam(required = false) String locMcd, // 지역 코드
            @RequestParam(required = false) String eduLv, // 학력 조건
            @RequestParam(required = false) String jobMidCd, //  직무 코드
            @RequestParam(defaultValue = "10") int count, // 검색 횟수
            @RequestParam(defaultValue = "1") int start, // 시작 페이지
            @RequestParam(defaultValue = "pd") String sort)  // 정렬 기준
    {

        Object result = jobPostingService.searchJobPostings(jobType, locMcd, eduLv, jobMidCd, count, start, sort);
        return ResponseEntity.ok(result);
    }

    // 채용공고 상세 조회
    @GetMapping("/{id}")
    public JobPosting getJobPosting(@PathVariable String id) {
        return jobPostingService.getJobPostingById(id);
    }
}
