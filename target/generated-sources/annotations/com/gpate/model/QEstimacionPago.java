package com.gpate.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QEstimacionPago is a Querydsl query type for EstimacionPago
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEstimacionPago extends EntityPathBase<EstimacionPago> {

    private static final long serialVersionUID = 635475020L;

    public static final QEstimacionPago estimacionPago = new QEstimacionPago("estimacionPago");

    public final StringPath concepto = createString("concepto");

    public final NumberPath<Long> contrato = createNumber("contrato", Long.class);

    public final DatePath<java.util.Date> fechaOperacion = createDate("fechaOperacion", java.util.Date.class);

    public final StringPath hipervinculo = createString("hipervinculo");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<java.math.BigDecimal> importe = createNumber("importe", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> importeAbono = createNumber("importeAbono", java.math.BigDecimal.class);

    public final StringPath numeroAbono = createString("numeroAbono");

    public final StringPath observaciones = createString("observaciones");

    public QEstimacionPago(String variable) {
        super(EstimacionPago.class, forVariable(variable));
    }

    public QEstimacionPago(Path<? extends EstimacionPago> path) {
        super(path.getType(), path.getMetadata());
    }

    public QEstimacionPago(PathMetadata metadata) {
        super(EstimacionPago.class, metadata);
    }

}

