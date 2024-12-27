package org.myweb.jobis.payment.jpa.repository;

//import org.myweb.jobis.jobposting.jpa.entity.JobFavoritesEntity;
import org.myweb.jobis.payment.jpa.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, String>, PaymentRepositoryCustom {
    // 기본 JPA 메소드 사용 가능
}
