package org.myweb.jobis.jobposting;

import org.myweb.jobis.jobposting.model.dto.JobPosting;
import org.myweb.jobis.jobposting.model.dto.JobPostingResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class SaraminApiClient {

    @Value("${saramins.api.url}")
    private String apiKey;

    @Value("${saramins.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    public SaraminApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<JobPosting> searchJobs(String jobType, String locMcd, String eduLv, String jobCd, int count, int start, String sort) {
        String url = String.format("%s/job-search?access-key=%s&job-type=%s&loc-mcd=%s&edu-lv=%s&job-cd=%s&count=%d&start=%d&sort=%s",
                apiUrl, apiKey, jobType, locMcd, eduLv, jobCd, count, start, sort);

        ResponseEntity<JobPostingResponse> response = restTemplate.exchange(url, HttpMethod.GET, null, JobPostingResponse.class);

        return response.getBody().getJobs();
    }

    public JobPosting getJobDetail(String id) {
        String url = String.format("%s/job/%s?access-key=%s", apiUrl, id, apiKey);

        ResponseEntity<JobPosting> response = restTemplate.exchange(url, HttpMethod.GET, null, JobPosting.class);

        return response.getBody();
    }
}
