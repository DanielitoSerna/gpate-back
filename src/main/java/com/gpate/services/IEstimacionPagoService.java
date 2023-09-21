package com.gpate.services;

import java.io.IOException;
import java.text.ParseException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

public interface IEstimacionPagoService {
	
	public String eliminarEstimacionPago(Long id);
	
	public void generarEstadoCuenta(HttpServletResponse response) throws IOException;
	
	public String uploadFile(MultipartFile fileStorageProperties) throws ParseException;

}
