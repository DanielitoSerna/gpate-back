package com.gpate.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QContrato is a Querydsl query type for Contrato
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QContrato extends EntityPathBase<Contrato> {

    private static final long serialVersionUID = 1855856179L;

    public static final QContrato contrato = new QContrato("contrato");

    public final NumberPath<java.math.BigDecimal> anticipoContratado = createNumber("anticipoContratado", java.math.BigDecimal.class);

    public final StringPath centroCosto = createString("centroCosto");

    public final NumberPath<Integer> diasAtencion = createNumber("diasAtencion", Integer.class);

    public final NumberPath<Integer> diasProgramados = createNumber("diasProgramados", Integer.class);

    public final StringPath diasVencimiento = createString("diasVencimiento");

    public final StringPath especialidad = createString("especialidad");

    public final NumberPath<java.math.BigDecimal> estimacionesPagadas = createNumber("estimacionesPagadas", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> estimacionesProgramadas = createNumber("estimacionesProgramadas", java.math.BigDecimal.class);

    public final DatePath<java.util.Date> fechaFallo = createDate("fechaFallo", java.util.Date.class);

    public final DatePath<java.util.Date> fechaFirmadoCliente = createDate("fechaFirmadoCliente", java.util.Date.class);

    public final DatePath<java.util.Date> fechaJuridico = createDate("fechaJuridico", java.util.Date.class);

    public final DatePath<java.util.Date> fechaProgramadaEntrega = createDate("fechaProgramadaEntrega", java.util.Date.class);

    public final DatePath<java.util.Date> fechaSolicitudContrato = createDate("fechaSolicitudContrato", java.util.Date.class);

    public final DatePath<java.util.Date> fechaVencimientoContrato = createDate("fechaVencimientoContrato", java.util.Date.class);

    public final StringPath folio = createString("folio");

    public final StringPath hipervinculo = createString("hipervinculo");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<java.math.BigDecimal> importeContratado = createNumber("importeContratado", java.math.BigDecimal.class);

    public final StringPath observaciones = createString("observaciones");

    public final NumberPath<java.math.BigDecimal> pagosAplicados = createNumber("pagosAplicados", java.math.BigDecimal.class);

    public final StringPath proveedor = createString("proveedor");

    public final StringPath proyecto = createString("proyecto");

    public final NumberPath<java.math.BigDecimal> saldoPendienteContrato = createNumber("saldoPendienteContrato", java.math.BigDecimal.class);

    public final StringPath statusGeneral = createString("statusGeneral");

    public QContrato(String variable) {
        super(Contrato.class, forVariable(variable));
    }

    public QContrato(Path<? extends Contrato> path) {
        super(path.getType(), path.getMetadata());
    }

    public QContrato(PathMetadata metadata) {
        super(Contrato.class, metadata);
    }

}

