package com.gpate.rest;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gpate.services.impl.ContratoService;
import com.lowagie.text.DocumentException;

@RestController
@RequestMapping("api")
@CrossOrigin("*")
public class ContratoRest {

	@Autowired
	private ContratoService contratoService;

	@GetMapping("/pdf/contratos")
	public void generatePdf(@RequestParam(name = "proyecto") String proyecto,
			@RequestParam(name = "folio") String folio, @RequestParam(name = "especialidad") String especialidad,
			@RequestParam(name = "proveedor") String proveedor, @RequestParam(name = "estado") String estado,
			HttpServletResponse response) throws DocumentException, IOException {
		contratoService.generatePdf(response, proyecto, folio, especialidad, proveedor, estado);
	}

	public void generateExcel(@RequestParam(name = "proyecto") String proyecto,
			@RequestParam(name = "folio") String folio, @RequestParam(name = "especialidad") String especialidad,
			@RequestParam(name = "proveedor") String proveedor, @RequestParam(name = "estado") String estado,
			HttpServletResponse response) throws IOException {
		contratoService.generateExcel(response, proyecto, folio, especialidad, proveedor, estado);
	}

}
