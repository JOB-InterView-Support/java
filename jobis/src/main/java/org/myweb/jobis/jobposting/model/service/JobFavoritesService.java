package org.myweb.jobis.jobposting.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.jobposting.jpa.entity.JobFavoritesEntity;
import org.myweb.jobis.jobposting.jpa.repository.JobFavoritesRepository;
import org.myweb.jobis.jobposting.model.dto.JobFavorites;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobFavoritesService {

    private final JobFavoritesRepository jobFavoritesRepository;

    public JobFavorites addFavorite(String uuid, String jobPostingId) {
        if (jobFavoritesRepository.existsByUuidAndJobPostingId(uuid, jobPostingId)) {
            throw new IllegalArgumentException("이미 즐겨찾기에 추가된 공고입니다.");
        }

        String jobFavoritesNo = UUID.randomUUID().toString();
        JobFavoritesEntity entity = JobFavoritesEntity.fromDto(jobFavoritesNo, uuid, jobPostingId);
        jobFavoritesRepository.save(entity);

        return JobFavorites.builder()
                .jobFavoritesNo(entity.getJobFavoritesNo())
                .uuid(entity.getUuid())
                .jobPostingId(entity.getJobPostingId())
                .jobCreatedDate(entity.getJobCreatedDate())
                .build();
    }

    public List<JobFavorites> getFavoritesWithDetails(String uuid) {
        return jobFavoritesRepository.findByUuid(uuid).stream()
                .map(entity -> JobFavorites.builder()
                        .jobFavoritesNo(entity.getJobFavoritesNo())
                        .uuid(entity.getUuid())
                        .jobPostingId(entity.getJobPostingId())
                        .jobCreatedDate(entity.getJobCreatedDate())
                        .build())
                .toList();
    }

    public void removeFavorite(String jobFavoritesNo) {
        jobFavoritesRepository.deleteById(jobFavoritesNo);
    }
}
