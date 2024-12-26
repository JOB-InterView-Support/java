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
    private String apiKey;

    @Value("${saramins.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public Object searchJobPostings(String indCd, String locCd, String eduLv, String jobType,
                                    String count, String start, String sort) {

        // URI 생성
        String fullUri = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("access-key", apiKey)
                .queryParam("ind_cd", indCd)
                .queryParam("loc_cd", locCd)
                .queryParam("edu_lv", eduLv)
                .queryParam("job_cd", jobType)
                .queryParam("count", count)
                .queryParam("start", start)
                .queryParam("sort", sort)
                .toUriString();

        // API 호출
        return restTemplate.getForObject(fullUri, Object.class);
    }
}
