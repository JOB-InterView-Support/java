package org.myweb.jobis.jobposting.jpa.repository;

import org.myweb.jobis.jobposting.jpa.entity.JobFavoritesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobFavoritesRepository extends JpaRepository<JobFavoritesEntity, Long>, JobFavoritesRepositoryCustom {
    // 기본 Repository 메서드 + Custom 인터페이스 확장
}
