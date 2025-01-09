package org.myweb.jobis.jobposting.controller;

import org.myweb.jobis.jobposting.model.dto.JobPosting;
import org.myweb.jobis.jobposting.model.service.JobPostingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobposting")
public class JobPostingController {

    private final JobPostingService jobPostingService;

    public JobPostingController(JobPostingService jobPostingService) {
        this.jobPostingService = jobPostingService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<JobPosting>> searchJobPostings(
            @RequestParam String jobType,
            @RequestParam String locMcd,
            @RequestParam String eduLv,
            @RequestParam String jobCd,
            @RequestParam int count,
            @RequestParam int start,
            @RequestParam String sort) {
        List<JobPosting> jobPostings = jobPostingService.searchJobs(jobType, locMcd, eduLv, jobCd, count, start, sort);
        return ResponseEntity.ok(jobPostings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobPosting> getJobPostingDetail(@PathVariable String id) {
        JobPosting jobPosting = jobPostingService.getJobPostingDetail(id);
        return ResponseEntity.ok(jobPosting);
    }
}
