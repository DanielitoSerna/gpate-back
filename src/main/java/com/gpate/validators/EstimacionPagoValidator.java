package com.gpate.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.gpate.model.EstimacionPago;
import com.gpate.repository.ContratoRepository;
import com.gpate.repository.EstimacionPagoRepository;
import com.gpate.util.EstimacionPagoUtil;

@Component("beforeCreateEstimacionPagoValidator")
public class EstimacionPagoValidator implements Validator {

	private ContratoRepository contratoRepository;

	private EstimacionPagoRepository estimacionPagoRepository;

	@Override
	public boolean supports(Class<?> clazz) {
		return EstimacionPago.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		EstimacionPago estimacionPago = (EstimacionPago) target;
		EstimacionPagoUtil.calcularEstimacionPago(estimacionPago, estimacionPagoRepository, contratoRepository);
	}

	public EstimacionPagoValidator(ContratoRepository contratoRepository,
			EstimacionPagoRepository estimacionPagoRepository) {
		this.contratoRepository = contratoRepository;
		this.estimacionPagoRepository = estimacionPagoRepository;
	}

}
