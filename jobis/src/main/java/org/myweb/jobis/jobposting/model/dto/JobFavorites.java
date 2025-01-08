package org.myweb.jobis.jobposting.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobFavorites {
    private String jobFavoritesNo; // 즐겨찾기 ID
    private String uuid; // 사용자 UUID
    private String jobPostingId; // 사람인 공고 ID
    private LocalDateTime jobCreatedDate; // 즐겨찾기 추가 날짜

    // 추가 필드
    private String title; // 공고 제목
    private String companyName; // 회사 이름
    private String location; // 지역 정보
}
