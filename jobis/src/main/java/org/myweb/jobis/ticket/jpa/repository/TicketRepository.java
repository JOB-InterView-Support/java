package org.myweb.jobis.ticket.jpa.repository;

import org.myweb.jobis.jobposting.jpa.entity.JobFavoritesEntity;
import org.myweb.jobis.ticket.jpa.entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<TicketEntity, String>, TicketRepositoryCustom {
    // 기본 JPA 메소드 사용 가능
}
