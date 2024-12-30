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

    @Override
    public List<JobFavoritesEntity> findFavoritesByUuid(String uuid) {
        String jpql = "SELECT jf FROM JobFavoritesEntity jf WHERE jf.uuid = :uuid";
        TypedQuery<JobFavoritesEntity> query = entityManager.createQuery(jpql, JobFavoritesEntity.class);
        query.setParameter("uuid", uuid);
        return query.getResultList();
    }

    @Override
    public JobFavoritesEntity findFavoriteByUuidAndJobPostingId(String uuid, String jobPostingId) {
        String jpql = "SELECT jf FROM JobFavoritesEntity jf WHERE jf.uuid = :uuid AND jf.jobPostingId = :jobPostingId";
        TypedQuery<JobFavoritesEntity> query = entityManager.createQuery(jpql, JobFavoritesEntity.class);
        query.setParameter("uuid", uuid);
        query.setParameter("jobPostingId", jobPostingId);

        // 단일 결과 반환 (결과가 없거나 여러 개일 경우 예외 처리 가능)
        return query.getSingleResult();
    }

    @Override
    public boolean existsFavoriteByUuidAndJobPostingId(String uuid, String jobPostingId) {
        String jpql = "SELECT COUNT(jf) FROM JobFavoritesEntity jf WHERE jf.uuid = :uuid AND jf.jobPostingId = :jobPostingId";
        TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
        query.setParameter("uuid", uuid);
        query.setParameter("jobPostingId", jobPostingId);

        // 0보다 큰 결과가 있으면 true
        return query.getSingleResult() > 0;
    }
}
