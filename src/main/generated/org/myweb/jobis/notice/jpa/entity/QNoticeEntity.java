package org.myweb.jobis.notice.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QNoticeEntity is a Querydsl query type for NoticeEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNoticeEntity extends EntityPathBase<NoticeEntity> {

    private static final long serialVersionUID = -1429331980L;

    public static final QNoticeEntity noticeEntity = new QNoticeEntity("noticeEntity");

    public final ListPath<NoticeAttachmentEntity, QNoticeAttachmentEntity> noticeAttachments = this.<NoticeAttachmentEntity, QNoticeAttachmentEntity>createList("noticeAttachments", NoticeAttachmentEntity.class, QNoticeAttachmentEntity.class, PathInits.DIRECT2);

    public final StringPath noticeContent = createString("noticeContent");

    public final DateTimePath<java.sql.Timestamp> noticeDDate = createDateTime("noticeDDate", java.sql.Timestamp.class);

    public final StringPath noticeIsDeleted = createString("noticeIsDeleted");

    public final StringPath noticeNo = createString("noticeNo");

    public final StringPath noticePath = createString("noticePath");

    public final StringPath noticeTitle = createString("noticeTitle");

    public final DateTimePath<java.sql.Timestamp> noticeUDate = createDateTime("noticeUDate", java.sql.Timestamp.class);

    public final NumberPath<Integer> noticeVCount = createNumber("noticeVCount", Integer.class);

    public final DateTimePath<java.sql.Timestamp> noticeWDate = createDateTime("noticeWDate", java.sql.Timestamp.class);

    public final StringPath uuid = createString("uuid");

    public QNoticeEntity(String variable) {
        super(NoticeEntity.class, forVariable(variable));
    }

    public QNoticeEntity(Path<? extends NoticeEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QNoticeEntity(PathMetadata metadata) {
        super(NoticeEntity.class, metadata);
    }

}

