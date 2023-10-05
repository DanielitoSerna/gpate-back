package com.gpate.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QViewSaldoContratoTotal is a Querydsl query type for ViewSaldoContratoTotal
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QViewSaldoContratoTotal extends EntityPathBase<ViewSaldoContratoTotal> {

    private static final long serialVersionUID = -676683397L;

    public static final QViewSaldoContratoTotal viewSaldoContratoTotal = new QViewSaldoContratoTotal("viewSaldoContratoTotal");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<java.math.BigDecimal> pendienteActual = createNumber("pendienteActual", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> pendientePagoAnticipo = createNumber("pendientePagoAnticipo", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> pendientePagoEstimacion = createNumber("pendientePagoEstimacion", java.math.BigDecimal.class);

    public final StringPath proyecto = createString("proyecto");

    public final NumberPath<java.math.BigDecimal> saldoPendienteContrato = createNumber("saldoPendienteContrato", java.math.BigDecimal.class);

    public QViewSaldoContratoTotal(String variable) {
        super(ViewSaldoContratoTotal.class, forVariable(variable));
    }

    public QViewSaldoContratoTotal(Path<? extends ViewSaldoContratoTotal> path) {
        super(path.getType(), path.getMetadata());
    }

    public QViewSaldoContratoTotal(PathMetadata metadata) {
        super(ViewSaldoContratoTotal.class, metadata);
    }

}

