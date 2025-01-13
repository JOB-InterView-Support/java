package com.sevensegment.jobis.ticket.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.sevensegment.jobis.ticket.model.dto.Ticket;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Ticket")
public class TicketEntity {

    @Id
    @Column(name = "TICKET_KEY", length = 100, nullable = false)
    private String ticketKey;

    @Column(name = "UUID", length = 50, nullable = false)
    private String uuid;

    @Column(name = "PAYMENT_KEY", length = 400, nullable = false)
    private String paymentKey;

    @Column(name = "PROD_NUMBER", nullable = false)
    private int prodNumber; // 명시적으로 prodNumber를 추가

    @Column(name = "TICKET_NAME", length = 100, nullable = false)
    private String ticketName;

    @Column(name = "TICKET_AMOUNT", length = 50, nullable = false)
    private int ticketAmount;

    @Column(name = "TICKET_PERIOD", length = 50, nullable = false)
    private String ticketPeriod;

    @Column(name = "TICKET_COUNT", length = 50, nullable = false)
    private int ticketCount;

    @Column(name = "TICKET_START_DATE", length = 50, nullable = false)
    private Timestamp ticketStartDate;

    @Column(name = "TICKET_END_DATE", length = 50, nullable = false)
    private Timestamp ticketEndDate;

    @Column(name = "PROD_NUMBEROFTIME", nullable = false)
    private int prodNumberOfTime;

    // Entity에서 DTO로 변환
    public Ticket toDto() {
        return Ticket.builder()
                .ticketKey(ticketKey)
                .uuid(uuid)
                .paymentKey(paymentKey)
                .prodNumber(prodNumber) // 명시적으로 prodNumber 사용
                .ticketName(ticketName)
                .ticketAmount(ticketAmount)
                .ticketPeriod(ticketPeriod)
                .ticketCount(ticketCount)
                .ticketStartDate(ticketStartDate)
                .ticketEndDate(ticketEndDate)
                .prodNumberOfTime(prodNumberOfTime)
                .build();
    }
} // 25.01.07 최종 수정

