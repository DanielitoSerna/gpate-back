package com.gpate.services;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

public interface IEstimacionPagoService {
	
	public String eliminarEstimacionPago(Long id);
	
	public void generarEstadoCuenta(HttpServletResponse response) throws IOException;

}
