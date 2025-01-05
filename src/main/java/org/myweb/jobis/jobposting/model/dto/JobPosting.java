package org.myweb.jobis.jobposting.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class JobPosting {
    private String id;  // 공고 번호
    private String jobTitle;  // 공고 제목
    private String companyName;  // 회사명
    private String location;  // 근무지
    private String jobType;  // 근무 형태
    private String experienceLevel;  // 경력
    private String requiredEducationLevel;  // 학력
    private String postingDate;  // 게시일
    private String expirationDate;  // 마감일
    private String salary;  // 연봉
    private String jobUrl;  // 공고 URL
    private String title;       // 채용공고 제목
    private String description; // 채용공고 설명

    // 추가 필드 (예시로 사람인 API의 일부를 가정한 항목들)
    private String companyUrl;  // 회사 URL
    private String contactNumber;  // 연락처
    private String workType;  // 근무 형태 (정규직, 계약직 등)
    private String jobCategory;  // 직무 카테고리
    private String employmentType;  // 고용 형태
}