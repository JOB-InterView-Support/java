package org.myweb.jobis.mypage.jpa.repository;

import org.myweb.jobis.mypage.jpa.entity.SelfIntroduceEntity;
import org.myweb.jobis.mypage.model.dto.SelfIntroduce;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SelfIntroduceRepository extends JpaRepository<SelfIntroduceEntity, String> {
    List<SelfIntroduceEntity> findByUuid(String uuid);
}
