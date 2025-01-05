package org.myweb.jobis.payment.jpa.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.myweb.jobis.payment.jpa.entity.PaymentEntity;
import org.myweb.jobis.payment.jpa.entity.QPaymentEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryCustomImpl implements PaymentRepositoryCustom {

    private final JPAQueryFactory queryFactory;  // QueryDSL을 위한 JPAQueryFactory
    private final EntityManager entityManager;   // JPQL을 위한 EntityManager
}
