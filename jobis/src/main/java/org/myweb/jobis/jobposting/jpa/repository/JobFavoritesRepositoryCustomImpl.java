package org.myweb.jobis.jobposting.jpa.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.myweb.jobis.jobposting.jpa.entity.JobFavoritesEntity;
import org.myweb.jobis.jobposting.jpa.entity.QJobFavoritesEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JobFavoritesRepositoryCustomImpl implements JobFavoritesRepositoryCustom {

    private final JPAQueryFactory queryFactory;  // QueryDSL을 위한 JPAQueryFactory
    private final EntityManager entityManager;   // JPQL을 위한 EntityManager

}
