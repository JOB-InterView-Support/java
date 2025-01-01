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

    public Object searchJobPostings(String jobType, String locMcd, String eduLv, String jobCd,
                                    int count, int start, String sort, int page, int size) {

        String fullUri = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("access-key", apiKey)
                .queryParam("job_type", jobType)
                .queryParam("loc_mcd", locMcd)
                .queryParam("edu_lv", eduLv)
                .queryParam("job_cd", jobCd)
                .queryParam("count", count)
                .queryParam("start", start)
                .queryParam("sort", sort)
                .queryParam("page", page)
                .queryParam("size", size)
                .toUriString();

        try {
            return restTemplate.getForObject(fullUri, Object.class);
        } catch (Exception e) {
            throw new RuntimeException("API 호출 실패: " + e.getMessage(), e);
        }
    }

    // 채용공고 목록 가져오기
    public JobPostingResponse getJobPostings(int page, int size) {
        int start = (page - 1) * size;
        String url = UriComponentsBuilder.fromHttpUrl("https://oapi.saramin.co.kr/job-search")
                .queryParam("access-key", apiKey)
                .queryParam("page", page)  // 페이지 번호
                .queryParam("size", size)  // 한 페이지 당 항목 수
                .toUriString();

        // 외부 API 호출
        JobPostingResponse response = restTemplate.getForObject(url, JobPostingResponse.class);

        // 응답 처리
        if (response != null && response.getJobs() != null) {
            int totalItems = response.getTotalCount(); // 총 채용공고 수
            int totalPages = (int) Math.ceil((double) totalItems / size); // 전체 페이지 수 계산

            return new JobPostingResponse(
                    response.getJobs(),
                    totalItems,  // 전체 항목 수
                    totalPages,  // 전체 페이지 수
                    page,        // 현재 페이지
                    size         // 페이지 크기
            );
        }

        // API 응답이 없거나 비정상일 경우
        return new JobPostingResponse(List.of(), 0, 0, page, size);
    }

    // 채용공고 상세보기
    public JobPosting getJobPostingById(Long id) {
        // 사람인 API에서 상세정보를 가져올 수 있도록 수정 필요
        // 사람인 API에서는 특정 채용공고 ID로 상세 조회를 지원하지 않으므로
        // 목록에서 해당 ID에 맞는 채용공고를 반환하는 방식으로 처리
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