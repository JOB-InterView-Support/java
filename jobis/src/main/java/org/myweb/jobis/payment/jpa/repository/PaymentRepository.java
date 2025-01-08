package org.myweb.jobis.payment.jpa.repository;

//import org.myweb.jobis.jobposting.jpa.entity.JobFavoritesEntity;
import org.myweb.jobis.payment.jpa.entity.PaymentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
    boolean existsByPaymentKeyAndOrderId(String paymentKey, String orderId);

    boolean existsByOrderId(String orderId);
    // 기본 JPA 메소드 사용 가능

    Optional<Object> findByPaymentKey(String paymentKey);

    @Query("SELECT new map(p.prodNumber as prodNumber, p.uuid as uuid, u.userName as userName, " +
            "p.currency as currency, p.totalAmount as totalAmount, p.status as status, " +
            "p.requestedAt as requestedAt, p.approvedAt as approvedAt, p.cancelYN as cancelYN, " +
            "prod.prodName as prodName) " +
            "FROM PaymentEntity p " +
            "JOIN UserEntity u ON p.uuid = u.uuid " +
            "JOIN ProductsEntity prod ON p.prodNumber = prod.prodNumber " +
            "WHERE (:cancelYN IS NULL OR p.cancelYN = :cancelYN) " +
            "AND (:userName IS NULL OR u.userName LIKE %:userName%) " +
            "AND (:prodName IS NULL OR prod.prodName LIKE %:prodName%) " +
            "ORDER BY p.approvedAt DESC")
    Page<Map<String, Object>> findFilteredSalesHistory(
            @Param("cancelYN") String cancelYN,     // 환불 여부
            @Param("userName") String userName,     // 사용자 이름
            @Param("prodName") String prodName,     // 상품명
            Pageable pageable                       // 페이징 정보
    );


}
