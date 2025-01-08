package org.myweb.jobis.jobposting.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public ResponseEntity<JobPostingResponse> searchJobPostings(
            @RequestParam(required = false) String jobMidCd,
            @RequestParam(required = false) String locMcd,
            @RequestParam(required = false) String eduLv,
            @RequestParam(required = false) String jobType,
            HttpServletRequest request) {

        // 헤더에서 UUID 추출
        String uuid = request.getHeader("X-User-UUID");
        if (uuid == null || uuid.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        JobPostingResponse response = jobPostingService.searchJobPostings(jobMidCd, locMcd, eduLv, jobType, uuid);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobPostingResponse.Job> getJobPostingDetail(
            @PathVariable String id, HttpServletRequest request) {

        // 헤더에서 UUID 추출
        String uuid = request.getHeader("X-User-UUID");
        if (uuid == null || uuid.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        JobPostingResponse.Job job = jobPostingService.getJobPostingDetail(id, uuid);
        return ResponseEntity.ok(job);
    }
}
