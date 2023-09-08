package com.gpate.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.gpate.model.Contrato;
import com.gpate.util.ContratoUtil;

@Component("beforeCreateContratoValidator")
public class ContratoValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Contrato.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Contrato contrato = (Contrato) target;

		ContratoUtil.obtenerDatosContrato(contrato);
	}

}
