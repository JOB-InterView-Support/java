package org.myweb.jobis.jobposting.model.service;

import lombok.RequiredArgsConstructor;
import org.myweb.jobis.jobposting.jpa.entity.JobFavoritesEntity;
import org.myweb.jobis.jobposting.jpa.repository.JobFavoritesRepository;
import org.myweb.jobis.jobposting.model.dto.JobFavorites;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobFavoritesService {

    private final JobFavoritesRepository jobFavoritesRepository;

    public void addFavorite(JobFavorites jobFavorites) {
        boolean exists = jobFavoritesRepository.existsFavoriteByUuidAndJobPostingId(
                jobFavorites.getUuid(), jobFavorites.getJobPostingId()
        );
        if (exists) {
            throw new IllegalStateException("이미 즐겨찾기에 추가된 공고입니다.");
        }
        JobFavoritesEntity entity = JobFavoritesEntity.fromDto(jobFavorites);
        jobFavoritesRepository.save(entity);
    }

    public void removeFavorite(String uuid, String jobPostingId) {
        JobFavoritesEntity entity = jobFavoritesRepository.findFavoriteByUuidAndJobPostingId(uuid, jobPostingId);
        jobFavoritesRepository.delete(entity);
    }

    public List<JobFavorites> getFavorites(String uuid) {
        return jobFavoritesRepository.findFavoritesByUuid(uuid)
                .stream()
                .map(JobFavoritesEntity::toDto)
                .collect(Collectors.toList());
    }
}
