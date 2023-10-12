package com.gpate.dtos;

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

	private String importe;

	private String numeroAbono;

	private String observaciones;
	
	private Long contrato;
	
	private String importeAbono;
	
	private String status;
	
	private String folio;
	
	private String importeBruto;
	
	private String retencionViciosOcultos;
	
	private String amortizacionAnticipo;
	
	private String iva;
	
	private String retencionIva;
	
	private String isr;
	
	private String deducciones;

}
