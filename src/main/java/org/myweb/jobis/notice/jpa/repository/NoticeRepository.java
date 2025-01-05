package org.myweb.jobis.notice.jpa.repository;

import org.myweb.jobis.notice.jpa.entity.NoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<NoticeEntity, String>, NoticeRepositoryCustom {
}
