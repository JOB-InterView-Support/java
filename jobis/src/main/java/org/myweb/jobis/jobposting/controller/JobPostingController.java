package org.myweb.jobis.jobposting.controller;

import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.jobposting.model.dto.JobPosting;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/jobpostings")
public class JobPostingController {

    @Value("${saramins.api.key}")
    private String apiKey;

    @Value("${saramins.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    public JobPostingController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping("/search")
    public ResponseEntity<Object> searchJobPostings(@RequestBody JobPosting searchFilters) {
        // 사람인 API의 요청 URI
        String uri = apiUrl; // application.properties에서 주입받은 URL 사용

        // 검색 필터로 전달받은 파라미터들을 Map에 담아서 쿼리 스트링 생성
        Map<String, String> params = Map.of(
                "access-key", apiKey, // application.properties에서 주입받은 API 키 사용
                "ind_cd", searchFilters.getIndCd(),
                "loc_cd", searchFilters.getLocCd(),
                "edu_lv", searchFilters.getEduLv(),
                "job_cd", searchFilters.getJobType(),
                "count", "10",  // 기본 결과 수, 필요에 따라 조정 가능
                "start", "0",    // 기본 페이지 번호 (여기서는 0으로 시작)
                "sort", "pd"     // 게시일 역순 정렬 (기본값)
        );

        // 사람인 API로 GET 요청 전송
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String fullUri = uri + "?" + params.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .reduce((a, b) -> a + "&" + b)
                .orElse("");

        ResponseEntity<Object> response = restTemplate.exchange(
                fullUri, HttpMethod.GET, entity, Object.class
        );

        // 응답 데이터 반환
        return ResponseEntity.ok(response.getBody());
    }
}