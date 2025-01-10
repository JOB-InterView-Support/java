package org.myweb.jobis.ticket.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.products.jpa.entity.ProductsEntity;
import org.myweb.jobis.products.jpa.repository.ProductsRepository;
import org.myweb.jobis.ticket.jpa.entity.TicketEntity;
import org.myweb.jobis.ticket.jpa.repository.TicketRepository;
import org.myweb.jobis.user.jpa.entity.UserEntity;
import org.myweb.jobis.user.jpa.repository.UserRepository;
import org.myweb.jobis.security.jwt.JWTUtil;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final ProductsRepository productsRepository;
    private final UserRepository userRepository; // UserRepository 주입
    private final JWTUtil jwtUtil; // JWTUtil 주입 (토큰에서 userId 추출)

    public boolean startMockInterview(String selectedDateStr, String userUuid) {
        try {
            // String 날짜를 LocalDateTime으로 파싱
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime selectedDate = LocalDateTime.parse(selectedDateStr, formatter);

            // LocalDateTime을 Timestamp로 변환
            Timestamp selectedTimestamp = Timestamp.valueOf(selectedDate);

            // 사용자의 모든 Ticket 조회
            List<TicketEntity> userTickets = ticketRepository.findByUuid(userUuid);

            if (userTickets.isEmpty()) {
                log.info("사용 가능한 티켓이 없습니다. userUuid: {}", userUuid);
                return false;
            }

            // 조건 확인 및 Ticket 업데이트
            return userTickets.stream()
                    .filter(ticket -> ticket.getTicketEndDate().after(selectedTimestamp)) // ticketEndDate가 선택한 날짜 이후인지 확인
                    .filter(ticket -> ticket.getTicketCount() > 0) // TicketCount가 0이 아닌지 확인
                    .max(Comparator.comparing(TicketEntity::getTicketStartDate)) // 가장 최근 Ticket 가져오기
                    .map(ticket -> {
                        // 조건 만족하면 prodNumberOfTime 감소
                        ticket.setTicketCount(ticket.getTicketCount() - 1);

                        log.info("티켓 저장 전 상태: {}", ticket);
                        ticketRepository.save(ticket); // 변경사항 저장
                        log.info("티켓 차감 완료: {}", ticket);
                        return true;
                    })
                    .orElse(false); // 조건 만족하는 Ticket 없으면 false 반환
        } catch (Exception e) {
            // 예외 처리
            log.error("startMockInterview 중 오류 발생: ", e);
            return false;
        }
    }

    public List<Integer> getTicketCountsByUuid(String uuid) {
        List<TicketEntity> tickets = ticketRepository.findAllByUuidOrderByTicketStartDateDesc(uuid);
        return tickets.stream()
                .map(TicketEntity::getTicketCount)
                .collect(Collectors.toList());
    }

    public TicketEntity findLatestActiveTicketByUuid(String uuid) {
        return ticketRepository.findAllByUuidOrderByTicketStartDateDesc(uuid).stream()
                .filter(ticket -> ticket.getTicketCount() > 0)
                .findFirst()
                .orElse(null);
    }
} // 25.01.07 최종 수정
