package com.sevensegment.jobis.jobposting.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.sevensegment.jobis.jobposting.jpa.entity.JobFavoritesEntity;
import com.sevensegment.jobis.jobposting.jpa.repository.JobFavoritesRepository;
import com.sevensegment.jobis.jobposting.model.dto.JobFavorites;
import com.sevensegment.jobis.jobposting.model.dto.JobPosting;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class JobFavoritesService {
    private final JobFavoritesRepository jobFavoritesRepository;
    private final JobPostingService jobPostingService;

    // 즐겨찾기 추가
    public JobFavorites addFavorite(JobFavorites favorite) {
        // UUID 생성
        favorite.setJobFavoritesNo(UUID.randomUUID().toString());

        // 중복 체크
        Optional<JobFavoritesEntity> existing = jobFavoritesRepository
                .findByUuidAndJobPostingId(favorite.getUuid(), favorite.getJobPostingId());
        if (existing.isPresent()) {
            throw new RuntimeException("이미 즐겨찾기에 추가된 채용공고입니다.");
        }

        // 채용공고 정보 가져오기
        JobPosting jobPosting = jobPostingService.getJobPostingById(favorite.getJobPostingId());

        if (jobPosting == null) {
            throw new RuntimeException("채용공고 정보를 찾을 수 없습니다.");
        }

        // 즐겨찾기 저장 (JobPosting 정보를 함께 저장)
        JobFavoritesEntity savedEntity = jobFavoritesRepository.save(favorite.toEntity());

        // DTO 변환 시 JobPosting 포함
        return JobFavorites.fromEntity(savedEntity, jobPosting);  // JobPosting 정보를 포함하여 반환
    }

    // 즐겨찾기 목록 조회
    public List<JobFavorites> getFavorites(String uuid) {
        // 데이터 조회
        List<JobFavoritesEntity> entities = jobFavoritesRepository.searchFavorites(uuid);

        // 모든 jobPostingId를 리스트로 추출
        List<String> jobPostingIds = entities.stream()
                .map(JobFavoritesEntity::getJobPostingId)
                .collect(Collectors.toList());
        // 캐싱 로직 추가
        Map<String, JobPosting> jobPostingCache = new HashMap<>(); // 캐싱을 위한 맵 생성

        for (String id : jobPostingIds) {
            if (!jobPostingCache.containsKey(id)) { // 캐싱에 없는 경우만 API 호출
                try {
                    JobPosting posting = jobPostingService.getJobPostingById(id);
                    if (posting != null) {
                        jobPostingCache.put(id, posting); // 캐시에 저장
                    } else {
                        log.warn("채용공고 조회 실패: {}", id);
                    }
                } catch (Exception e) {
                    log.error("채용공고 조회 실패: {}", id, e);
                }
            }
        }

        // 즐겨찾기 목록 반환
        List<JobFavorites> jobFavoritesList = new ArrayList<>();
        for (JobFavoritesEntity entity : entities) {
            String jobPostingId = entity.getJobPostingId();
            JobPosting jobPosting = jobPostingCache.get(jobPostingId);  // 캐시에서 채용공고 조회

            if (jobPosting != null) {
                JobFavorites jobFavorites = new JobFavorites();
                jobFavorites.setJobPostingId(jobPosting.getId());
                jobFavorites.setTitle(jobPosting.getTitle());
                jobFavorites.setCompany(String.valueOf(jobPosting.getCompany()));
                jobFavorites.setIndustry(String.valueOf(jobPosting.getPosition().getIndustry()));
                jobFavorites.setLocation(jobPosting.getLocation());
                jobFavorites.setSalary(String.valueOf(jobPosting.getSalary()));

                jobFavoritesList.add(jobFavorites);
            }
        }

        return jobFavoritesList;
    }

    // 즐겨찾기 삭제
    public void removeFavorite(String uuid, String jobPostingId) {
        log.info("즐겨찾기 삭제 요청 - UUID: {}, JobPostingId: {}", uuid, jobPostingId);

        // 즐겨찾기 삭제 처리
        Optional<JobFavoritesEntity> existing = jobFavoritesRepository
                .findByUuidAndJobPostingId(uuid, jobPostingId);

        if (existing.isPresent()) {
            jobFavoritesRepository.delete(existing.get());
            log.info("즐겨찾기 삭제 완료 - UUID: {}, JobPostingId: {}", uuid, jobPostingId);
        } else {
            log.warn("삭제할 즐겨찾기 공고가 존재하지 않습니다. UUID: {}, JobPostingId: {}", uuid, jobPostingId);
            throw new RuntimeException("삭제할 즐겨찾기 공고가 존재하지 않습니다.");
        }
    }

    // 즐겨찾기 여부 확인
    public boolean isFavorite(String uuid, String jobPostingId) {
        log.info("즐겨찾기 여부 확인 - UUID: {}, JobPostingId: {}", uuid, jobPostingId);

        boolean exists = jobFavoritesRepository.findByUuidAndJobPostingId(uuid, jobPostingId).isPresent();
        log.info("즐겨찾기 여부: {}", exists);

        return exists;
    }
}
