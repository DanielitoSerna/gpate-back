package com.gpate.dtos;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class EstimacionPagoDto {
	
	private Long id;

	private String concepto;

	private String fechaOperacion;

	private String hipervinculo;

	private BigDecimal importe;

	private String numeroAbono;

	private String observaciones;
	
	private Long contrato;
	
	private BigDecimal importeAbono;
	
	private String status;

}
