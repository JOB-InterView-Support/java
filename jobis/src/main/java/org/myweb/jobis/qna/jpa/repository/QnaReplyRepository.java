package org.myweb.jobis.qna.jpa.repository;

import org.myweb.jobis.qna.jpa.entity.QnaReplyEntity;
import org.myweb.jobis.qna.model.dto.QnaReply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QnaReplyRepository extends JpaRepository<QnaReplyEntity, String> {
    //QnaReplyRepository
}
