package org.myweb.jobis.jobposting.jpa.repository;

import org.myweb.jobis.jobposting.jpa.entity.JobFavoritesEntity;

import java.util.List;

public interface JobFavoritesRepositoryCustom {

    // 사용자별 즐겨찾기 목록을 가져오는 커스텀 메서드 (예: 페이징 처리 등)
    List<JobFavoritesEntity> findFavoritesByUserWithCustomConditions(String uuid);

    // 특정 사용자가 해당 채용공고를 즐겨찾기 했는지 확인하는 커스텀 메서드 (다양한 조건으로 체크 가능)
    boolean existsByUuidAndJobPostingId(String uuid, String jobPostingId);
}