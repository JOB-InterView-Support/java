package org.myweb.jobis.jobposting.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QJobFavoritesEntity is a Querydsl query type for JobFavoritesEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QJobFavoritesEntity extends EntityPathBase<JobFavoritesEntity> {

    private static final long serialVersionUID = 658091459L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QJobFavoritesEntity jobFavoritesEntity = new QJobFavoritesEntity("jobFavoritesEntity");

    public final DateTimePath<java.time.LocalDateTime> jobCreatedDate = createDateTime("jobCreatedDate", java.time.LocalDateTime.class);

    public final StringPath jobFavoritesNo = createString("jobFavoritesNo");

    public final StringPath jobPostingId = createString("jobPostingId");

    public final org.myweb.jobis.user.jpa.entity.QUserEntity user;

    public final StringPath uuid = createString("uuid");

    public QJobFavoritesEntity(String variable) {
        this(JobFavoritesEntity.class, forVariable(variable), INITS);
    }

    public QJobFavoritesEntity(Path<? extends JobFavoritesEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QJobFavoritesEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QJobFavoritesEntity(PathMetadata metadata, PathInits inits) {
        this(JobFavoritesEntity.class, metadata, inits);
    }

    public QJobFavoritesEntity(Class<? extends JobFavoritesEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new org.myweb.jobis.user.jpa.entity.QUserEntity(forProperty("user")) : null;
    }

}

