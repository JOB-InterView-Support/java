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
            @RequestParam(required = false) String jobType, // 근무형태
            @RequestParam(required = false) String locMcd, // 지역 코드
            @RequestParam(required = false) String eduLv, // 학력 조건
            @RequestParam(required = false) String jobCd, // 직무 코드
            @RequestParam(defaultValue = "5") int count, // 검색 횟수
            @RequestParam(defaultValue = "1") int start, // 시작 페이지
            @RequestParam(defaultValue = "110") int total, // 총 횟수
            @RequestParam(defaultValue = "pd") String sort // 정렬 기준
    ) {
        // 서비스 호출
        Object result = jobPostingService.searchJobPostings(jobType, locMcd, eduLv, jobCd, count, start, total, sort);

        // 서비스 결과 로그로 출력

        return ResponseEntity.ok(result);
    }

    // 채용공고 상세 조회
    @GetMapping("/{id}")
    public JobPosting getJobPosting(@PathVariable Long id) {

        JobPosting jobPosting = jobPostingService.getJobPostingById(id);
        return jobPosting;
    }
}