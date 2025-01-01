package org.myweb.jobis.jobposting.model.service;

import lombok.RequiredArgsConstructor;
import org.myweb.jobis.jobposting.jpa.entity.JobFavoritesEntity;
import org.myweb.jobis.jobposting.jpa.repository.JobFavoritesRepository;
import org.myweb.jobis.jobposting.model.dto.JobFavorites;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JobFavoritesService {

    private final JobFavoritesRepository jobFavoritesRepository;

    // 즐겨찾기 추가
    @Transactional
    public JobFavorites addFavorite(JobFavorites jobFavorites) {
        // Check if the favorite already exists
        Optional<JobFavoritesEntity> existingFavorite = jobFavoritesRepository.findByUuidAndJobPostingId(jobFavorites.getUuid(), jobFavorites.getJobPostingId());

        if (existingFavorite.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This job posting is already in the favorites list.");
        }

        JobFavoritesEntity jobFavoritesEntity = jobFavorites.toEntity();
        jobFavoritesRepository.save(jobFavoritesEntity);

        return jobFavoritesEntity.toDto();  // 변환 후 반환
    }

    // 특정 사용자(UUID)의 즐겨찾기 목록 조회
    public List<JobFavorites> getFavorites(String uuid) {
        List<JobFavoritesEntity> favorites = jobFavoritesRepository.findByUuid(uuid);

        return favorites.stream()
                .map(JobFavoritesEntity::toDto)  // Convert entities to DTOs
                .toList();
    }

    // 즐겨찾기 삭제
    @Transactional
    public void removeFavorite(String uuid, String jobPostingId) {
        // Find the favorite record
        JobFavoritesEntity favoriteEntity = jobFavoritesRepository.findByUuidAndJobPostingId(uuid, jobPostingId)
                .orElseThrow(() -> new RuntimeException("Favorite not found for the given job posting."));

        // Delete the favorite record
        jobFavoritesRepository.delete(favoriteEntity);
    }

    // 즐겨찾기 여부 확인
    public boolean isFavorite(String uuid, String jobPostingId) {
        return jobFavoritesRepository.existsByUuidAndJobPostingId(uuid, jobPostingId);
    }
}