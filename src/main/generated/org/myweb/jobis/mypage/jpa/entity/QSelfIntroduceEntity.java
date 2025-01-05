package org.myweb.jobis.mypage.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSelfIntroduceEntity is a Querydsl query type for SelfIntroduceEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSelfIntroduceEntity extends EntityPathBase<SelfIntroduceEntity> {

    private static final long serialVersionUID = -1675713542L;

    public static final QSelfIntroduceEntity selfIntroduceEntity = new QSelfIntroduceEntity("selfIntroduceEntity");

    public final StringPath applicationCompanyName = createString("applicationCompanyName");

    public final StringPath certificate = createString("certificate");

    public final StringPath introContents = createString("introContents");

    public final DateTimePath<java.time.LocalDateTime> introDate = createDateTime("introDate", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> introDeletedDate = createDateTime("introDeletedDate", java.time.LocalDateTime.class);

    public final StringPath introIsDeleted = createString("introIsDeleted");

    public final StringPath introIsEdited = createString("introIsEdited");

    public final StringPath introNo = createString("introNo");

    public final StringPath introTitle = createString("introTitle");

    public final StringPath uuid = createString("uuid");

    public final StringPath workType = createString("workType");

    public QSelfIntroduceEntity(String variable) {
        super(SelfIntroduceEntity.class, forVariable(variable));
    }

    public QSelfIntroduceEntity(Path<? extends SelfIntroduceEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSelfIntroduceEntity(PathMetadata metadata) {
        super(SelfIntroduceEntity.class, metadata);
    }

}

