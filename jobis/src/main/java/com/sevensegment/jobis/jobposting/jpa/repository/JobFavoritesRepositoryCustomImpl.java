package com.sevensegment.jobis.jobposting.jpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import com.sevensegment.jobis.jobposting.jpa.entity.JobFavoritesEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JobFavoritesRepositoryCustomImpl implements JobFavoritesRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<JobFavoritesEntity> searchFavorites(String uuid) {
        // JPQL을 사용하여 uuid로 즐겨찾기 목록을 조회하는 예제
        String jpql = "SELECT jf FROM JobFavoritesEntity jf WHERE jf.uuid = :uuid";
        TypedQuery<JobFavoritesEntity> query = entityManager.createQuery(jpql, JobFavoritesEntity.class);
        query.setParameter("uuid", uuid);

        return query.getResultList();
    }
}
