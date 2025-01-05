package org.myweb.jobis.jobposting.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.jobposting.jpa.entity.JobFavoritesEntity;
import org.myweb.jobis.jobposting.jpa.repository.JobFavoritesRepository;
import org.myweb.jobis.jobposting.model.dto.JobFavorites;
import org.myweb.jobis.jobposting.model.dto.JobPostingResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobFavoritesService {

    @Value("${saramins.api.key}")
    private String apiKey;

    @Value("${saramins.api.url}")
    private String apiUrl;

    private final JobFavoritesRepository jobFavoritesRepository;
    private final JobPostingService jobPostingService;

    @Transactional
    public JobFavorites addFavorite(JobFavorites jobFavorites) {
        jobFavorites.setJobFavoritesNo(UUID.randomUUID().toString());
        jobFavorites.setJobCreatedDate(LocalDateTime.now());

        JobFavoritesEntity entity = JobFavorites.toEntity(jobFavorites);
        JobFavoritesEntity savedEntity = jobFavoritesRepository.save(entity);

        return JobFavorites.toDto(savedEntity, null);
    }

    @Transactional
    public void deleteFavorite(String uuid, String jobFavoritesNo) {
        jobFavoritesRepository.deleteByUuidAndJobFavoritesNo(uuid, jobFavoritesNo);
    }

    @Transactional(readOnly = true)
    public List<JobFavorites> getFavorites(String uuid) {
        // UUID로 즐겨찾기 목록을 찾는다.
        List<JobFavoritesEntity> entities = jobFavoritesRepository.findByUuid(uuid);

        // 각 즐겨찾기 항목에 대해 공고 정보를 호출하여 리스트로 반환
        return entities.stream()
                .map(entity -> {
                    try {
                        // 사람인 API를 통해 채용공고 상세 정보 조회
                        JobPostingResponse jobPostingResponse = jobPostingService.getJobPostingDetail(entity.getJobPostingId());
                        JobPostingResponse.Job job = jobPostingResponse.getJobs().getJob().get(0); // 첫 번째 Job 정보

                        // JobPostingResponse.Job 정보를 이용해 JobFavorites DTO 생성
                        return JobFavorites.toDto(entity, job);
                    } catch (Exception e) {
                        e.printStackTrace();
                        // 예외가 발생하면 null 또는 기본값을 반환 (유효성 검사 필요)
                        return JobFavorites.toDto(entity, null); // 기본값으로 null 처리
                    }
                })
                .collect(Collectors.toList());
    }
}