package org.myweb.jobis.notice.jpa.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.notice.jpa.entity.NoticeEntity;
import org.myweb.jobis.notice.jpa.entity.QNoticeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class NoticeRepositoryCustomImpl implements NoticeRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;
    private final QNoticeEntity notice = QNoticeEntity.noticeEntity;

    //
    @Override
    public Optional<NoticeEntity> findByNotice(String noticeNo) {
        NoticeEntity result = queryFactory
                .selectFrom(notice)
                .where(notice.noticeNo.eq(noticeNo)
                        .and(notice.noticeIsDeleted.eq("N"))) // 삭제되지 않은 데이터만 조회
                .fetchOne();
        log.info("findByNotice result for noticeNo {}: {}", noticeNo, result); // 디버깅 로그 추가
        return Optional.ofNullable(result);
    }

    @Override
    public Page<NoticeEntity> findByNoticeIsDeleted(String noticeIsDeleted, Pageable pageable) {
        JPAQuery<NoticeEntity> query = queryFactory.selectFrom(notice)
                .where(notice.noticeIsDeleted.eq(noticeIsDeleted))
                .orderBy(notice.noticeWDate.desc()); // 내림차순 정렬

        long total = query.fetchCount(); // 총 데이터 개수 계산
        List<NoticeEntity> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public String findLastNoticeNo() {
        NoticeEntity noticeEntity = queryFactory
                .selectFrom(notice)
                .orderBy(notice.noticeNo.desc())
                .fetchFirst(); // fetch().get(0) 대신 fetchFirst()로 대체
        return noticeEntity != null ? noticeEntity.getNoticeNo() : null;
    }

    @Override
    public List<NoticeEntity> findSearchTitle(String keyword, Pageable pageable) {
        return queryFactory
                .selectFrom(notice)
                .where(notice.noticeTitle.like("%" + keyword + "%"))
                .orderBy(notice.noticeNo.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public String countSearchTitle(String keyword) {
        return String.valueOf(queryFactory
                .selectFrom(notice)
                .where(notice.noticeTitle.like("%" + keyword + "%"))
                .fetchCount());
    }

    @Override
    public List<NoticeEntity> findSearchContent(String keyword, Pageable pageable) {
        return queryFactory
                .selectFrom(notice)
                .where(notice.noticeContent.like("%" + keyword + "%"))
                .orderBy(notice.noticeNo.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public String countSearchContent(String keyword) {
        return String.valueOf(queryFactory
                .selectFrom(notice)
                .where(notice.noticeContent.like("%" + keyword + "%"))
                .fetchCount());
    }

    @Override
    public List<NoticeEntity> findSearchDate(Timestamp begin, Timestamp end, Pageable pageable) {
        return queryFactory
                .selectFrom(notice)
                .where(notice.noticeWDate.between(begin, end))
                .orderBy(notice.noticeNo.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public String countSearchDate(Timestamp begin, Timestamp end) {
        return String.valueOf(queryFactory
                .selectFrom(notice)
                .where(notice.noticeWDate.between(begin, end))
                .fetchCount());
    }
}
