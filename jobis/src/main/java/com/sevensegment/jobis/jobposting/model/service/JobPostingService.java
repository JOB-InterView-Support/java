package com.sevensegment.jobis.jobposting.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.sevensegment.jobis.jobposting.model.dto.JobPosting;
import com.sevensegment.jobis.jobposting.model.dto.JobPostingResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobPostingService {

    @Value("${saramins.api.key}")
    private String apiKey;

    @Value("${saramins.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @Cacheable(value = "searchResults",
            key = "{#jobType, #locMcd, #eduLv, #jobMidCd, #count, #start, #sort}",
            unless = "#result == null")
    public Object searchJobPostings(String jobType, String locMcd, String eduLv, String jobMidCd,
                                    int count, int start, String sort) {

        String fullUri = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("access-key", apiKey)
                .queryParam("job_type", jobType)
                .queryParam("loc_mcd", locMcd)
                .queryParam("edu_lv", eduLv)
                .queryParam("job_mid_cd", jobMidCd)
                .queryParam("count", count)
                .queryParam("start", start)
                .queryParam("sort", sort)
                .toUriString();

        try {
            return restTemplate.getForObject(fullUri, Object.class);
        } catch (Exception e) {
            log.error("API 호출 실패", e);
            return null;
        }
    }

    // 채용공고 목록 가져오기
    public JobPostingResponse getJobPostings(int start, int count) {
        // 사람인 API 요청 URL 생성
        String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("access-key", apiKey)
                .queryParam("start", start)
                .queryParam("count", count)
                .toUriString();
        // 외부 API 호출
        JobPostingResponse response = restTemplate.getForObject(url, JobPostingResponse.class);
        if (response != null) {
            // Jobs 객체를 래핑하여 반환
            return new JobPostingResponse(
                    new JobPostingResponse.Jobs(
                            response.getJobs().getCount(),  // 전체 개수
                            response.getJobs().getStart(),  // 시작 위치
                            response.getJobs().getTotal(),  // 총 개수
                            response.getJobs().getJob()     // 실제 채용공고 리스트
                    )
            );
        }
        return new JobPostingResponse(
                new JobPostingResponse.Jobs(0, 0, "0", List.of())
        );
    }

    // 채용공고 상세보기
    @Cacheable(value = "jobPostings", key = "#id", condition = "#id != null")
    public JobPosting getJobPostingById(String jobPostingId) {
        int start = 1; // 페이지 번호 (여기서는 1부터 시작)
        int count = 50; // 한 페이지 당 항목 수 (이 값을 증가시켜 더 많은 데이터를 요청)

        while (true) {
            // 한 페이지당 count 개 항목을 요청
            JobPostingResponse response = getJobPostings(start, count);

            // 해당 jobPostingId를 가진 채용공고를 찾음
            JobPosting jobPosting = response.getJobs().getJob().stream()
                    .filter(job -> job.getId().equals(jobPostingId))
                    .findFirst()
                    .orElse(null);

            // 채용공고를 찾으면 바로 반환
            if (jobPosting != null) {
                return jobPosting;
            }

            // 마지막 페이지에 도달했으면 종료
            if (start * count >= Integer.parseInt(response.getJobs().getTotal())) {
                break;
            }

            // 페이지 번호 증가
            start++;
        }

        // 채용공고를 찾을 수 없으면 예외를 던짐
        throw new RuntimeException("채용공고를 찾을 수 없습니다.");
    }

    // 캐시 수동 삭제 메서드
    @CacheEvict(value = "jobPostings", allEntries = true)
    public void clearJobPostingsCache() {
        log.info("채용공고 캐시 전체 삭제");
    }

    @CacheEvict(value = "searchResults", allEntries = true)
    public void clearSearchResultsCache() {
        log.info("검색결과 캐시 전체 삭제");
    }
}