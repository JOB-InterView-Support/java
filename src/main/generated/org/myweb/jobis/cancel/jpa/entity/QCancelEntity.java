package org.myweb.jobis.cancel.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCancelEntity is a Querydsl query type for CancelEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCancelEntity extends EntityPathBase<CancelEntity> {

    private static final long serialVersionUID = -1697429576L;

    public static final QCancelEntity cancelEntity = new QCancelEntity("cancelEntity");

    public final DateTimePath<java.sql.Timestamp> cancelApprovedAt = createDateTime("cancelApprovedAt", java.sql.Timestamp.class);

    public final StringPath cancelKey = createString("cancelKey");

    public final DateTimePath<java.sql.Timestamp> cancelRequestedAt = createDateTime("cancelRequestedAt", java.sql.Timestamp.class);

    public final StringPath payPaymentKey = createString("payPaymentKey");

    public final NumberPath<Integer> prodNumber = createNumber("prodNumber", Integer.class);

    public final StringPath uuid = createString("uuid");

    public QCancelEntity(String variable) {
        super(CancelEntity.class, forVariable(variable));
    }

    public QCancelEntity(Path<? extends CancelEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCancelEntity(PathMetadata metadata) {
        super(CancelEntity.class, metadata);
    }

}

