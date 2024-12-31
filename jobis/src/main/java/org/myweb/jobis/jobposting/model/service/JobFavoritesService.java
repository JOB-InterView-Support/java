package org.myweb.jobis.jobposting.model.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.jobposting.jpa.entity.JobFavoritesEntity;
import org.myweb.jobis.jobposting.jpa.repository.JobFavoritesRepository;
import org.myweb.jobis.jobposting.model.dto.JobFavorites;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class JobFavoritesService {
    private final JobFavoritesRepository jobFavoritesRepository;

    // 즐겨찾기 추가
    public JobFavorites addFavorite(JobFavorites favorites) {

        // UUID 생성
        favorites.setJobFavoritesNo(UUID.randomUUID().toString());

        // 중복 체크
        Optional<JobFavoritesEntity> existing = jobFavoritesRepository
                .findByUuidAndJobPostingId(favorites.getUuid(), favorites.getJobPostingId());
        if (existing.isPresent()) {
            throw new RuntimeException("이미 즐겨찾기에 추가된 채용공고입니다.");
        }

        // 데이터 저장
        JobFavoritesEntity savedEntity = jobFavoritesRepository.save(favorites.toEntity());

        return savedEntity.toDto();
    }

    // 즐겨찾기 목록 조회
    public List<JobFavorites> getFavorites(String uuid) {

        // 데이터 조회
        List<JobFavoritesEntity> entities = jobFavoritesRepository.searchFavorites(uuid);

        // DTO 변환 후 반환
        return entities.stream()
                .map(JobFavoritesEntity::toDto)
                .collect(Collectors.toList());
    }

    // 즐겨찾기 삭제
    public void removeFavorite(String uuid, String jobPostingId) {
        // 즐겨찾기 삭제 처리
        Optional<JobFavoritesEntity> existing = jobFavoritesRepository
                .findByUuidAndJobPostingId(uuid, jobPostingId);

        if (existing.isPresent()) {
            jobFavoritesRepository.delete(existing.get());
        } else {
            throw new RuntimeException("삭제할 즐겨찾기 공고가 존재하지 않습니다.");
        }
    }

    // 즐겨찾기 여부 확인
    public boolean isFavorite(String uuid, String jobPostingId) {

        boolean exists = jobFavoritesRepository.findByUuidAndJobPostingId(uuid, jobPostingId).isPresent();

        return exists;
    }
}
