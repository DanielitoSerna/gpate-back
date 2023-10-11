package com.gpate.enums;

public enum TipoConceptoEnum {
	
	ESTIMACION(1L, "ESTIMACIÓN"),
	ABONO_ESTIMACION(2L, "ABONO A ESTIMACIÓN"),
	ABONO_ANTICIPO(3L, "ABONO A ANTICIPO"),
	ABONO_CONTRATO(4L, "ABONO A CONTRATO");
	
	private Long id;
	private String descripcion;
	
	private TipoConceptoEnum(Long id, String descripcion) {
		this.id = id;
		this.descripcion = descripcion;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	

}
