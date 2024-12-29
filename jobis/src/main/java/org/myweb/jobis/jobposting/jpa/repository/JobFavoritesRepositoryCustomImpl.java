package org.myweb.jobis.jobposting.jpa.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.myweb.jobis.jobposting.jpa.entity.JobFavoritesEntity;
import org.myweb.jobis.jobposting.jpa.repository.JobFavoritesRepositoryCustom;


import java.util.List;

public class JobFavoritesRepositoryCustomImpl implements JobFavoritesRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager; // JPQL 용

    public JobFavoritesRepositoryCustomImpl(JPAQueryFactory queryFactory, EntityManager entityManager) {
        this.queryFactory = queryFactory;
        this.entityManager = entityManager;
    }

    // 사용자 정의 쿼리 메서드 예시
    public List<JobFavoritesEntity> customQueryExample() {
        return entityManager.createQuery("SELECT j FROM JobFavoritesEntity j", JobFavoritesEntity.class)
                .getResultList();
    }
}
