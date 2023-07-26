package com.gpate.rest;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
	public void generatePdf(HttpServletResponse response) throws DocumentException, IOException {
		contratoService.generatePdf(response);
	}

}
