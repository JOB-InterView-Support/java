package org.myweb.jobis.jobposting.jpa.repository;

import org.myweb.jobis.jobposting.jpa.entity.JobFavoritesEntity;
import org.myweb.jobis.jobposting.model.dto.JobFavorites;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobFavoritesRepository extends JpaRepository<JobFavoritesEntity, String>, JobFavoritesRepositoryCustom {
    List<JobFavoritesEntity> findByUuid(String uuid);
    void deleteByUuidAndJobFavoritesNo(String uuid, String jobFavoritesNo);

}