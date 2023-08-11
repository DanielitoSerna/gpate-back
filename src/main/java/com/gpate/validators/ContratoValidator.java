package com.gpate.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.gpate.model.Contrato;

@Component("beforeCreateContratoValidator")
public class ContratoValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Contrato.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Contrato contrato = (Contrato) target;

		if (contrato.getFechaProgramadaEntrega() != null && contrato.getFechaSolicitudContrato() != null) {

			int milisecondsByDay = 86400000;

			int dias = (int) ((contrato.getFechaProgramadaEntrega().getTime()
					- contrato.getFechaSolicitudContrato().getTime()) / milisecondsByDay);

			contrato.setDiasProgramados(dias);
		}
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
	}

}
