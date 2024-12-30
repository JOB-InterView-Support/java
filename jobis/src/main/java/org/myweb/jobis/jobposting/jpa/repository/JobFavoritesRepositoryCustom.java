package org.myweb.jobis.jobposting.jpa.repository;

import org.myweb.jobis.jobposting.jpa.entity.JobFavoritesEntity;

import java.util.List;

public interface JobFavoritesRepositoryCustom {
    // UUID로 즐겨찾기 조회 (JPQL)
    List<JobFavoritesEntity> findFavoritesByUuid(String uuid);

    // UUID와 공고 ID로 즐겨찾기 조회 (JPQL)
    JobFavoritesEntity findFavoriteByUuidAndJobPostingId(String uuid, String jobPostingId);

    // UUID와 공고 ID 중복 여부 확인 (JPQL)
    boolean existsFavoriteByUuidAndJobPostingId(String uuid, String jobPostingId);
}
