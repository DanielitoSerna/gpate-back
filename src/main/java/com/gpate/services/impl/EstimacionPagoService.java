package com.gpate.services.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gpate.dtos.EstimacionPagoDto;
import com.gpate.model.Contrato;
import com.gpate.model.EstimacionPago;
import com.gpate.repository.ContratoRepository;
import com.gpate.repository.EstimacionPagoRepository;
import com.gpate.services.IEstimacionPagoService;
import com.gpate.util.ExcelEstimacionPagoGenerator;

@Service
public class EstimacionPagoService implements IEstimacionPagoService {

	@Autowired
	private EstimacionPagoRepository estimacionPagoRepository;

	@Autowired
	private ContratoRepository contratoRepository;

	@Override
	public String eliminarEstimacionPago(Long id) {
		String mensaje = "";
		EstimacionPago estimacionPago = null;
		try {
			estimacionPago = estimacionPagoRepository.findById(id).get();
			Long idContrato = 0L;
			BigDecimal importe = estimacionPago.getImporte();
			String concepto = estimacionPago.getConcepto();
			Integer numeroAbono = Integer.parseInt(estimacionPago.getNumeroAbono());
			if (estimacionPago != null) {
				idContrato = estimacionPago.getContrato();
				estimacionPagoRepository.delete(estimacionPago);
				List<EstimacionPago> estimacionPagosAbonoEstimacion = estimacionPagoRepository
						.findByNumeroAbonoAndContrato(estimacionPago.getNumeroAbono(), idContrato);
				BigDecimal sumaAbonoEstimacion = new BigDecimal(0);
				for (EstimacionPago estimacionPagoAbonoEstimacion : estimacionPagosAbonoEstimacion) {
					if("ESTIMACIÓN".equals(concepto) && "ABONO A ESTIMACIÓN".equals(estimacionPagoAbonoEstimacion.getConcepto())) {
						sumaAbonoEstimacion = sumaAbonoEstimacion.add(estimacionPagoAbonoEstimacion.getImporte());
						estimacionPagoRepository.delete(estimacionPagoAbonoEstimacion);
					}
				}
				Contrato contrato = contratoRepository.findById(idContrato).get();
				if (contrato != null) {
					BigDecimal estimacionProgramada = contrato.getEstimacionesProgramadas();
					BigDecimal estimcacionPagada = contrato.getEstimacionesPagadas();
					BigDecimal pagoAplicado = contrato.getPagosAplicados();
					BigDecimal saldoPendiente = contrato.getSaldoPendienteContrato();
					BigDecimal montoContrato = contrato.getImporteContratado();

					List<EstimacionPago> estimacionPagos = estimacionPagoRepository.findByContrato(idContrato);
					for (EstimacionPago estimacionPago2 : estimacionPagos) {
						Integer numeroAbono2 = Integer.parseInt(estimacionPago2.getNumeroAbono());
						if (estimacionPago2.getConcepto().equals(concepto)) {
							if (numeroAbono < numeroAbono2) {
								if (numeroAbono < 10) {
									estimacionPago2.setNumeroAbono("0" + numeroAbono);
								} else {
									estimacionPago2.setNumeroAbono(numeroAbono + "");
								}
								numeroAbono++;
								estimacionPagoRepository.save(estimacionPago2);
							}
						}
					}

					if ("ESTIMACIÓN".equals(concepto)) {
						estimacionProgramada = estimacionProgramada.subtract(importe);
						estimcacionPagada = estimcacionPagada.subtract(sumaAbonoEstimacion);
						pagoAplicado = pagoAplicado.subtract(sumaAbonoEstimacion);
						if (contrato.getTieneImporte() != null) {
							if (contrato.getTieneImporte()) {
								montoContrato = montoContrato.subtract(importe);
							}
						}
						saldoPendiente = montoContrato.subtract(pagoAplicado);
						contrato.setEstimacionesProgramadas(estimacionProgramada);
						contrato.setEstimacionesPagadas(estimcacionPagada);
						contrato.setPagosAplicados(pagoAplicado);
						contrato.setSaldoPendienteContrato(saldoPendiente);
					}
					if ("ABONO A ESTIMACIÓN".equals(concepto)) {
						List<EstimacionPago> estimacionPagosEstimacion = estimacionPagoRepository
								.findByNumeroAbonoAndContrato(estimacionPago.getNumeroAbono(), idContrato);
						for (EstimacionPago estimacionPagoEstimacion : estimacionPagosEstimacion) {
							if("ESTIMACIÓN".equals(estimacionPagoEstimacion.getConcepto())) {
								BigDecimal importeEstimacion = estimacionPagoEstimacion.getImporteAbono();
								importeEstimacion = importeEstimacion.subtract(importe);
								estimacionPagoEstimacion.setImporteAbono(importeEstimacion);
								estimacionPagoRepository.save(estimacionPagoEstimacion);
							}
						}
						estimcacionPagada = estimcacionPagada.subtract(importe);
						pagoAplicado = pagoAplicado.subtract(importe);
						if (contrato.getTieneImporte() != null) {
							if (contrato.getTieneImporte()) {
								montoContrato = montoContrato.subtract(importe);
							}
						}
						saldoPendiente = montoContrato.subtract(pagoAplicado);
						contrato.setEstimacionesPagadas(estimcacionPagada);
						contrato.setPagosAplicados(pagoAplicado);
						contrato.setSaldoPendienteContrato(saldoPendiente);
					}
					if ("ABONO A CONTRATO".equals(concepto)) {
						pagoAplicado = pagoAplicado.subtract(importe);
						if (contrato.getTieneImporte() != null) {
							if (contrato.getTieneImporte()) {
								montoContrato = montoContrato.subtract(importe);
							}
						}
						saldoPendiente = montoContrato.subtract(pagoAplicado);
						contrato.setPagosAplicados(pagoAplicado);
						contrato.setSaldoPendienteContrato(saldoPendiente);
					}
					if ("ABONO A ANTICIPO".equals(concepto)) {
						pagoAplicado = pagoAplicado.subtract(importe);
						if (contrato.getTieneImporte() != null) {
							if (contrato.getTieneImporte()) {
								montoContrato = montoContrato.subtract(importe);
							}
						}
						saldoPendiente = montoContrato.subtract(pagoAplicado);
						contrato.setPagosAplicados(pagoAplicado);
						contrato.setSaldoPendienteContrato(saldoPendiente);
					}
					contratoRepository.save(contrato);
				}
			}
			mensaje = "Ítem eliminado correctamente.";
		} catch (Exception e) {
			mensaje = "ïtem no encontrado.";
		}

		return mensaje;
	}

	@Override
	public void generarEstadoCuenta(HttpServletResponse response) throws IOException {
		response.setContentType("application/octet-stream");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
		
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=estado_cuenta_" + currentDateTime + "_.xlsx";
		response.setHeader(headerKey, headerValue);
		
		List<EstimacionPago> estimacionPagos = estimacionPagoRepository.findAll();
		List<EstimacionPagoDto> estimacionPagoDtos = new ArrayList<>();
		
		for (EstimacionPago estimacionPago : estimacionPagos) {
			EstimacionPagoDto estimacionPagoDto = new EstimacionPagoDto();
			Contrato objContrato = contratoRepository.findById(estimacionPago.getContrato()).get();
			estimacionPagoDto.setConcepto(estimacionPago.getConcepto());
			estimacionPagoDto.setFolio(objContrato.getFolio());
			
			String fechaOPeracion = dateFormatter.format(estimacionPago.getFechaOperacion());
			
			estimacionPagoDto.setFechaOperacion(fechaOPeracion);
			estimacionPagoDto.setHipervinculo(estimacionPago.getHipervinculo());
			
			Locale locale = new Locale("en", "US");
			NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);

			DecimalFormat df = new DecimalFormat("#.##");
			df.setRoundingMode(RoundingMode.CEILING);
			
			estimacionPagoDto.setImporte(numberFormat.format(estimacionPago.getImporte().doubleValue()));
			estimacionPagoDto.setImporteAbono(estimacionPago.getImporteAbono());
			estimacionPagoDto.setNumeroAbono(estimacionPago.getNumeroAbono());
			estimacionPagoDto.setObservaciones(estimacionPago.getObservaciones());
			
			estimacionPagoDtos.add(estimacionPagoDto);
		}
		ExcelEstimacionPagoGenerator estimacionPagoGenerator = new ExcelEstimacionPagoGenerator(estimacionPagoDtos);
		estimacionPagoGenerator.generateExcelFile(response);
	}

}
