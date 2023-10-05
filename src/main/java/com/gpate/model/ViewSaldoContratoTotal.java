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
@Table(name="saldos_contratos_total", schema = "gpate")
public class ViewSaldoContratoTotal {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="total_pendiente_pago_estimacion")
	private BigDecimal pendientePagoEstimacion;
	
	@Column(name="total_pendiente_pago_anticipo")
	private BigDecimal pendientePagoAnticipo;
	
	@Column(name="total_pendiente_contrato")
	private BigDecimal saldoPendienteContrato;
	
	@Column(name="total_pendiente_actual")
	private BigDecimal pendienteActual;

	private String proyecto;

}
