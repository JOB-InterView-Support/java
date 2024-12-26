package org.myweb.jobis.jobposting.controller;

import lombok.RequiredArgsConstructor;
import org.myweb.jobis.jobposting.model.service.JobPostingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/jobpostings")
@RequiredArgsConstructor
public class JobPostingController {

    private final JobPostingService jobPostingService;

    @GetMapping("/search")
    public Object searchJobPostings(
            @RequestParam(required = false) String indCd,
            @RequestParam(required = false) String locCd,
            @RequestParam(required = false) String eduLv,
            @RequestParam(required = false) String jobType,
            @RequestParam(defaultValue = "10") int count,  // 페이지 크기 (기본값 10)
            @RequestParam(defaultValue = "1") int start,  // 페이지 번호 (기본값 0)
            @RequestParam(defaultValue = "pd") String sort) {

        return jobPostingService.searchJobPostings(indCd, locCd, eduLv, jobType, count, start, sort);
    }
}
