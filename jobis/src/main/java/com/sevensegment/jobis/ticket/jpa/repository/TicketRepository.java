package com.sevensegment.jobis.ticket.jpa.repository;


import com.sevensegment.jobis.payment.jpa.entity.PaymentEntity;
import com.sevensegment.jobis.ticket.jpa.entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<TicketEntity, String>{
    // 기본 JPA 메소드 사용 가능
    List<TicketEntity> findAllByUuidOrderByTicketStartDateDesc(String uuid);

    Optional<PaymentEntity> findByPaymentKey(String paymentKey);
    Optional<TicketEntity> findTicketByPaymentKey(String paymentKey);

    Optional<TicketEntity> findFirstByUuidOrderByTicketStartDateDesc(String uuid);

    // UUID로 Ticket 목록 조회
    List<TicketEntity> findByUuid(String uuid);

}
