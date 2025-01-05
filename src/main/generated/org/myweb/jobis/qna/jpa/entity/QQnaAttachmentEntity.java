package org.myweb.jobis.qna.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QQnaAttachmentEntity is a Querydsl query type for QnaAttachmentEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QQnaAttachmentEntity extends EntityPathBase<QnaAttachmentEntity> {

    private static final long serialVersionUID = 1126561907L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QQnaAttachmentEntity qnaAttachmentEntity = new QQnaAttachmentEntity("qnaAttachmentEntity");

    public final StringPath qANo = createString("qANo");

    public final StringPath qATitle = createString("qATitle");

    public final StringPath qExtension = createString("qExtension");

    public final QQnaEntity qna;

    public QQnaAttachmentEntity(String variable) {
        this(QnaAttachmentEntity.class, forVariable(variable), INITS);
    }

    public QQnaAttachmentEntity(Path<? extends QnaAttachmentEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QQnaAttachmentEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QQnaAttachmentEntity(PathMetadata metadata, PathInits inits) {
        this(QnaAttachmentEntity.class, metadata, inits);
    }

    public QQnaAttachmentEntity(Class<? extends QnaAttachmentEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.qna = inits.isInitialized("qna") ? new QQnaEntity(forProperty("qna"), inits.get("qna")) : null;
    }

}

