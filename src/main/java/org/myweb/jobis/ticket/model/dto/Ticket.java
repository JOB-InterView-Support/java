package org.myweb.jobis.ticket.model.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.myweb.jobis.ticket.jpa.entity.TicketEntity;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ticket {
    private String ticketKey;
    private String uuid;
    private String paymentKey;
    private int prodNumber;
    private String ticketName;
    private int ticketAmount;


    private String ticketPeriod;

    private int ticketCount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp ticketStartDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp ticketEndDate;

    private int prodNumberOfTime;

    // Entity 객체로 변환하는 메서드
    public TicketEntity toEntity() {
        return TicketEntity.builder()
                .ticketKey(ticketKey)
                .uuid(uuid)
                .paymentKey(paymentKey)
                .prodNumber(prodNumber)
                .ticketName(ticketName)
                .ticketAmount(ticketAmount)
                .ticketPeriod(ticketPeriod)
                .ticketCount(ticketCount)
                .ticketStartDate(ticketStartDate)
                .ticketEndDate(ticketEndDate)
                .prodNumberOfTime(prodNumberOfTime)
                .build();
    }

    //test
}
