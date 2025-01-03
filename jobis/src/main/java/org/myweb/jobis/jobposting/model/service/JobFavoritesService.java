package org.myweb.jobis.jobposting.model.service;

import lombok.RequiredArgsConstructor;
import org.myweb.jobis.jobposting.jpa.repository.JobFavoritesRepository;
import org.myweb.jobis.jobposting.model.dto.JobFavorites;
import org.myweb.jobis.jobposting.model.dto.JobPostingResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobFavoritesService {

    @Value("${saramins.api.key}")
    private String apiKey;

    @Value("${saramins.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    // 즐겨찾기 추가
    public JobFavorites addFavorite(JobFavorites jobFavorites) {
        // 즐겨찾기 추가 로직 구현 (예: DB에 저장)
        return jobFavorites;
    }

    // 특정 사용자(UUID)의 즐겨찾기 목록 조회
    public List<JobFavorites> getFavorites(String uuid) {
        // 즐겨찾기 목록 조회 로직 구현 (예: DB에서 조회)
        return List.of(); // 예시: 빈 목록 반환
    }

    // 즐겨찾기 삭제
    public void removeFavorite(String uuid, String jobPostingId) {
        // 즐겨찾기 삭제 로직 구현 (예: DB에서 삭제)
    }

    // 즐겨찾기 여부 확인
    public boolean isFavorite(String uuid, String jobPostingId) {
        // 즐겨찾기 여부 확인 로직 구현 (예: DB에서 확인)
        return false; // 예시: 항상 false 반환
    }

    // 외부 사람인 API 호출하여 채용공고 목록 검색
    public JobPostingResponse searchJobPostings(String jobType, String locMcd, String eduLv, String jobCd,
                                                int count, int start, int total, String sort) {
        String fullUri = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("access-key", apiKey)
                .queryParam("job_type", jobType)
                .queryParam("loc_mcd", locMcd)
                .queryParam("edu_lv", eduLv)
                .queryParam("job_cd", jobCd)
                .queryParam("count", count)
                .queryParam("start", start)
                .queryParam("total", total)
                .queryParam("sort", sort)
                .toUriString();

        try {
            return restTemplate.getForObject(fullUri, JobPostingResponse.class);
        } catch (Exception e) {
            System.err.println("API 호출 실패: " + e.getMessage());
            throw new RuntimeException("API 호출 실패: " + e.getMessage(), e);
        }
    }
        public JobPostingResponse getJobPostingById(String jobPostingId) {
            RestTemplate restTemplate = new RestTemplate();

            // 요청 URL 생성
            String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                    .queryParam("access-key", apiKey)
                    .queryParam("id", jobPostingId)

                    .toUriString();

            // API 호출
            ResponseEntity<JobPostingResponse> response = restTemplate.getForEntity(url, JobPostingResponse.class);

            return response.getBody();
    }
}