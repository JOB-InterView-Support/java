package org.myweb.jobis.qna.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QQnaReplyEntity is a Querydsl query type for QnaReplyEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QQnaReplyEntity extends EntityPathBase<QnaReplyEntity> {

    private static final long serialVersionUID = 543363520L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QQnaReplyEntity qnaReplyEntity = new QQnaReplyEntity("qnaReplyEntity");

    public final QQnaEntity qna;

    public final StringPath repcontent = createString("repcontent");

    public final DateTimePath<java.sql.Timestamp> repdate = createDateTime("repdate", java.sql.Timestamp.class);

    public final DateTimePath<java.sql.Timestamp> repdeletedate = createDateTime("repdeletedate", java.sql.Timestamp.class);

    public final ComparablePath<Character> repisdeleted = createComparable("repisdeleted", Character.class);

    public final StringPath repno = createString("repno");

    public final DateTimePath<java.sql.Timestamp> repupdatedate = createDateTime("repupdatedate", java.sql.Timestamp.class);

    public final StringPath repwriter = createString("repwriter");

    public final StringPath uuid = createString("uuid");

    public QQnaReplyEntity(String variable) {
        this(QnaReplyEntity.class, forVariable(variable), INITS);
    }

    public QQnaReplyEntity(Path<? extends QnaReplyEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QQnaReplyEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QQnaReplyEntity(PathMetadata metadata, PathInits inits) {
        this(QnaReplyEntity.class, metadata, inits);
    }

    public QQnaReplyEntity(Class<? extends QnaReplyEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.qna = inits.isInitialized("qna") ? new QQnaEntity(forProperty("qna"), inits.get("qna")) : null;
    }

}

