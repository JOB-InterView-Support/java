package org.myweb.jobis.review.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReviewEntity is a Querydsl query type for ReviewEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviewEntity extends EntityPathBase<ReviewEntity> {

    private static final long serialVersionUID = -8847116L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReviewEntity reviewEntity = new QReviewEntity("reviewEntity");

    public final DateTimePath<java.sql.Timestamp> rADate = createDateTime("rADate", java.sql.Timestamp.class);

    public final StringPath rAttachmentTitle = createString("rAttachmentTitle");

    public final StringPath rContent = createString("rContent");

    public final NumberPath<Integer> rCount = createNumber("rCount", Integer.class);

    public final DateTimePath<java.sql.Timestamp> rDDate = createDateTime("rDDate", java.sql.Timestamp.class);

    public final StringPath rIsDeleted = createString("rIsDeleted");

    public final StringPath rNo = createString("rNo");

    public final StringPath rTitle = createString("rTitle");

    public final DateTimePath<java.sql.Timestamp> rUpdateDate = createDateTime("rUpdateDate", java.sql.Timestamp.class);

    public final DateTimePath<java.sql.Timestamp> rWDate = createDateTime("rWDate", java.sql.Timestamp.class);

    public final StringPath rWriter = createString("rWriter");

    public final org.myweb.jobis.user.jpa.entity.QUserEntity user;

    public final StringPath uuid = createString("uuid");

    public QReviewEntity(String variable) {
        this(ReviewEntity.class, forVariable(variable), INITS);
    }

    public QReviewEntity(Path<? extends ReviewEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReviewEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReviewEntity(PathMetadata metadata, PathInits inits) {
        this(ReviewEntity.class, metadata, inits);
    }

    public QReviewEntity(Class<? extends ReviewEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new org.myweb.jobis.user.jpa.entity.QUserEntity(forProperty("user")) : null;
    }

}

