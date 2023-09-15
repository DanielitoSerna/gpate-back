package com.gpate.rest;

import java.io.IOException;
import java.text.ParseException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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

	@GetMapping("/excel/contratos")
	public void generateExcel(@RequestParam(name = "proyecto") String proyecto,
			@RequestParam(name = "folio") String folio, @RequestParam(name = "especialidad") String especialidad,
			@RequestParam(name = "proveedor") String proveedor, @RequestParam(name = "estado") String estado,
			HttpServletResponse response) throws IOException {
		contratoService.generateExcel(response, proyecto, folio, especialidad, proveedor, estado);
	}
	
	@DeleteMapping("eliminarContrato")
	public ResponseEntity<?> eliminarContrato(@RequestParam(name = "idContrato") Long idContrato) {
		String mensaje = contratoService.eliminarContrato(idContrato);
		if (!mensaje.contains("Error")) {
			return new ResponseEntity<>(mensaje, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(mensaje, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/cargarContrato")
	public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) throws ParseException {
		String fileName = contratoService.uploadFile(file);
		ServletUriComponentsBuilder.fromCurrentContextPath().path(fileName).toUriString();
		if (!fileName.contains("Error")) {
			return new ResponseEntity<>(fileName, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(fileName, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
