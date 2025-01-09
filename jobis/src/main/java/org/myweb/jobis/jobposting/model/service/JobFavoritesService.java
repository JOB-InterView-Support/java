package org.myweb.jobis.jobposting.model.service;

import org.myweb.jobis.jobposting.jpa.entity.JobFavoritesEntity;
import org.myweb.jobis.jobposting.jpa.repository.JobFavoritesRepository;
import org.myweb.jobis.jobposting.model.dto.JobFavoriteRequest;
import org.myweb.jobis.jobposting.model.dto.JobFavoriteResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobFavoritesService {

    private final JobFavoritesRepository jobFavoritesRepository;

    public JobFavoritesService(JobFavoritesRepository jobFavoritesRepository) {
        this.jobFavoritesRepository = jobFavoritesRepository;
    }

    public void addFavorite(JobFavoriteRequest request) {
        JobFavoritesEntity entity = JobFavoritesEntity.builder()
                .uuid(request.getUuid())
                .jobPostingId(request.getJobPostingId())
                .build();
        jobFavoritesRepository.save(entity);
    }

    public List<JobFavoriteResponse> getFavorites(String uuid) {
        return jobFavoritesRepository.findByUuid(uuid).stream()
                .map(JobFavoritesEntity::toDto)
                .collect(Collectors.toList());
    }

    public void deleteFavorite(JobFavoriteRequest request) {
        jobFavoritesRepository.deleteByUuidAndJobPostingId(request.getUuid(), request.getJobPostingId());
    }

    public boolean isFavorite(String uuid, String jobPostingId) {
        return jobFavoritesRepository.existsByUuidAndJobPostingId(uuid, jobPostingId);
    }
}
