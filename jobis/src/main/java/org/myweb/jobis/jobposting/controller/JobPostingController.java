package org.myweb.jobis.jobposting.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.jobposting.model.dto.JobPosting;
import org.myweb.jobis.jobposting.model.service.JobPostingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/jobposting")
@RequiredArgsConstructor
@Slf4j
public class JobPostingController {

    private final JobPostingService jobPostingService;

    // 채용공고 검색 API
    @GetMapping("/search")
    public ResponseEntity<Object> searchJobPostings(
            @RequestParam(required = false) String indCd,
            @RequestParam(required = false) String locCd,
            @RequestParam(required = false) String eduLv,
            @RequestParam(required = false) String jobCd,
            @RequestParam(defaultValue = "1") int count,
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "pd") String sort) {

        log.info("검색 요청: indCd={}, locCd={}, eduLv={}, jobCd={}, count={}, start={}, sort={}",
                indCd, locCd, eduLv, jobCd, count, start, sort, page, size);

        Object result = jobPostingService.searchJobPostings(indCd, locCd, eduLv, jobCd, count, start, sort);

        log.info("검색 결과: {}", result);

        return ResponseEntity.ok(result);
    }
    // 채용공고 상세 조회
    @GetMapping("/{id}")
    public JobPosting getJobPosting(@PathVariable Long id) {
        return jobPostingService.getJobPostingById(id);
    }
}
