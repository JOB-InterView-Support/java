package org.myweb.jobis.jobposting.controller;

import lombok.RequiredArgsConstructor;
import org.myweb.jobis.jobposting.model.service.JobPostingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jobPostings")
@RequiredArgsConstructor
public class JobPostingController {

    private final JobPostingService jobPostingService;

    @GetMapping
    public Object searchJobPostings(
            @RequestParam(required = false) String indCd,
            @RequestParam(required = false) String locCd,
            @RequestParam(required = false) String eduLv,
            @RequestParam(required = false) String jobType,
            @RequestParam(defaultValue = "10") String count,
            @RequestParam(defaultValue = "0") String start,
            @RequestParam(defaultValue = "pd") String sort) {

        return jobPostingService.searchJobPostings(indCd, locCd, eduLv, jobType, count, start, sort);
    }
}
