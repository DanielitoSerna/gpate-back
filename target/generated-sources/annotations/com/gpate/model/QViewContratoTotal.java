package com.gpate.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QViewContratoTotal is a Querydsl query type for ViewContratoTotal
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QViewContratoTotal extends EntityPathBase<ViewContratoTotal> {

    private static final long serialVersionUID = -1607462484L;

    public static final QViewContratoTotal viewContratoTotal = new QViewContratoTotal("viewContratoTotal");

    public final NumberPath<java.math.BigDecimal> anticipoContratado = createNumber("anticipoContratado", java.math.BigDecimal.class);

    public final StringPath estado = createString("estado");

    public final NumberPath<java.math.BigDecimal> estimacionesPagadas = createNumber("estimacionesPagadas", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> estimacionesProgramadas = createNumber("estimacionesProgramadas", java.math.BigDecimal.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<java.math.BigDecimal> importeContratado = createNumber("importeContratado", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> pagosAnticipo = createNumber("pagosAnticipo", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> pagosAplicados = createNumber("pagosAplicados", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> saldoPendiente = createNumber("saldoPendiente", java.math.BigDecimal.class);

    public QViewContratoTotal(String variable) {
        super(ViewContratoTotal.class, forVariable(variable));
    }

    public QViewContratoTotal(Path<? extends ViewContratoTotal> path) {
        super(path.getType(), path.getMetadata());
    }

    public QViewContratoTotal(PathMetadata metadata) {
        super(ViewContratoTotal.class, metadata);
    }

}

