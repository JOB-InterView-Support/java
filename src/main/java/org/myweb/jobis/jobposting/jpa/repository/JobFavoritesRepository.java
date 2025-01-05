package org.myweb.jobis.jobposting.jpa.repository;

import org.myweb.jobis.jobposting.jpa.entity.JobFavoritesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobFavoritesRepository extends JpaRepository<JobFavoritesEntity, String>, JobFavoritesRepositoryCustom {

    // 특정 사용자(UUID)의 즐겨찾기 목록을 조회
    List<JobFavoritesEntity> findByUuid(String uuid);

    // 특정 사용자가 해당 채용공고를 즐겨찾기 했는지 확인
    Optional<JobFavoritesEntity> findByUuidAndJobPostingId(String uuid, String jobPostingId);

    boolean existsByUuidAndJobPostingId(String uuid, String jobPostingId);
}