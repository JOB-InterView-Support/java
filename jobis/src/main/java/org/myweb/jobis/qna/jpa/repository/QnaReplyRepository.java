package org.myweb.jobis.qna.jpa.repository;

import org.myweb.jobis.qna.jpa.entity.QnaReplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface QnaReplyRepository extends JpaRepository<QnaReplyEntity, String> {

}
