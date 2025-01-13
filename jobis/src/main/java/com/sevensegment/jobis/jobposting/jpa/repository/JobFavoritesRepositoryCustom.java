package com.sevensegment.jobis.jobposting.jpa.repository;

import com.sevensegment.jobis.jobposting.jpa.entity.JobFavoritesEntity;

import java.util.List;

// JobFavoritesRepositoryCustom.java
public interface JobFavoritesRepositoryCustom {
    // UUID로 즐겨찾기 목록을 조회하는 메서드
    List<JobFavoritesEntity> searchFavorites(String uuid);
}