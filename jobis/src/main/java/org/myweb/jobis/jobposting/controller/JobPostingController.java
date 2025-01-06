package org.myweb.jobis.jobposting.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.jobposting.model.dto.JobPosting;
import org.myweb.jobis.jobposting.model.dto.JobPostingResponse;
import org.myweb.jobis.jobposting.model.service.JobPostingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jobposting")
@Slf4j
public class JobPostingController {

    @Autowired
    private JobPostingService jobPostingService;

    @GetMapping("/search")
    public ResponseEntity<JobPostingResponse> searchJobPostings(
            @RequestParam(required = false) String jobMidCd,
            @RequestParam(required = false) String locMcd,
            @RequestParam(required = false) String eduLv,
            @RequestParam(required = false) String jobType,
            @RequestParam(defaultValue = "10") Integer count,
            @RequestParam(defaultValue = "1") Integer start,
            @RequestParam(required = false) String total,
            @RequestParam(defaultValue = "pd") String sort
    ) {
        log.info("Searching job postings with parameters: jobMidCd={}, locMcd={}, eduLv={}, jobType={}, count={}, start={}, sort={}",
                jobMidCd, locMcd, eduLv, jobType, count, start, sort);

        JobPostingResponse response = jobPostingService.searchJobPostings(
                jobType, locMcd, eduLv, jobMidCd, count, start, total, sort);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobPostingResponse> getJobPostingDetail(@PathVariable String id) {
        log.info("Fetching job posting details for id: {}", id);
        JobPostingResponse response = jobPostingService.getJobPostingDetail(id);
        return ResponseEntity.ok(response);
    }
}