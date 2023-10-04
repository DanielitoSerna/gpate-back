package com.gpate.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QViewSaldoContrato is a Querydsl query type for ViewSaldoContrato
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QViewSaldoContrato extends EntityPathBase<ViewSaldoContrato> {

    private static final long serialVersionUID = -1129812151L;

    public static final QViewSaldoContrato viewSaldoContrato = new QViewSaldoContrato("viewSaldoContrato");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<java.math.BigDecimal> pendienteActual = createNumber("pendienteActual", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> pendientePagoAnticipo = createNumber("pendientePagoAnticipo", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> pendientePagoEstimacion = createNumber("pendientePagoEstimacion", java.math.BigDecimal.class);

    public final StringPath proveedor = createString("proveedor");

    public final StringPath proyecto = createString("proyecto");

    public final NumberPath<java.math.BigDecimal> saldoPendienteContrato = createNumber("saldoPendienteContrato", java.math.BigDecimal.class);

    public QViewSaldoContrato(String variable) {
        super(ViewSaldoContrato.class, forVariable(variable));
    }

    public QViewSaldoContrato(Path<? extends ViewSaldoContrato> path) {
        super(path.getType(), path.getMetadata());
    }

    public QViewSaldoContrato(PathMetadata metadata) {
        super(ViewSaldoContrato.class, metadata);
    }

}

