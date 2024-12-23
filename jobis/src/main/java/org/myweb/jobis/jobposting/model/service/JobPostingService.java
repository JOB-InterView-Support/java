package org.myweb.jobis.jobposting.model.service;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.jobposting.model.dto.JobPosting;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class JobPostingService {

//    private final RestTemplate restTemplate = new RestTemplate();
//
//    public List<JobPosting> searchJobPostings(String apiKey, String keywords, String locCode, int start) {
//        String apiUrl = "https://oapi.saramin.co.kr/job-search?access-key=" + apiKey;
//
//        // 검색 조건 추가
//        if (keywords != null && !keywords.isEmpty()) {
//            apiUrl += "&keywords=" + keywords;
//        }
//        if (locCode != null && !locCode.isEmpty()) {
//            apiUrl += "&loc_cd=" + locCode;
//        }
//        apiUrl += "&start=" + start;
//
//        // API 호출
//        ResponseEntity<JobPosting[]> response = restTemplate.getForEntity(apiUrl, JobPosting[].class);
//
//        if (response.getBody() != null) {
//            return List.of(response.getBody());
//        } else {
//            return new ArrayList<>();
//        }
//    }
}
