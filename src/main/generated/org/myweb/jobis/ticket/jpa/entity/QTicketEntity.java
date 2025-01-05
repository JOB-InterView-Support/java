package org.myweb.jobis.ticket.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTicketEntity is a Querydsl query type for TicketEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTicketEntity extends EntityPathBase<TicketEntity> {

    private static final long serialVersionUID = 2012771676L;

    public static final QTicketEntity ticketEntity = new QTicketEntity("ticketEntity");

    public final StringPath paymentKey = createString("paymentKey");

    public final NumberPath<Integer> prodNumber = createNumber("prodNumber", Integer.class);

    public final NumberPath<Integer> prodNumberOfTime = createNumber("prodNumberOfTime", Integer.class);

    public final NumberPath<Integer> ticketAmount = createNumber("ticketAmount", Integer.class);

    public final NumberPath<Integer> ticketCount = createNumber("ticketCount", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> ticketEndDate = createDateTime("ticketEndDate", java.time.LocalDateTime.class);

    public final StringPath ticketKey = createString("ticketKey");

    public final StringPath ticketName = createString("ticketName");

    public final StringPath ticketPeriod = createString("ticketPeriod");

    public final DateTimePath<java.time.LocalDateTime> ticketStartDate = createDateTime("ticketStartDate", java.time.LocalDateTime.class);

    public final StringPath uuid = createString("uuid");

    public QTicketEntity(String variable) {
        super(TicketEntity.class, forVariable(variable));
    }

    public QTicketEntity(Path<? extends TicketEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTicketEntity(PathMetadata metadata) {
        super(TicketEntity.class, metadata);
    }

}

