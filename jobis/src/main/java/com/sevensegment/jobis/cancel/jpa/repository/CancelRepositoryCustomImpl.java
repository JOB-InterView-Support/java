package com.sevensegment.jobis.cancel.jpa.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CancelRepositoryCustomImpl implements CancelRepositoryCustom {

    private final JPAQueryFactory queryFactory;  // QueryDSL을 위한 JPAQueryFactory
    private final EntityManager entityManager;   // JPQL을 위한 EntityManager
}
