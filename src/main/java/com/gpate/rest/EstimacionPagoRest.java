package com.gpate.rest;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gpate.services.impl.EstimacionPagoService;

@RestController
@RequestMapping("api")
@CrossOrigin("*")
public class EstimacionPagoRest {

	@Autowired
	private EstimacionPagoService estimacionPagoService;

	@DeleteMapping("eliminarEstimacionPago")
	public ResponseEntity<?> eliminarEstimacionPago(@RequestParam(name = "idEstimacion") Long idEstimacion) {
		String mensaje = estimacionPagoService.eliminarEstimacionPago(idEstimacion);
		if (!mensaje.contains("Error")) {
			return new ResponseEntity<>(mensaje, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(mensaje, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/excel/estadoCuenta")
	public void generarEstadoCuenta(@RequestParam(name = "contrato") Long contrato, HttpServletResponse response) {
		
	}

}
