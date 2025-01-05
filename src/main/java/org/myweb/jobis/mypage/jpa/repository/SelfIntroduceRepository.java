package org.myweb.jobis.mypage.jpa.repository;

import org.myweb.jobis.mypage.jpa.entity.SelfIntroduceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SelfIntroduceRepository extends JpaRepository<SelfIntroduceEntity, String> {
    //전체 리스트 표기
    List<SelfIntroduceEntity> findByUuid(String uuid);
    //삭제여부만 체크하여 리스트표기
    List<SelfIntroduceEntity> findByUuidAndIntroIsDeleted(String uuid, String introIsDeleted);
    //삭제여부 / 첨삭여부 체크하여 리스트 표기
    List<SelfIntroduceEntity> findByUuidAndIntroIsDeletedAndIntroIsEdited(String uuid, String introIsDeleted, String introIsEdited);
    //작성 가능수 제한
    long countByUuidAndIntroIsDeletedAndIntroIsEdited(String uuid, String introIsDeleted, String introIsEdited);


    Optional<SelfIntroduceEntity> findByIntroNo(String introNo);
}


