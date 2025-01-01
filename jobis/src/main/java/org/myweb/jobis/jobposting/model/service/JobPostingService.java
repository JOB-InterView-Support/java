package org.myweb.jobis.jobposting.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.jobposting.model.dto.JobPosting;
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

    @Value("${saramins.api.key}")
    private String apiKey;

    @Value("${saramins.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    // 채용공고 목록 검색
    public JobPostingResponse searchJobPostings(String jobType, String locMcd, String eduLv, String jobCd,
                                                int count, int start, String sort, int page, int size) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("access-key", apiKey);

        // Optional 파라미터 추가
        if (jobType != null) uriBuilder.queryParam("job_type", jobType);
        if (locMcd != null) uriBuilder.queryParam("loc_mcd", locMcd);
        if (eduLv != null) uriBuilder.queryParam("edu_lv", eduLv);
        if (jobCd != null) uriBuilder.queryParam("job_cd", jobCd);
        if (count > 0) uriBuilder.queryParam("count", count);
        if (start > 0) uriBuilder.queryParam("start", start);
        if (sort != null) uriBuilder.queryParam("sort", sort);
        if (page > 0) uriBuilder.queryParam("page", page);
        if (size > 0) uriBuilder.queryParam("size", size);

        String fullUri = uriBuilder.toUriString();

        try {
            // API 호출 후, JobPostingResponse로 응답 받기
            JobPostingResponse response = restTemplate.getForObject(fullUri, JobPostingResponse.class);

            // 응답이 없으면 빈 리스트 반환
            if (response == null || response.getJobs() == null) {
                return new JobPostingResponse(List.of(), 0, 0, page, size);
            }

            return response;
        } catch (Exception e) {
            log.error("API 호출 실패: " + e.getMessage());
            throw new RuntimeException("API 호출 실패: " + e.getMessage(), e);
        }
    }

    // 채용공고 목록 가져오기
    public JobPostingResponse getJobPostings(int page, int size) {
        String url = UriComponentsBuilder.fromHttpUrl("https://oapi.saramin.co.kr/job-search")
                .queryParam("access-key", apiKey)
                .queryParam("page", page)
                .queryParam("size", size)
                .toUriString();

        try {
            // 외부 API 호출
            JobPostingResponse response = restTemplate.getForObject(url, JobPostingResponse.class);

            if (response != null && response.getJobs() != null) {
                // 필요한 메타 데이터 계산 후 반환
                int totalCount = response.getJobs().size(); // 예시로 totalCount를 계산
                int totalPages = (int) Math.ceil((double) totalCount / size); // 페이지 수 계산

                response.setTotalCount(totalCount);
                response.setTotalPages(totalPages);
                response.setCurrentPage(page);
                response.setPageSize(size);

                return response;
            }
        } catch (Exception e) {
            log.error("API 호출 실패: " + e.getMessage());
        }

        // 응답이 없으면 빈 리스트와 0 값을 반환
        return new JobPostingResponse(List.of(), 0, 0, page, size);
    }

    // 채용공고 상세보기
    public JobPosting getJobPostingById(Long id) {
        int page = 1; // 페이지 번호 (여기서는 1부터 시작)

        while (true) {
            // 해당 페이지에서 채용공고 목록 가져오기
            JobPostingResponse response = getJobPostings(page, 50); // 한 페이지당 50개 항목

            // 해당 ID에 맞는 채용공고 찾기
            JobPosting jobPosting = response.getJobs().stream()
                    .filter(job -> job.getId().equals(id))
                    .findFirst()
                    .orElse(null);

            if (jobPosting != null) {
                // 해당 채용공고가 있으면 반환
                return jobPosting;
            }

            // 다음 페이지로 이동
            if (page >= response.getTotalPages()) {
                // 마지막 페이지에 도달하면 종료
                break;
            }
            page++; // 페이지 번호 증가
        }

        // 해당 ID로 채용공고를 찾을 수 없는 경우 예외 처리
        throw new RuntimeException("채용공고를 찾을 수 없습니다.");
    }
}