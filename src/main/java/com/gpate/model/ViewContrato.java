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
@Table(name="contratos_view", schema = "gpate")
public class ViewContrato {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String proyecto;
	
	@Column(name="importe_contratado")
	private BigDecimal importeContratado;
	
	@Column(name="anticipo_contratado")
	private BigDecimal anticipoContratado;
	
	@Column(name="estimaciones_programadas")
	private BigDecimal estimacionesProgramadas;
	
	@Column(name="estimaciones_pagadas")
	private BigDecimal estimacionesPagadas;
	
	@Column(name="pagos_aplicados")
	private BigDecimal pagosAplicados;
	
	@Column(name="saldo_pendiente_contrato")
	private BigDecimal saldoPendiente;
	
	@Column(name="pago_anticipo")
	private BigDecimal pagosAnticipo;
	
	private String estado;

}
