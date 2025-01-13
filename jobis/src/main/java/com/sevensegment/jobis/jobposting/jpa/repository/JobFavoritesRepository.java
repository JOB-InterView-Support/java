package com.sevensegment.jobis.jobposting.jpa.repository;

import com.sevensegment.jobis.jobposting.jpa.entity.JobFavoritesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// JobFavoritesRepository.java
@Repository
public interface JobFavoritesRepository extends JpaRepository<JobFavoritesEntity, String>, JobFavoritesRepositoryCustom {
    Optional<JobFavoritesEntity> findByUuidAndJobPostingId(String uuid, String jobPostingId);
}