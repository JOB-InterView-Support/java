package org.myweb.jobis.qna.jpa.repository;

import org.myweb.jobis.qna.jpa.entity.QnaReplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QnaReplyRepository extends JpaRepository<QnaReplyEntity, String> {

    // JPQL 쿼리: 전달받은 QNA 원글 번호(qno)로 댓글 및 대댓글 조회
    @Query("SELECT r FROM QnaReplyEntity r WHERE r.qna.qNo = :qno AND r.repisdeleted = 'N'")
    List<QnaReplyEntity> findAllReply(@Param("qno") String qno);
}
