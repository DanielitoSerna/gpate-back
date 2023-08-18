package com.gpate.services.impl;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gpate.model.Contrato;
import com.gpate.repository.ContratoRepository;
import com.gpate.services.IContratoService;
import com.gpate.util.ExcelGenerator;
import com.gpate.util.PDFGenerator;
import com.lowagie.text.DocumentException;

@Service
public class ContratoService implements IContratoService {

	@Autowired
	private ContratoRepositoryCustom contratoRepositoryCustom;
	
	@Autowired
	private ContratoRepository contratoRepository;

	@Override
	public void generatePdf(HttpServletResponse response, String proyecto, String folio, String especialidad,
			String proveedor, String estado) throws DocumentException, IOException {
		response.setContentType("application/pdf");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormat.format(new Date());
		String headerkey = "Content-Disposition";
		String headervalue = "attachment; filename=contratos_" + currentDateTime + "_.pdf";
		response.setHeader(headerkey, headervalue);
		List<Contrato> contratos = contratoRepositoryCustom.getContractsByFilters(proyecto, folio, especialidad,
				proveedor, estado);
		
		PDFGenerator generator = new PDFGenerator();
		generator.setContratos(contratos);
		generator.generate(response);
	}

	@Override
	public void generateExcel(HttpServletResponse response, String proyecto, String folio, String especialidad,
			String proveedor, String estado) throws IOException {
		response.setContentType("application/octet-stream");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
		
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=contratos_" + currentDateTime + "_.xlsx";
		response.setHeader(headerKey, headerValue);
		
		List<Contrato> contratos = contratoRepositoryCustom.getContractsByFilters(proyecto, folio, especialidad,
				proveedor, estado);
		ExcelGenerator generator = new ExcelGenerator(contratos);
		generator.generateExcelFile(response);
		
	}

	@Override
	public Contrato getInfoContrato(Long idContrato) {
		Contrato contrato = contratoRepository.findById(idContrato).get();
		
		return contrato; 
	}

}
