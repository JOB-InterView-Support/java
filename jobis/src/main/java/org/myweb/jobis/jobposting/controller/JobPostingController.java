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

    @GetMapping("/search")
    public ResponseEntity<JobPostingResponse> searchJobPosting(
            @RequestParam(required = false) String job_mid_cd,
            @RequestParam(required = false) String loc_mcd,
            @RequestParam(required = false) String edu_lv,
            @RequestParam(required = false) String job_type,
            @RequestParam(defaultValue = "1") int start,
            @RequestParam(defaultValue = "10") int count) {

        JobPostingResponse response = jobPostingService.searchJobPosting(
                job_mid_cd, loc_mcd, edu_lv, job_type, start, count);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobPostingResponse> getJobPostingDetail(@PathVariable String id) {
        JobPostingResponse response = jobPostingService.getJobPostingDetail(id);
        return ResponseEntity.ok(response);
    }
}