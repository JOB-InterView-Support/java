package org.myweb.jobis.jobposting.jpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.myweb.jobis.jobposting.jpa.entity.JobFavoritesEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JobFavoritesRepositoryCustomImpl implements JobFavoritesRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    // 사용자 ID(UUID)로 즐겨찾기 목록 조회
    @Override
    public List<JobFavoritesEntity> findFavoritesByUserWithCustomConditions(String uuid) {
        String jpql = "SELECT j FROM JobFavoritesEntity j WHERE j.uuid = :uuid";
        TypedQuery<JobFavoritesEntity> query = entityManager.createQuery(jpql, JobFavoritesEntity.class);
        query.setParameter("uuid", uuid);
        return query.getResultList();
    }

    // 즐겨찾기 여부 확인
    @Override
    public boolean checkIfFavoriteExists(String uuid, String jobPostingId) {
        String jpql = "SELECT COUNT(j) FROM JobFavoritesEntity j WHERE j.uuid = :uuid AND j.jobPostingId = :jobPostingId";
        TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
        query.setParameter("uuid", uuid);
        query.setParameter("jobPostingId", jobPostingId);
        Long count = query.getSingleResult();
        return count > 0; // 즐겨찾기 여부 반환
    }



}