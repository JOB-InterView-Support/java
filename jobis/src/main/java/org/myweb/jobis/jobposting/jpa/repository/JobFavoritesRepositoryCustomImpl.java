package org.myweb.jobis.jobposting.jpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.myweb.jobis.jobposting.jpa.entity.JobFavoritesEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JobFavoritesRepositoryCustomImpl implements JobFavoritesRepositoryCustom {

    private final EntityManager entityManager;


    @Override
    public List<JobFavoritesEntity> findFavoritesByUserWithCustomConditions(String uuid) {
        // JPQL 쿼리 작성
        String jpql = "SELECT j FROM JobFavoritesEntity j WHERE j.uuid = :uuid";
        TypedQuery<JobFavoritesEntity> query = entityManager.createQuery(jpql, JobFavoritesEntity.class);
        query.setParameter("uuid", uuid);
        return query.getResultList();
    }

    @Override
    public boolean checkIfFavoriteExists(String uuid, String jobPostingId) {
        // JPQL 쿼리 작성
        String jpql = "SELECT COUNT(j) > 0 FROM JobFavoritesEntity j WHERE j.uuid = :uuid AND j.jobPostingId = :jobPostingId";
        TypedQuery<Boolean> query = entityManager.createQuery(jpql, Boolean.class);
        query.setParameter("uuid", uuid);
        query.setParameter("jobPostingId", jobPostingId);
        return query.getSingleResult();
    }
}
