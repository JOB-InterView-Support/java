package org.myweb.jobis.jobposting.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.jobposting.model.dto.JobPostingResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class JobPostingService {
    private final RestTemplate restTemplate;

    @Value("${saramins.api.key}")
    private String apiKey;

    @Value("${saramins.api.url}")
    private String apiUrl;

    public JobPostingResponse searchJobPostings(
            String jobType,
            String locMcd,
            String eduLv,
            String jobMidCd,
            Integer count,
            Integer start,
            String total,
            String sort) {

        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl)
                    .queryParam("access-key", apiKey)
                    .queryParam("fields", "posting-date,expiration-date,keyword-code,count");

            // Add optional parameters
            Optional.ofNullable(jobType).ifPresent(val -> builder.queryParam("job_type", val));
            Optional.ofNullable(locMcd).ifPresent(val -> builder.queryParam("loc_mcd", val));
            Optional.ofNullable(eduLv).ifPresent(val -> builder.queryParam("edu_lv", val));
            Optional.ofNullable(jobMidCd).ifPresent(val -> builder.queryParam("job_mid_cd", val));
            Optional.ofNullable(count).ifPresent(val -> builder.queryParam("count", val));
            Optional.ofNullable(start).ifPresent(val -> builder.queryParam("start", val));
            Optional.ofNullable(total).ifPresent(val -> builder.queryParam("total", val));
            Optional.ofNullable(sort).ifPresent(val -> builder.queryParam("sort", val));

            HttpHeaders headers = createHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);

            log.debug("Requesting Saramin API with URL: {}", builder.toUriString());
            ResponseEntity<JobPostingResponse> response = restTemplate.exchange(
                    builder.build().toUri(),
                    HttpMethod.GET,
                    entity,
                    JobPostingResponse.class
            );

            return handleResponse(response);
        } catch (RestClientException e) {
            log.error("Error while searching job postings", e);
            return new JobPostingResponse();  // 빈 응답 객체를 반환하거나, 상황에 맞는 값을 반환
        }
    }

    public JobPostingResponse getJobPostingDetail(String id) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl)
                    .queryParam("access-key", apiKey)
                    .queryParam("id", id)
                    .queryParam("fields", "posting-date,expiration-date,keyword-code,count");

            HttpHeaders headers = createHeaders();
            HttpEntity<?> entity = new HttpEntity<>(headers);

            log.debug("Requesting job posting detail with ID: {}", id);
            ResponseEntity<JobPostingResponse> response = restTemplate.exchange(
                    builder.build().toUri(),
                    HttpMethod.GET,
                    entity,
                    JobPostingResponse.class
            );

            return handleResponse(response);
        } catch (RestClientException e) {
            log.error("Error while fetching job posting detail for id: {}", id, e);
            return new JobPostingResponse();  // 빈 응답 객체를 반환하거나, 상황에 맞는 값을 반환
        }
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }

    private JobPostingResponse handleResponse(ResponseEntity<JobPostingResponse> response) {
        JobPostingResponse jobPostingResponse = response.getBody();
        if (jobPostingResponse == null) {
            jobPostingResponse = new JobPostingResponse();
        }

        if (jobPostingResponse.getJobs() == null) {
            jobPostingResponse.setJobs(new JobPostingResponse.Jobs());
        }

        return jobPostingResponse;
    }
}