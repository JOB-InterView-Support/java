package com.sevensegment.jobis.qna.jpa.repository;

import com.sevensegment.jobis.qna.jpa.entity.QnaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QnaRepository extends JpaRepository<QnaEntity, String>, QnaRepositoryCustom {



}