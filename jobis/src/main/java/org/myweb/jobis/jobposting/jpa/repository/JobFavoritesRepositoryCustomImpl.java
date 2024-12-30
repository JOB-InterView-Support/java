package org.myweb.jobis.jobposting.jpa.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.myweb.jobis.jobposting.jpa.entity.JobFavoritesEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JobFavoritesRepositoryCustomImpl implements JobFavoritesRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager; // JPQL 용

    public JobFavoritesRepositoryCustomImpl(JPAQueryFactory queryFactory, EntityManager entityManager) {
        this.queryFactory = queryFactory;
        this.entityManager = entityManager;
    }

    // 시퀀스를 사용하여 job_favorites_no 값을 생성하는 메서드
    @Override
    public String getNextJobFavoritesNo() {
        // Oracle 시퀀스를 사용한 값 생성 쿼리
        String jpql = "SELECT job_favorites_seq.NEXTVAL FROM dual";
        return (String) entityManager.createNativeQuery(jpql).getSingleResult();
    }

    @Override
    public List<JobFavoritesEntity> findFavoritesByUuid(String uuid) {
        // JPQL을 사용하여 즐겨찾기 리스트를 조회하는 쿼리
        String jpql = "SELECT j FROM JobFavoritesEntity j WHERE j.uuid = :uuid";
        return entityManager.createQuery(jpql, JobFavoritesEntity.class)
                .setParameter("uuid", uuid)
                .getResultList();
    }

    @Override
    public JobFavoritesEntity findFavoriteByUuidAndJobPostingId(String uuid, String jobPostingId) {
        // JPQL을 사용하여 특정 즐겨찾기 조회
        String jpql = "SELECT j FROM JobFavoritesEntity j WHERE j.uuid = :uuid AND j.jobPostingId = :jobPostingId";
        return entityManager.createQuery(jpql, JobFavoritesEntity.class)
                .setParameter("uuid", uuid)
                .setParameter("jobPostingId", jobPostingId)
                .getSingleResult();
    }

    @Override
    public boolean existsFavoriteByUuidAndJobPostingId(String uuid, String jobPostingId) {
        // JPQL을 사용하여 UUID와 공고 ID의 중복 여부를 확인
        String jpql = "SELECT COUNT(j) FROM JobFavoritesEntity j WHERE j.uuid = :uuid AND j.jobPostingId = :jobPostingId";
        long count = (long) entityManager.createQuery(jpql)
                .setParameter("uuid", uuid)
                .setParameter("jobPostingId", jobPostingId)
                .getSingleResult();
        return count > 0;
    }
}
