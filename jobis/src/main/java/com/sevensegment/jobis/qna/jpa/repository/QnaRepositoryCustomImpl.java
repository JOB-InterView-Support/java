package com.sevensegment.jobis.qna.jpa.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.sevensegment.jobis.qna.jpa.entity.QQnaEntity;
import com.sevensegment.jobis.qna.jpa.entity.QnaEntity;
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
    @PersistenceContext
    private final EntityManager entityManager; // JPQL 용

    private final QQnaEntity qna = QQnaEntity.qnaEntity; // QueryDSL Q 클래스 매핑



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


    @Override
    public List<QnaEntity> searchByKeyword(String keyword, Pageable pageable) {
        String queryStr = "SELECT q FROM QnaEntity q WHERE q.qTitle LIKE :keyword OR q.qContent LIKE :keyword";
        TypedQuery<QnaEntity> query = entityManager.createQuery(queryStr, QnaEntity.class);
        query.setParameter("keyword", "%" + keyword + "%");
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        return query.getResultList();
    }


}


