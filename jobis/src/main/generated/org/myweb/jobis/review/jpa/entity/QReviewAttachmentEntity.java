package org.myweb.jobis.review.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReviewAttachmentEntity is a Querydsl query type for ReviewAttachmentEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviewAttachmentEntity extends EntityPathBase<ReviewAttachmentEntity> {

    private static final long serialVersionUID = -94880073L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReviewAttachmentEntity reviewAttachmentEntity = new QReviewAttachmentEntity("reviewAttachmentEntity");

    public final StringPath rANo = createString("rANo");

    public final StringPath rATitle = createString("rATitle");

    public final QReviewEntity review;

    public final StringPath rExtension = createString("rExtension");

    public QReviewAttachmentEntity(String variable) {
        this(ReviewAttachmentEntity.class, forVariable(variable), INITS);
    }

    public QReviewAttachmentEntity(Path<? extends ReviewAttachmentEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReviewAttachmentEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReviewAttachmentEntity(PathMetadata metadata, PathInits inits) {
        this(ReviewAttachmentEntity.class, metadata, inits);
    }

    public QReviewAttachmentEntity(Class<? extends ReviewAttachmentEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.review = inits.isInitialized("review") ? new QReviewEntity(forProperty("review"), inits.get("review")) : null;
    }

}

