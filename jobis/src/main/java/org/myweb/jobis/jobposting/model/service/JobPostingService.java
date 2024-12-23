package org.myweb.jobis.jobposting.model.service;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.jobposting.model.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class JobPostingService {

    // application.properties에서 URL을 주입받음
    @Value("${saramins.api.url}")
    private String apiUrl;

    @Value("${saramins.api.key}")
    private String apiKey;

    // 채용 공고 목록을 가져오는 메소드
    public ApiResponse getJobPostings(String keyword, String jobType, String location) {
        String url = buildApiUrl(keyword, jobType, location);
        RestTemplate restTemplate = new RestTemplate();

        // API 요청
        ResponseEntity<ApiResponse> response = restTemplate.exchange(url, HttpMethod.GET, null, ApiResponse.class);

        return response.getBody(); // ApiResponse 반환
    }

    // API URL을 구성하는 메소드
    private String buildApiUrl(String keyword, String jobType, String location) {
        StringBuilder urlBuilder = new StringBuilder(apiUrl);
        urlBuilder.append("?access-key=").append(apiKey);
        urlBuilder.append("&keywords=").append(keyword); // 키워드 검색

        if (jobType != null) urlBuilder.append("&jobType=").append(jobType); // 직무 종류
        if (location != null) urlBuilder.append("&location=").append(location); // 근무지
        urlBuilder.append("&count=10"); // 페이지당 결과 수

        return urlBuilder.toString();
    }
}