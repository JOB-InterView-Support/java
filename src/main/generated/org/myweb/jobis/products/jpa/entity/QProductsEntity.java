package org.myweb.jobis.products.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QProductsEntity is a Querydsl query type for ProductsEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProductsEntity extends EntityPathBase<ProductsEntity> {

    private static final long serialVersionUID = 1613197580L;

    public static final QProductsEntity productsEntity = new QProductsEntity("productsEntity");

    public final NumberPath<Integer> prodAmount = createNumber("prodAmount", Integer.class);

    public final StringPath prodDescription = createString("prodDescription");

    public final StringPath prodName = createString("prodName");

    public final NumberPath<Integer> prodNumber = createNumber("prodNumber", Integer.class);

    public final NumberPath<Integer> prodNumberOfTime = createNumber("prodNumberOfTime", Integer.class);

    public final StringPath prodPeriod = createString("prodPeriod");

    public final StringPath prodSellable = createString("prodSellable");

    public QProductsEntity(String variable) {
        super(ProductsEntity.class, forVariable(variable));
    }

    public QProductsEntity(Path<? extends ProductsEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProductsEntity(PathMetadata metadata) {
        super(ProductsEntity.class, metadata);
    }

}

