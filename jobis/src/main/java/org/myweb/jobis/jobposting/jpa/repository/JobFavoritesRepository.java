package org.myweb.jobis.jobposting.jpa.repository;

import org.myweb.jobis.jobposting.jpa.entity.JobFavoritesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobFavoritesRepository extends JpaRepository<JobFavoritesEntity, String>, JobFavoritesRepositoryCustom {
    // 기본 JPA 메소드 사용 가능
}
