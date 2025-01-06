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
import java.util.Optional;
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

    // 즐겨찾기 추가
    @Transactional
    public JobFavorites addFavorite(JobFavorites jobFavorites) {
        // 즐겨찾기 중복 체크
        if (jobFavoritesRepository.existsByUuidAndJobPostingId(jobFavorites.getUuid(), jobFavorites.getJobPostingId())) {
            // 중복된 즐겨찾기 존재 시 예외 처리
            throw new IllegalArgumentException("This favorite already exists.");
        }

        // 새 즐겨찾기 추가
        jobFavorites.setJobFavoritesNo(UUID.randomUUID().toString()); // 새 즐겨찾기 번호 생성
        jobFavorites.setJobCreatedDate(LocalDateTime.now()); // 현재 시간으로 생성 일자 설정

        JobFavoritesEntity entity = JobFavorites.toEntity(jobFavorites); // DTO -> Entity 변환
        JobFavoritesEntity savedEntity = jobFavoritesRepository.save(entity); // DB에 저장

        return JobFavorites.toDto(savedEntity, null); // Entity -> DTO 변환하여 반환
    }

    // 즐겨찾기 삭제
    @Transactional
    public void deleteFavorite(String uuid, String jobFavoritesNo) {
        Optional<JobFavoritesEntity> entityOptional = jobFavoritesRepository.findById(jobFavoritesNo);

        if (entityOptional.isEmpty()) {
            throw new RuntimeException("Favorite not found.");
        }

        JobFavoritesEntity entity = entityOptional.get();
        if (!entity.getUuid().equals(uuid)) {
            throw new RuntimeException("This favorite does not belong to the user.");
        }

        jobFavoritesRepository.delete(entity);
    }

    // 즐겨찾기 목록 조회
    public List<JobFavorites> getFavorites(String uuid) {
        List<JobFavoritesEntity> entityList = jobFavoritesRepository.findByUuid(uuid);
        return entityList.stream()
                .map(JobFavorites::toDto)
                .collect(Collectors.toList());
    }
}