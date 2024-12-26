package org.myweb.jobis.qna.jpa.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.qna.jpa.entity.QQnaEntity;
import org.myweb.jobis.qna.jpa.entity.QnaEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class QnaRepositoryCustomImpl implements QnaRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager; // JPQL 용

    private final QQnaEntity qna = QQnaEntity.qnaEntity; // QueryDSL Q 클래스 매핑

    @Override
    public String findLastQnaNo() {
        QnaEntity qnaEntity = queryFactory
                .selectFrom(qna)
                .from(qna)
                .orderBy(qna.qNo.desc())
                .fetchFirst(); // 가장 마지막 등록 글 1개 조회
        return qnaEntity.getQNo();
    }

    @Override
    public long countSearchTitle(String keyword) {
        return queryFactory
                .selectFrom(qna)
                .where(qna.qTitle.containsIgnoreCase(keyword))
                .fetchCount();
    }

    @Override
    public long countSearchWriter(String keyword) {
        return queryFactory
                .selectFrom(qna)
                .where(qna.qWriter.containsIgnoreCase(keyword))
                .fetchCount();
    }

    // 날짜로 검색하는 부분 검색 수정중
//    @Override
//    public long countSearchDate(Date begin, Date end) {
//        return queryFactory
//                .selectFrom(qna)
//               .where(qna.qWDate.between(begin.toLocalDate().atStartOfDay(), end.toLocalDate().atStartOfDay()))
//                .fetchCount();
//    }

    @Override
    public List<QnaEntity> findSearchTitle(String keyword, Pageable pageable) {
        return queryFactory
                .selectFrom(qna)
                .where(qna.qTitle.containsIgnoreCase(keyword))
                .orderBy(qna.qNo.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public List<QnaEntity> findSearchWriter(String keyword, Pageable pageable) {
        return queryFactory
                .selectFrom(qna)
                .where(qna.qWriter.containsIgnoreCase(keyword))
                .orderBy(qna.qNo.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public Optional<QnaEntity> findByQno(String qno) {
        QnaEntity result = queryFactory
                .selectFrom(qna)
                .where(qna.qNo.eq(qno)
                        .and(qna.qIsDeleted.eq("N"))) // 삭제되지 않은 데이터만 조회
                .fetchOne();
        log.info("findByQno result for qNo {}: {}", qno, result); // 디버깅 로그 추가
        return Optional.ofNullable(result);
    }


}

    //날짜 검색 부분 .. 수정중
//    @Override
//    public List<QnaEntity> findSearchDate(Date begin, Date end, Pageable pageable) {
//        return queryFactory
//                .selectFrom(qna)
//            //    .where(qna.qWDate.between(begin.toLocalDate().atStartOfDay(), end.toLocalDate().atStartOfDay()))
//                .orderBy(qna.qNo.desc())
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//    }

