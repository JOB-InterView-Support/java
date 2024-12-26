package org.myweb.jobis.mypage.jpa.repository;

import org.myweb.jobis.mypage.jpa.entity.SelfIntroduceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SelfIntroduceRepository extends JpaRepository<SelfIntroduceEntity, String> {
    List<SelfIntroduceEntity> findByUuid(String uuid);

    Optional<SelfIntroduceEntity> findByIntroNo(String introNo);
}


