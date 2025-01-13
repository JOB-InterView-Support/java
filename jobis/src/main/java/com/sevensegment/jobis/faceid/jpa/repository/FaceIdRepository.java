package com.sevensegment.jobis.faceid.jpa.repository;

import com.sevensegment.jobis.faceid.jpa.entity.FaceIdEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FaceIdRepository extends JpaRepository<FaceIdEntity, Long> {
    Optional<FaceIdEntity> findByUuid(String uuid);
}
