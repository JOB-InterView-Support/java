package org.myweb.jobis.jobposting.model.service;

import org.myweb.jobis.jobposting.jpa.entity.JobFavoritesEntity;
import org.myweb.jobis.jobposting.jpa.repository.JobFavoritesRepository;
import org.myweb.jobis.jobposting.model.dto.JobFavorites;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobFavoritesService {

    private final JobFavoritesRepository jobFavoritesRepository;

    public JobFavoritesService(JobFavoritesRepository jobFavoritesRepository) {
        this.jobFavoritesRepository = jobFavoritesRepository;
    }

    // 즐겨찾기 추가
    public JobFavorites addFavorite(JobFavorites jobFavorites) {
        JobFavoritesEntity entity = jobFavorites.toEntity();
        JobFavoritesEntity savedEntity = jobFavoritesRepository.save(entity);
        return savedEntity.toDto();
    }

    // 즐겨찾기 삭제
    public void removeFavorite(String jobFavoritesNo) {
        jobFavoritesRepository.deleteById(jobFavoritesNo);
    }

    // 즐겨찾기 조회
    public List<JobFavorites> listFavorites(String uuid) {
        List<JobFavoritesEntity> entities = jobFavoritesRepository.findByUuid(uuid);
        return entities.stream()
                .map(JobFavoritesEntity::toDto)
                .collect(Collectors.toList());
    }
}
