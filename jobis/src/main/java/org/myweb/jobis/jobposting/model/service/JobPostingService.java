package org.myweb.jobis.jobposting.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.jobposting.model.dto.JobPosting;
import org.myweb.jobis.jobposting.model.dto.JobPostingResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobPostingService {
    private final RestTemplate restTemplate;

    @Value("${saramins.api.key}")
    private String apiKey;

    @Value("${saramins.api.url}")
    private String apiUrl;

    public JobPostingResponse searchJobPosting(
            String job_mid_cd, String loc_mcd, String edu_lv,
            String job_type, int start, int count) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("access-key", apiKey)
                .queryParam("start", start)
                .queryParam("count", count);

        // Optional parameters
        if (job_mid_cd != null && !job_mid_cd.isEmpty()) {
            builder.queryParam("job_mid_cd", job_mid_cd);
        }
        if (loc_mcd != null && !loc_mcd.isEmpty()) {
            builder.queryParam("loc_mcd", loc_mcd);
        }
        if (edu_lv != null && !edu_lv.isEmpty()) {
            builder.queryParam("edu_lv", edu_lv);
        }
        if (job_type != null && !job_type.isEmpty()) {
            builder.queryParam("job_type", job_type);
        }

        // Log the constructed URL
        log.info("Request URL: {}", builder.build().toUri());

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<JobPostingResponse> response = restTemplate.exchange(
                builder.build().toUri(),
                HttpMethod.GET,
                entity,
                JobPostingResponse.class
        );

        // Log the API response
        log.info("API Response: {}", response.getBody());

        JobPostingResponse jobPostingResponse = response.getBody();
        if (jobPostingResponse == null) {
            jobPostingResponse = new JobPostingResponse();
        }

        // Initialize jobs if null
        if (jobPostingResponse.getJobs() == null) {
            jobPostingResponse.setJobs(new JobPostingResponse.Jobs());
        }

        return response.getBody();
    }


    public JobPostingResponse getJobPostingDetail(String id) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("access-key", apiKey)
                .queryParam("id", id);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<JobPostingResponse> response = restTemplate.exchange(
                builder.build().toUri(),
                HttpMethod.GET,
                entity,
                JobPostingResponse.class
        );

        JobPostingResponse jobPostingResponse = response.getBody();
        if (jobPostingResponse == null) {
            jobPostingResponse = new JobPostingResponse();
        }

        // jobs가 null인 경우 빈 Jobs 객체로 초기화
        if (jobPostingResponse.getJobs() == null) {
            jobPostingResponse.setJobs(new JobPostingResponse.Jobs());
        }

        return response.getBody();
    }
}