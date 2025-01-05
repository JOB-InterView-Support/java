package org.myweb.jobis.qna.jpa.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.qna.jpa.entity.QQnaEntity;
import org.myweb.jobis.qna.jpa.entity.QnaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class QnaRepositoryCustomImpl implements QnaRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager; // JPQL 용

    private final QQnaEntity qna = QQnaEntity.qnaEntity; // QueryDSL Q 클래스 매핑

//    @Override
//    public Page<QnaEntity> findByQIsDeleted(String qIsDeleted, Pageable pageable) {
//        String query = "SELECT q FROM QnaEntity q WHERE q.qIsDeleted = :qIsDeleted";
//        List<QnaEntity> resultList = entityManager.createQuery(query, QnaEntity.class)
//                .setParameter("qIsDeleted", qIsDeleted)
//                .setFirstResult((int) pageable.getOffset())
//                .setMaxResults(pageable.getPageSize())
//                .getResultList();
//
//        String countQuery = "SELECT COUNT(q) FROM QnaEntity q WHERE q.qIsDeleted = :qIsDeleted";
//        Long totalCount = entityManager.createQuery(countQuery, Long.class)
//                .setParameter("qIsDeleted", qIsDeleted)
//                .getSingleResult();
//
//        return new PageImpl<>(resultList, pageable, totalCount);
//    }

    @Override
    public Page<QnaEntity> findByQIsDeleted(String qIsDeleted, Pageable pageable) {
        JPAQuery<QnaEntity> query = queryFactory.selectFrom(qna)
                .where(qna.qIsDeleted.eq(qIsDeleted))
                .orderBy(qna.qWDate.desc()); // 내림차순 정렬

        long total = query.fetchCount(); // 총 데이터 개수 계산
        List<QnaEntity> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, total);
    }



//    @Override
//    public String findLastQnaNo() {
//        QnaEntity qnaEntity = queryFactory
//                .selectFrom(qna)
//                .from(qna)
//                .orderBy(qna.qNo.desc())
//                .fetchFirst(); // 가장 마지막 등록 글 1개 조회
//        return qnaEntity.getQNo();
//    }

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

