package org.myweb.jobis.qna.jpa.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.myweb.jobis.qna.jpa.entity.QQnaEntity;
import org.myweb.jobis.qna.jpa.entity.QnaEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class QnaRepositoryCustomImpl implements QnaRepositoryCustom {
    @Override
    public int findLastQnaNo() {
        return 0;
    }

    @Override
    public long countSearchTitle(String keyword) {
        return 0;
    }

    @Override
    public long countSearchWriter(String keyword) {
        return 0;
    }

    @Override
    public long countSearchDate(Date begin, Date end) {
        return 0;
    }

    @Override
    public List<QnaEntity> findSearchTitle(String keyword, Pageable pageable) {
        return List.of();
    }

    @Override
    public List<QnaEntity> findSearchWriter(String keyword, Pageable pageable) {
        return List.of();
    }

    @Override
    public List<QnaEntity> findSearchDate(Date begin, Date end, Pageable pageable) {
        return List.of();
    }
//
//    private final JPAQueryFactory queryFactory;
//
//
//    private final QQnaEntity qna = QQnaEntity.qnaEntity; // QueryDSL Q 클래스 매핑
//
//    @Override
//    public int findLastQnaNo() {
//        QnaEntity qnaEntity = queryFactory
//                .selectFrom(qna)
//                .orderBy(qna.qNo.desc())
//                .fetchFirst(); // 가장 마지막 등록 글 1개 조회
//        return Integer.parseInt(qnaEntity.getQNo());
//    }
//
//    @Override
//    public long countSearchTitle(String keyword) {
//        return queryFactory
//                .selectFrom(qna)
//                .where(qna.qTitle.containsIgnoreCase(keyword))
//                .fetchCount();
//    }
//
//    @Override
//    public long countSearchWriter(String keyword) {
//        return queryFactory
//                .selectFrom(qna)
//                .where(qna.qWriter.containsIgnoreCase(keyword))
//                .fetchCount();
//    }
//
//    @Override
//    public long countSearchDate(Date begin, Date end) {
//        return queryFactory
//                .selectFrom(qna)
//                //.where(qna.qWDate.between(begin.toLocalDate().atStartOfDay(), end.toLocalDate().atStartOfDay()))
//                .fetchCount();
//    }
//
//    @Override
//    public List<QnaEntity> findSearchTitle(String keyword, Pageable pageable) {
//        return queryFactory
//                .selectFrom(qna)
//                .where(qna.qTitle.containsIgnoreCase(keyword))
//                .orderBy(qna.qNo.desc())
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//    }
//
//    @Override
//    public List<QnaEntity> findSearchWriter(String keyword, Pageable pageable) {
//        return queryFactory
//                .selectFrom(qna)
//                .where(qna.qWriter.containsIgnoreCase(keyword))
//                .orderBy(qna.qNo.desc())
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//    }
//
//    @Override
//    public List<QnaEntity> findSearchDate(Date begin, Date end, Pageable pageable) {
//        return queryFactory
//                .selectFrom(qna)
//              //  .where(qna.qWDate.between(begin.toLocalDate().atStartOfDay(), end.toLocalDate().atStartOfDay()))
//                .orderBy(qna.qNo.desc())
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//    }
}
