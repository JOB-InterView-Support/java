package org.myweb.jobis.cancel.jpa.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.myweb.jobis.cancel.jpa.entity.CancelEntity;
import org.myweb.jobis.cancel.jpa.entity.QCancelEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CancelRepositoryCustomImpl implements CancelRepositoryCustom {

    private final JPAQueryFactory queryFactory;  // QueryDSL을 위한 JPAQueryFactory
    private final EntityManager entityManager;   // JPQL을 위한 EntityManager
}
