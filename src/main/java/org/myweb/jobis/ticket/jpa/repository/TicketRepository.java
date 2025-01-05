package org.myweb.jobis.ticket.jpa.repository;


import org.myweb.jobis.ticket.jpa.entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<TicketEntity, String>, TicketRepositoryCustom {
    // 기본 JPA 메소드 사용 가능
    List<TicketEntity> findAllByUuidOrderByTicketStartDateDesc(String uuid);
}
