package com.gpate.services;

import java.io.IOException;
import java.text.ParseException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.gpate.model.Contrato;
import com.lowagie.text.DocumentException;

public interface IContratoService {

	public void generatePdf(HttpServletResponse response, String proyecto, String folio, String especialidad,
			String proveedor, String estado) throws DocumentException, IOException;

	public void generateExcel(HttpServletResponse response, String proyecto, String folio, String especialidad,
			String proveedor, String estado) throws IOException;
	
	public Contrato getInfoContrato(Long idContrato);
	
	public String eliminarContrato(Long idContrato);
	
	public String uploadFile(MultipartFile  fileStorageProperties) throws ParseException;

}
