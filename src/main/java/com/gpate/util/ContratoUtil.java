package com.gpate.util;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.gpate.model.Contrato;
import com.gpate.model.EstimacionPago;
import com.gpate.repository.EstimacionPagoRepository;

public class ContratoUtil {

	public static void obtenerDatosContrato(Contrato contrato, EstimacionPagoRepository estimacionPagoRepository) {
		// DIAS PROGRAMADOS
		if (contrato.getFechaProgramadaEntrega() != null && contrato.getFechaSolicitudContrato() != null) {
			int milisecondsByDay = 86400000;
			int dias = (int) ((contrato.getFechaProgramadaEntrega().getTime()
					- contrato.getFechaSolicitudContrato().getTime()) / milisecondsByDay);
			contrato.setDiasProgramados(dias);
		}

		// DIAS DE ATENCION
		if (contrato.getFechaFirmadoCliente() != null && contrato.getFechaFallo() != null) {
			int milisecondsByDay = 86400000;
			int dias = (int) ((contrato.getFechaFirmadoCliente().getTime() - contrato.getFechaFallo().getTime())
					/ milisecondsByDay);
			contrato.setDiasAtencion(dias);
		}

		// DIAS VENCIMIENTO
		if (contrato.getFechaVencimientoContrato() != null) {
			int milisecondsByDay = 86400000;
			Date fechaActual = new Date();
			Integer dias = (int) ((contrato.getFechaVencimientoContrato().getTime() - fechaActual.getTime())
					/ milisecondsByDay);
			if (dias < 0) {
				contrato.setDiasVencimiento("CONTRATO VENCIDO (" + dias.toString().replace("-", "") + " DIAS)");
			} else {
				contrato.setDiasVencimiento(dias.toString() + " DIAS PARA SU VENCIMIENTO");
			}
		} else {
			contrato.setDiasVencimiento("FALTA FECHA DE VENCIMIENTO DE CONTRATO");
		}

		// STATUS GENERAL
		if (contrato.getFechaFallo() != null) {
			if (contrato.getFechaSolicitudContrato() != null) {
				if (contrato.getFechaProgramadaEntrega() != null) {
					if (contrato.getFechaJuridico() != null) {
						if (contrato.getFechaFirmadoCliente() != null) {
							contrato.setStatusGeneral("CONTRATO FIRMADO");
						} else {
							contrato.setStatusGeneral("PENDIENTE FIRMA CLIENTE PROVEEDOR");
						}
					} else {
						contrato.setStatusGeneral("EN JURÍDICO");
					}
				} else {
					contrato.setStatusGeneral("EN ESPERA DE ENTREGA PROGRAMADA");
				}
			} else {
				contrato.setStatusGeneral("EN ESPERA DE SOLICITUD");
			}
		} else {
			contrato.setStatusGeneral("EN ESPERA DE FALLO");
		}

		// RECALCULAR SALDO PENDIENTE
		if (contrato.getTieneImporte() != null) {
			if (!contrato.getTieneImporte()) {
				BigDecimal pagosAplicados = contrato.getPagosAplicados() != null ? contrato.getPagosAplicados()
						: new BigDecimal("0");
				BigDecimal montoContrato = contrato.getImporteContratado() != null ? contrato.getImporteContratado()
						: new BigDecimal("0");
				montoContrato = montoContrato.subtract(pagosAplicados);
				contrato.setSaldoPendienteContrato(montoContrato);
			} else {
				if (contrato.getId() != null) {
					BigDecimal sumatoriaImporteContrato = new BigDecimal(0);
					BigDecimal estimacionesProgramadas = new BigDecimal(0);
					BigDecimal pagosAplicados = new BigDecimal(0);
					BigDecimal saldoContrato = new BigDecimal(0);
					BigDecimal abonoPagado = new BigDecimal(0);
					List<EstimacionPago> estimacionPagos = estimacionPagoRepository.findByContrato(contrato.getId());
					for (EstimacionPago estimacionPago : estimacionPagos) {
						if (!"ABONO A ANTICIPO".equals(estimacionPago.getConcepto())
								&& !"ABONO A ESTIMACIÓN".equals(estimacionPago.getConcepto())) {
							sumatoriaImporteContrato = sumatoriaImporteContrato.add(estimacionPago.getImporte());
						}
						if ("ESTIMACIÓN".equals(estimacionPago.getConcepto())) {
							estimacionesProgramadas = estimacionesProgramadas.add(estimacionPago.getImporte());
						}
						if ("ABONO A CONTRATO".equals(estimacionPago.getConcepto())) {
							pagosAplicados = pagosAplicados.add(estimacionPago.getImporte());
						}
						if ("ABONO A ESTIMACIÓN".equals(estimacionPago.getConcepto())) {
							pagosAplicados = pagosAplicados.add(estimacionPago.getImporte());
						}
						if ("ABONO A ANTICIPO".equals(estimacionPago.getConcepto())) {
							abonoPagado = abonoPagado.add(estimacionPago.getImporte());
						}
					}
					saldoContrato = sumatoriaImporteContrato.subtract(pagosAplicados);
					contrato.setImporteContratado(sumatoriaImporteContrato);
					contrato.setEstimacionesProgramadas(estimacionesProgramadas);
					contrato.setPagosAplicados(pagosAplicados);
					contrato.setSaldoPendienteContrato(saldoContrato);
					contrato.setAnticipoPagado(abonoPagado);
				} else {
					contrato.setImporteContratado(new BigDecimal(0));
					contrato.setSaldoPendienteContrato(new BigDecimal(0));
				}

			}
		} else {
			BigDecimal pagosAplicados = contrato.getPagosAplicados() != null ? contrato.getPagosAplicados()
					: new BigDecimal("0");
			BigDecimal montoContrato = contrato.getImporteContratado() != null ? contrato.getImporteContratado()
					: new BigDecimal("0");
			montoContrato = montoContrato.subtract(pagosAplicados);
			contrato.setSaldoPendienteContrato(montoContrato);
		}

	}

}
