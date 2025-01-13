package com.sevensegment.jobis.review.jpa.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.sevensegment.jobis.review.jpa.entity.QReviewEntity;
import com.sevensegment.jobis.review.jpa.entity.ReviewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ReviewRepositoryCustomImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    private final QReviewEntity review = QReviewEntity.reviewEntity;


    @Override
    public Page<ReviewEntity> findByRIsDeleted(String rIsDeleted, Pageable pageable) {
        JPAQuery<ReviewEntity> query = queryFactory.selectFrom(review)
            .where(review.rIsDeleted.eq(rIsDeleted))
            .orderBy(review.rWDate.desc()); // 내림차순 정렬

        long total = query.fetchCount(); // 총 데이터 개수 계산
        List<ReviewEntity> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, total);
    }

    // 마지막 리뷰 번호 조회
    @Override
    public String findLastReviewNo() {
        ReviewEntity reviewEntity = queryFactory
                .selectFrom(review)
                .from(review)
                .orderBy(review.rNo.desc())
                .fetchFirst(); // 가장 최근 리뷰 가져오기
        return reviewEntity.getRNo();
    }

    // 제목으로 검색한 리뷰 개수
    @Override
    public long countSearchTitle(String keyword) {
        return queryFactory
                .selectFrom(review)
                .where(review.rTitle.like("%" + keyword + "%")) // 제목에 키워드 포함
                .fetchCount();
    }

    // 작성자로 검색한 리뷰 개수
    @Override
    public long countSearchWriter(String keyword) {
        return queryFactory
                .selectFrom(review)
                .where(review.rWriter.containsIgnoreCase(keyword)) // 작성자에 키워드 포함
                .fetchCount();
    }

    // 제목으로 검색한 리뷰 목록
    @Override
    public List<ReviewEntity> findSearchTitle(String keyword, Pageable pageable) {
        return queryFactory
                .selectFrom(review)
                .where(review.rTitle.containsIgnoreCase(keyword)) // 제목에 키워드 포함
                .orderBy(review.rNo.desc()) // 최신순 정렬
                .offset(pageable.getOffset()) // 페이지 오프셋
                .limit(pageable.getPageSize()) // 페이지 크기
                .fetch();
    }

    // 내용으로 검색한 리뷰 목록
    @Override
    public List<ReviewEntity> findSearchContent(String keyword, Pageable pageable) {
        return queryFactory
                .selectFrom(review)
                .where(review.rContent.like("%" + keyword + "%")) // 내용에 키워드 포함
                .orderBy(review.rNo.desc()) // 최신순 정렬
                .offset(pageable.getOffset()) // 페이지 오프셋
                .limit(pageable.getPageSize()) // 페이지 크기
                .fetch();
    }

    @Override
    public Optional<ReviewEntity> findByrNo(String rNo) {
        ReviewEntity result = queryFactory
                .selectFrom(review)
                .where(review.rNo.eq(rNo)
                        .and(review.rIsDeleted.eq("N")))
                .fetchOne();
        log.info("findByrNo result for rNo {}: {}", rNo, result);
        return Optional.ofNullable(result);
    }
}
