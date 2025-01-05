package org.myweb.jobis.qna.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QQnaEntity is a Querydsl query type for QnaEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QQnaEntity extends EntityPathBase<QnaEntity> {

    private static final long serialVersionUID = 1427404208L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QQnaEntity qnaEntity = new QQnaEntity("qnaEntity");

    public final DateTimePath<java.sql.Timestamp> qADate = createDateTime("qADate", java.sql.Timestamp.class);

    public final StringPath qAttachmentTitle = createString("qAttachmentTitle");

    public final StringPath qAttachmentYN = createString("qAttachmentYN");

    public final StringPath qContent = createString("qContent");

    public final DateTimePath<java.sql.Timestamp> qDDate = createDateTime("qDDate", java.sql.Timestamp.class);

    public final StringPath qIsDeleted = createString("qIsDeleted");

    public final StringPath qIsSecret = createString("qIsSecret");

    public final StringPath qNo = createString("qNo");

    public final StringPath qTitle = createString("qTitle");

    public final DateTimePath<java.sql.Timestamp> qUpdateDate = createDateTime("qUpdateDate", java.sql.Timestamp.class);

    public final StringPath qUpdateYN = createString("qUpdateYN");

    public final DateTimePath<java.sql.Timestamp> qWDate = createDateTime("qWDate", java.sql.Timestamp.class);

    public final StringPath qWriter = createString("qWriter");

    public final org.myweb.jobis.user.jpa.entity.QUserEntity user;

    public final StringPath uuid = createString("uuid");

    public QQnaEntity(String variable) {
        this(QnaEntity.class, forVariable(variable), INITS);
    }

    public QQnaEntity(Path<? extends QnaEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QQnaEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QQnaEntity(PathMetadata metadata, PathInits inits) {
        this(QnaEntity.class, metadata, inits);
    }

    public QQnaEntity(Class<? extends QnaEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new org.myweb.jobis.user.jpa.entity.QUserEntity(forProperty("user")) : null;
    }

}

