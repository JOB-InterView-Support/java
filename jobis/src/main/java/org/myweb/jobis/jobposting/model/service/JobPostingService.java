package org.myweb.jobis.jobposting.model.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    public Object searchJobPostings(String indCd, String locCd, String eduLv, String jobCd,
                                    int count, int start, String sort) {

        String fullUri = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("access-key", apiKey)
                .queryParam("ind_cd", indCd)
                .queryParam("loc_cd", locCd)
                .queryParam("edu_lv", eduLv)
                .queryParam("job_cd", jobCd)
                .queryParam("count", count)
                .queryParam("start", start)
                .queryParam("sort", sort)
                .toUriString();

        try {
            return restTemplate.getForObject(fullUri, Object.class);
        } catch (Exception e) {
            throw new RuntimeException("API 호출 실패: " + e.getMessage(), e);
        }
    }

    // 채용공고 목록 가져오기
    public JobPostingResponse getJobPostings(int page, int size) {
        // 사람인 API 요청 URL 생성
        String url = UriComponentsBuilder.fromHttpUrl("https://oapi.saramin.co.kr/job-search")
                .queryParam("access-key", apiKey)
                .queryParam("page", page)
                .queryParam("size", size)
                .toUriString();

        // 외부 API 호출
        JobPostingResponse response = restTemplate.getForObject(url, JobPostingResponse.class);
        // response가 null이 아닐 경우
        if (response != null) {
            // 페이지 정보와 함께 반환
            return new JobPostingResponse(
                    response.getJobs(),
                    response.getTotalPages(),
                    response.getTotalElements(),
                    response.getCurrentPage(),
                    response.getSize()
            );
        }
        return new JobPostingResponse(List.of(), 0, 0, page, size);  // 빈 리스트와 0 값으로 반환
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