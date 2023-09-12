package com.gpate.services.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gpate.model.Contrato;
import com.gpate.model.EstimacionPago;
import com.gpate.repository.ContratoRepository;
import com.gpate.repository.EstimacionPagoRepository;
import com.gpate.services.IEstimacionPagoService;

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
									estimacionPago2.setNumeroAbono("0"+numeroAbono);
								} else {
									estimacionPago2.setNumeroAbono(numeroAbono+"");
								}
								numeroAbono++;
								estimacionPagoRepository.save(estimacionPago2);
							}
						}
					}
					
					if("ESTIMACIÓN".equals(concepto)) {
						estimacionProgramada = estimacionProgramada.subtract(importe);
						contrato.setEstimacionesProgramadas(estimacionProgramada);
					}
					if("ABONO A CONTRATO".equals(concepto)) {
						pagoAplicado = pagoAplicado.subtract(importe);
						saldoPendiente = montoContrato.subtract(pagoAplicado);
						contrato.setPagosAplicados(pagoAplicado);
						contrato.setSaldoPendienteContrato(saldoPendiente);
					}
					if ("ABONO A ANTICIPO".equals(concepto)) {
						pagoAplicado = pagoAplicado.subtract(importe);
						saldoPendiente = montoContrato.subtract(pagoAplicado);
						contrato.setPagosAplicados(pagoAplicado);
						contrato.setSaldoPendienteContrato(saldoPendiente);
					}
					
					contratoRepository.save(contrato);
				}
			}
			
		} catch (Exception e) {
			mensaje = "No encontrado.";
		}
		
		return mensaje;
	}

}
