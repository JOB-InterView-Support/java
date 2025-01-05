package org.myweb.jobis.ticket.jpa.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.myweb.jobis.ticket.jpa.entity.TicketEntity;
import org.myweb.jobis.ticket.jpa.entity.QTicketEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TicketRepositoryCustomImpl implements TicketRepositoryCustom {

    private final JPAQueryFactory queryFactory;  // QueryDSL을 위한 JPAQueryFactory
    private final EntityManager entityManager;   // JPQL을 위한 EntityManager
}
