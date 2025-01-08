package org.myweb.jobis.jobposting.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.jobposting.jpa.entity.JobFavoritesEntity;
import org.myweb.jobis.jobposting.jpa.repository.JobFavoritesRepository;
import org.myweb.jobis.jobposting.model.dto.JobPostingResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobPostingService {

    private final RestTemplate restTemplate;
    private final JobFavoritesRepository jobFavoritesRepository;

    @Value("${saramins.api.key}")
    private String apiKey;

    @Value("${saramins.api.url}")
    private String apiUrl;

    public JobPostingResponse searchJobPostings(String jobMidCd, String locMcd, String eduLv, String jobType, String uuid) {
        String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("access-key", apiKey)
                .queryParam("job_mid_cd", jobMidCd)
                .queryParam("loc_mcd", locMcd)
                .queryParam("edu_lv", eduLv)
                .queryParam("job_type", jobType)
                .toUriString();

        log.info("Saramin API 요청 URL: {}", url);

        JobPostingResponse response = restTemplate.getForObject(url, JobPostingResponse.class);

        // 즐겨찾기 여부 추가
        if (response != null && response.getJobs() != null) {
            List<String> favoriteIds = jobFavoritesRepository.findByUuid(uuid)
                    .stream()
                    .map(JobFavoritesEntity::getJobPostingId)
                    .toList();

            response.getJobs().getJob().forEach(job -> {
                job.setIsFavorite(favoriteIds.contains(job.getId()));
            });
        }

        return response;
    }

    public JobPostingResponse.Job getJobPostingDetail(String jobPostingId, String uuid) {
        String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("access-key", apiKey)
                .queryParam("id", jobPostingId)
                .toUriString();

        log.info("Saramin API 상세 요청 URL: {}", url);

        JobPostingResponse response = restTemplate.getForObject(url, JobPostingResponse.class);

        if (response != null && response.getJobs() != null && !response.getJobs().getJob().isEmpty()) {
            JobPostingResponse.Job job = response.getJobs().getJob().get(0);
            job.setIsFavorite(jobFavoritesRepository.existsByUuidAndJobPostingId(uuid, jobPostingId));
            return job;
        }

        return null;
    }
}
