package org.myweb.jobis.notice.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QNoticeAttachmentEntity is a Querydsl query type for NoticeAttachmentEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNoticeAttachmentEntity extends EntityPathBase<NoticeAttachmentEntity> {

    private static final long serialVersionUID = -250438217L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QNoticeAttachmentEntity noticeAttachmentEntity = new QNoticeAttachmentEntity("noticeAttachmentEntity");

    public final QNoticeEntity notice;

    public final StringPath noticeAName = createString("noticeAName");

    public final StringPath noticeANo = createString("noticeANo");

    public QNoticeAttachmentEntity(String variable) {
        this(NoticeAttachmentEntity.class, forVariable(variable), INITS);
    }

    public QNoticeAttachmentEntity(Path<? extends NoticeAttachmentEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QNoticeAttachmentEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QNoticeAttachmentEntity(PathMetadata metadata, PathInits inits) {
        this(NoticeAttachmentEntity.class, metadata, inits);
    }

    public QNoticeAttachmentEntity(Class<? extends NoticeAttachmentEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.notice = inits.isInitialized("notice") ? new QNoticeEntity(forProperty("notice")) : null;
    }

}

