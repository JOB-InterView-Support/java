package org.myweb.jobis.jobposting.jpa.repository;

import org.myweb.jobis.jobposting.jpa.entity.JobFavoritesEntity;

import java.util.List;

public interface JobFavoritesRepositoryCustom {
    // 커스텀 메서드 선언
    List<JobFavoritesEntity> findFavoritesByUserWithCustomConditions(String uuid);

    boolean checkIfFavoriteExists(String uuid, String jobPostingId);
}
