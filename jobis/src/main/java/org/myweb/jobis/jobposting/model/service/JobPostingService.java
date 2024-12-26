package org.myweb.jobis.jobposting.model.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class JobPostingService {

    @Value("${saramins.api.key}")
    private String apiKey;  // 사람인 API 키

    @Value("${saramins.api.url}")
    private String apiUrl;  // 사람인 API 기본 URL

    private final RestTemplate restTemplate = new RestTemplate();  // RestTemplate을 사용하여 HTTP 요청을 보냄

    public Object searchJobPostings(String indCd, String locCd, String eduLv, String jobType,
                                    int count, int start, String sort) {

        // URI 생성 (UriComponentsBuilder를 사용하여 API URL을 구성)
        String fullUri = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("access-key", apiKey)  // API 키 추가
                .queryParam("ind_cd", indCd)       // 산업 코드
                .queryParam("loc_cd", locCd)       // 지역 코드
                .queryParam("edu_lv", eduLv)       // 학력 수준
                .queryParam("job_cd", jobType)     // 직무 코드
                .queryParam("count", count)        // 한 페이지에 출력할 공고 수
                .queryParam("start", start)        // 페이지 번호
                .queryParam("sort", sort)          // 정렬 기준
                .toUriString();                    // URI 문자열로 변환

        try {
            // RestTemplate을 사용하여 API 호출
            Object response = restTemplate.getForObject(fullUri, Object.class);

            if (response == null) {
                throw new IllegalArgumentException("API 응답이 null입니다.");
            }

            return response;  // API 응답을 그대로 반환 (페이징 정보 포함)
        } catch (Exception e) {
            throw new RuntimeException("API 호출 중 오류 발생: " + e.getMessage(), e);  // 예외 처리
        }
    }
}
