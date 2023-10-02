package com.gpate.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QViewContrato is a Querydsl query type for ViewContrato
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QViewContrato extends EntityPathBase<ViewContrato> {

    private static final long serialVersionUID = -1665670024L;

    public static final QViewContrato viewContrato = new QViewContrato("viewContrato");

    public final NumberPath<java.math.BigDecimal> anticipoContratado = createNumber("anticipoContratado", java.math.BigDecimal.class);

    public final StringPath estado = createString("estado");

    public final NumberPath<java.math.BigDecimal> estimacionesPagadas = createNumber("estimacionesPagadas", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> estimacionesProgramadas = createNumber("estimacionesProgramadas", java.math.BigDecimal.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<java.math.BigDecimal> importeContratado = createNumber("importeContratado", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> pagosAnticipo = createNumber("pagosAnticipo", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> pagosAplicados = createNumber("pagosAplicados", java.math.BigDecimal.class);

    public final StringPath proyecto = createString("proyecto");

    public final NumberPath<java.math.BigDecimal> saldoPendiente = createNumber("saldoPendiente", java.math.BigDecimal.class);

    public QViewContrato(String variable) {
        super(ViewContrato.class, forVariable(variable));
    }

    public QViewContrato(Path<? extends ViewContrato> path) {
        super(path.getType(), path.getMetadata());
    }

    public QViewContrato(PathMetadata metadata) {
        super(ViewContrato.class, metadata);
    }

}

