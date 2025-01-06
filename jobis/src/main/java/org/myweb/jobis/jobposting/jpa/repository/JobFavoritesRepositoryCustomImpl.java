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

    @Override
    public boolean existsByUuidAndJobPostingId(String uuid, String jobPostingId) {
        // JPQL을 사용해서 uuid와 jobPostingId로 즐겨찾기 중복 여부를 확인
        String jpql = "SELECT COUNT(jf) FROM JobFavoritesEntity jf WHERE jf.uuid = :uuid AND jf.jobPostingId = :jobPostingId";
        Long count = entityManager.createQuery(jpql, Long.class)
                .setParameter("uuid", uuid)
                .setParameter("jobPostingId", jobPostingId)
                .getSingleResult();

        return count > 0;
    }



}