package org.myweb.jobis.jobposting.jpa.repository;

import org.myweb.jobis.jobposting.jpa.entity.JobFavoritesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface JobFavoritesRepository extends JpaRepository<JobFavoritesEntity, Long> {

    // 특정 사용자의 즐겨찾기 목록 조회
    @Query("SELECT j FROM JobFavoritesEntity j WHERE j.uuid = :uuid")
    List<JobFavoritesEntity> findByUuid(@Param("uuid") String uuid);

    // 특정 사용자의 즐겨찾기 삭제
    @Modifying
    @Transactional
    @Query("DELETE FROM JobFavoritesEntity j WHERE j.uuid = :uuid AND j.jobPostingId = :jobPostingId")
    void deleteByUuidAndJobPostingId(@Param("uuid") String uuid, @Param("jobPostingId") String jobPostingId);

    // 특정 즐겨찾기 존재 여부 확인
    @Query("SELECT COUNT(j) > 0 FROM JobFavoritesEntity j WHERE j.uuid = :uuid AND j.jobPostingId = :jobPostingId")
    boolean existsByUuidAndJobPostingId(@Param("uuid") String uuid, @Param("jobPostingId") String jobPostingId);
}
