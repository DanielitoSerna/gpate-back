package com.gpate.util;

import java.math.BigDecimal;
import java.util.Date;

import com.gpate.model.Contrato;

public class ContratoUtil {
	
	public static void obtenerDatosContrato(Contrato contrato) {
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
		BigDecimal pagosAplicados = contrato.getPagosAplicados() != null ? contrato.getPagosAplicados()
				: new BigDecimal("0");
		BigDecimal montoContrato = contrato.getImporteContratado() != null ? contrato.getImporteContratado()
				: new BigDecimal("0");

		montoContrato = montoContrato.subtract(pagosAplicados);

		contrato.setSaldoPendienteContrato(montoContrato);
	}

}
