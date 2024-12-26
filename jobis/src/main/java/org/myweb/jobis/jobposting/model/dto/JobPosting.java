package org.myweb.jobis.jobposting.model.dto;

import lombok.Data;

@Data
public class JobPosting {
    private String keyword; // 검색 키워드
    private String indCd;   // 산업/업종 코드
    private String locCd;   // 지역 코드
    private String jobType; // 직무 코드
    private String eduLv;   // 학력 조건
    private int start = 0;  // 시작 페이지 (default 0)
    private int count = 10; // 결과 개수 (default 10)
    private String sort = "pd"; // 정렬 기준 (default pd)
}
