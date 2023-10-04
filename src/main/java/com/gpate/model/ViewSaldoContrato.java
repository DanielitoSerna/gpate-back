package com.gpate.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="saldos_contrato", schema = "gpate")
public class ViewSaldoContrato {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="pendiente_pago_estimacion")
	private BigDecimal pendientePagoEstimacion;
	
	@Column(name="pendiente_pago_anticipo")
	private BigDecimal pendientePagoAnticipo;
	
	@Column(name="saldo_pendiente_contrato")
	private BigDecimal saldoPendienteContrato;
	
	@Column(name="estimaciones_pagadas")
	private BigDecimal estimacionesPagadas;
	
	@Column(name="pendiente_actual")
	private BigDecimal pendienteActual;

	private String proyecto;
	
	private String proveedor;

}
