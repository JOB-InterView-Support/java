package com.sevensegment.jobis.cancel.jpa.repository;

import com.sevensegment.jobis.cancel.jpa.entity.CancelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CancelRepository extends JpaRepository<CancelEntity, Long>{
    // 기본 JPA 메소드 사용 가능
}
