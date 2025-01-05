package org.myweb.jobis.user.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserEntity is a Querydsl query type for UserEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserEntity extends EntityPathBase<UserEntity> {

    private static final long serialVersionUID = 2007896922L;

    public static final QUserEntity userEntity = new QUserEntity("userEntity");

    public final StringPath adminYn = createString("adminYn");

    public final DatePath<java.time.LocalDate> userBirthday = createDate("userBirthday", java.time.LocalDate.class);

    public final DateTimePath<java.time.LocalDateTime> userCreateAt = createDateTime("userCreateAt", java.time.LocalDateTime.class);

    public final StringPath userDefaultEmail = createString("userDefaultEmail");

    public final DateTimePath<java.time.LocalDateTime> userDeletionDate = createDateTime("userDeletionDate", java.time.LocalDateTime.class);

    public final StringPath userDeletionReason = createString("userDeletionReason");

    public final StringPath userDeletionStatus = createString("userDeletionStatus");

    public final StringPath userFaceIdStatus = createString("userFaceIdStatus");

    public final StringPath userGender = createString("userGender");

    public final StringPath userGoogleEmail = createString("userGoogleEmail");

    public final StringPath userId = createString("userId");

    public final StringPath userKakaoEmail = createString("userKakaoEmail");

    public final StringPath userName = createString("userName");

    public final StringPath userNaverEmail = createString("userNaverEmail");

    public final StringPath userPhone = createString("userPhone");

    public final StringPath userPw = createString("userPw");

    public final StringPath userRefreshToken = createString("userRefreshToken");

    public final StringPath userRestrictionReason = createString("userRestrictionReason");

    public final StringPath userRestrictionStatus = createString("userRestrictionStatus");

    public final DateTimePath<java.time.LocalDateTime> userUpdateAt = createDateTime("userUpdateAt", java.time.LocalDateTime.class);

    public final StringPath uuid = createString("uuid");

    public QUserEntity(String variable) {
        super(UserEntity.class, forVariable(variable));
    }

    public QUserEntity(Path<? extends UserEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserEntity(PathMetadata metadata) {
        super(UserEntity.class, metadata);
    }

}

