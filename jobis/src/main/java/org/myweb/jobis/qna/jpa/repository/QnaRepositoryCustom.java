package org.myweb.jobis.qna.jpa.repository;

import org.myweb.jobis.qna.jpa.entity.QnaEntity;
import org.springframework.data.domain.Pageable;

import java.sql.Date;
import java.util.List;

public interface QnaRepositoryCustom {
    String findLastQnaNo();
    long countSearchTitle(String keyword);
    long countSearchWriter(String keyword);
 //   long countSearchDate(Date begin, Date end);
    List<QnaEntity> findSearchTitle(String keyword, Pageable pageable);
    List<QnaEntity> findSearchWriter(String keyword, Pageable pageable);
//    List<QnaEntity> findSearchDate(Date begin, Date end, Pageable pageable);


}
