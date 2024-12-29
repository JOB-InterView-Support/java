package org.myweb.jobis.jobposting.jpa.repository;

import org.myweb.jobis.jobposting.jpa.entity.JobFavoritesEntity;
import org.myweb.jobis.jobposting.jpa.repository.JobFavoritesRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobFavoritesRepository extends JpaRepository<JobFavoritesEntity, String>, JobFavoritesRepositoryCustom {
    List<JobFavoritesEntity> findByUuid(String uuid);
}
