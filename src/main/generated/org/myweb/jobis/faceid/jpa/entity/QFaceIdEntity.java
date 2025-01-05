package org.myweb.jobis.faceid.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QFaceIdEntity is a Querydsl query type for FaceIdEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFaceIdEntity extends EntityPathBase<FaceIdEntity> {

    private static final long serialVersionUID = 1493577428L;

    public static final QFaceIdEntity faceIdEntity = new QFaceIdEntity("faceIdEntity");

    public final DateTimePath<java.time.LocalDateTime> capturedAt = createDateTime("capturedAt", java.time.LocalDateTime.class);

    public final StringPath imagePath = createString("imagePath");

    public final DateTimePath<java.time.LocalDateTime> updateAt = createDateTime("updateAt", java.time.LocalDateTime.class);

    public final StringPath userFaceId = createString("userFaceId");

    public final StringPath uuid = createString("uuid");

    public QFaceIdEntity(String variable) {
        super(FaceIdEntity.class, forVariable(variable));
    }

    public QFaceIdEntity(Path<? extends FaceIdEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFaceIdEntity(PathMetadata metadata) {
        super(FaceIdEntity.class, metadata);
    }

}

