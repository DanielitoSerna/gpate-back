package com.gpate.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="contratos", schema = "gpate")
public class Contrato {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "Anticipo contratado es obligatorio")
	@Column(name="anticipo_contratado")
	private BigDecimal anticipoContratado;

	@NotNull(message = "Centro de costo es obligatorio")
	@NotEmpty(message = "Centro de costo es obligatorio")
	@Column(name="centro_costo")
	private String centroCosto;

	@Column(name="dias_atencion")
	private Integer diasAtencion;

	@Column(name="dias_programados")
	private Integer diasProgramados;

	@Column(name="dias_vencimiento")
	private String diasVencimiento;

	private String especialidad;

	@Column(name="estimaciones_pagadas")
	private BigDecimal estimacionesPagadas;

	@Column(name="estimaciones_programadas")
	private BigDecimal estimacionesProgramadas;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_fallo")
	private Date fechaFallo;

	@NotNull(message = "Fecha entrega firmado por cliente-proveedor es obligatorio")
	@Temporal(TemporalType.DATE)
	@Column(name="fecha_firmado_cliente")
	private Date fechaFirmadoCliente;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_juridico")
	private Date fechaJuridico;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_programada_entrega")
	private Date fechaProgramadaEntrega;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_solicitud_contrato")
	private Date fechaSolicitudContrato;

	@NotNull(message = "Fecha de vencimiento del contrato es obligatorio")
	@Temporal(TemporalType.DATE)
	@Column(name="fecha_vencimiento_contrato")
	private Date fechaVencimientoContrato;

	@NotNull(message = "Folio es obligatorio")
	@NotEmpty(message = "Folio es obligatorio")
	private String folio;

	private String hipervinculo;

	@NotNull(message = "Importe contratado es obligatorio")
	@Column(name="importe_contratado")
	private BigDecimal importeContratado;

	private String observaciones;

	@Column(name="pagos_aplicados")
	private BigDecimal pagosAplicados;

	private String proveedor;

	@NotNull(message = "Proyecto es obligatorio")
	@NotEmpty(message = "Proyecto es obligatorio")
	private String proyecto;

	@Column(name="saldo_pendiente_contrato")
	private BigDecimal saldoPendienteContrato;

	@Column(name="status_general")
	private String statusGeneral;
	
	private String estado;
	
}
