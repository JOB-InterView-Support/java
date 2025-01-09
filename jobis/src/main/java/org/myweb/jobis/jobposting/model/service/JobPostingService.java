package org.myweb.jobis.jobposting.model.service;

import org.myweb.jobis.jobposting.SaraminApiClient;
import org.myweb.jobis.jobposting.model.dto.JobPosting;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class JobPostingService {

    private final SaraminApiClient saraminApiClient;

    public JobPostingService(SaraminApiClient saraminApiClient) {
        this.saraminApiClient = saraminApiClient;
    }

    public List<JobPosting> searchJobs(String jobType, String locMcd, String eduLv, String jobCd, int count, int start, String sort) {
        return saraminApiClient.searchJobs(jobType, locMcd, eduLv, jobCd, count, start, sort);
    }

    public JobPosting getJobPostingDetail(String id) {
        return saraminApiClient.getJobDetail(id);
    }
}
