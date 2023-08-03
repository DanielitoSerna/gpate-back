package com.gpate.validators;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.gpate.model.Contrato;
import com.gpate.model.EstimacionPago;
import com.gpate.repository.ContratoRepository;

@Component("afterCreateEstimacionPagoValidator")
public class EstimacionPagoValidator implements Validator {

	private ContratoRepository contratoRepository;

	@Override
	public boolean supports(Class<?> clazz) {
		return EstimacionPago.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		EstimacionPago estimacionPago = (EstimacionPago) target;
		Contrato contrato = new Contrato();

		if (estimacionPago.getContrato() != null && estimacionPago.getImporte() != null
				&& (estimacionPago.getConcepto() != null && !estimacionPago.getConcepto().isEmpty()
						&& "ESTIMACIÓN".equals(estimacionPago.getConcepto()))) {
			
			contrato = contratoRepository.findById(estimacionPago.getContrato()).get();

			if (contrato.getId() != null) {
				BigDecimal total = contrato.getEstimacionesProgramadas();
				total = total.add(estimacionPago.getImporte());
				System.out.println(total);
				contrato.setEstimacionesProgramadas(total);

				contratoRepository.save(contrato);
			}
		}
	}

	public EstimacionPagoValidator(ContratoRepository contratoRepository) {
		this.contratoRepository = contratoRepository;
	}

}
