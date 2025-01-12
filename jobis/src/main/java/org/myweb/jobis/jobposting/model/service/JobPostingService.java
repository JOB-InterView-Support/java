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
            throw new RuntimeException("API 호출 실패: " + e.getMessage(), e);
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

        // response가 null이 아닐 경우
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

        // 빈 리스트와 0 값으로 반환
        return new JobPostingResponse(
                new JobPostingResponse.Jobs(0, 0, "0", List.of())
        );
    }

    // 채용공고 상세보기
    public JobPosting getJobPostingById(String jobPostingId) {
        // 사람인 API에서 상세정보를 가져올 수 있도록 수정 필요
        // 사람인 API에서는 특정 채용공고 ID로 상세 조회를 지원하지 않으므로
        // 목록에서 해당 ID에 맞는 채용공고를 반환하는 방식으로 처리
        int start = 1; // 페이지 번호 (여기서는 1부터 시작)
        int count = 10; // 한 페이지 당 항목 수

        while (true) {
            // 해당 페이지에서 채용공고 목록 가져오기
            JobPostingResponse response = getJobPostings(start, count); // 한 페이지당 count 개 항목

            // 해당 ID에 맞는 채용공고 찾기
            JobPosting jobPosting = response.getJobs().getJob().stream()
                    .filter(job -> job.getId().equals(jobPostingId))
                    .findFirst()
                    .orElse(null);

            if (jobPosting != null) {
                // 해당 채용공고가 있으면 반환
                return jobPosting;
            }

            // 다음 페이지로 이동
            if (start * count >= Integer.parseInt(response.getJobs().getTotal())) {
                // 마지막 페이지에 도달하면 종료
                break;
            }
            start++; // 페이지 번호 증가
        }

        // 해당 ID로 채용공고를 찾을 수 없는 경우 예외 처리
        throw new RuntimeException("채용공고를 찾을 수 없습니다.");
    }
}