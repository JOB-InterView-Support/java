package org.myweb.jobis.payment.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPaymentEntity is a Querydsl query type for PaymentEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPaymentEntity extends EntityPathBase<PaymentEntity> {

    private static final long serialVersionUID = 1642749552L;

    public static final QPaymentEntity paymentEntity = new QPaymentEntity("paymentEntity");

    public final DateTimePath<java.sql.Timestamp> approvedAt = createDateTime("approvedAt", java.sql.Timestamp.class);

    public final StringPath cancelYN = createString("cancelYN");

    public final StringPath currenoy = createString("currenoy");

    public final StringPath mid = createString("mid");

    public final StringPath orderId = createString("orderId");

    public final StringPath orderName = createString("orderName");

    public final StringPath paymentKey = createString("paymentKey");

    public final NumberPath<Integer> prodNumber = createNumber("prodNumber", Integer.class);

    public final DateTimePath<java.sql.Timestamp> requestAt = createDateTime("requestAt", java.sql.Timestamp.class);

    public final StringPath status = createString("status");

    public final NumberPath<Integer> totalAmount = createNumber("totalAmount", Integer.class);

    public final StringPath uuid = createString("uuid");

    public QPaymentEntity(String variable) {
        super(PaymentEntity.class, forVariable(variable));
    }

    public QPaymentEntity(Path<? extends PaymentEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPaymentEntity(PathMetadata metadata) {
        super(PaymentEntity.class, metadata);
    }

}

