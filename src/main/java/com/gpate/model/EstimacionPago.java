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
@Table(name="estimacion_pago", schema = "gpate")
public class EstimacionPago {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "Concepto es obligatorio")
	@NotEmpty(message = "Concepto es obligatorio")
	private String concepto;

	@NotNull(message = "Fecha operación es obligatorio")
	@Temporal(TemporalType.DATE)
	@Column(name="fecha_operacion")
	private Date fechaOperacion;

	private String hipervinculo;

	@NotNull(message = "Importe es obligatorio")
	private BigDecimal importe;

	@NotNull(message = "Número abono es obligatorio")
	@NotEmpty(message = "Número abono es obligatorio")
	@Column(name="numero_abono")
	private String numeroAbono;

	private String observaciones;
	
	@Column(name = "id_contrato")
	private Long contrato;
	
	@Column(name = "importe_abono")
	private BigDecimal importeAbono;
	
	@Column(name = "importe_bruto")
	private BigDecimal importeBruto;
	
	@Column(name = "retencion_vicios_ocultos")
	private BigDecimal retencionViciosOcultos;
	
	@Column(name = "amortizacion_anticipo")
	private BigDecimal amortizacionAnticipo;
	
	private BigDecimal iva;
	
	@Column(name = "retencion_iva")
	private BigDecimal retencionIva;
	
	private BigDecimal isr;
	
	private BigDecimal deducciones;

}
