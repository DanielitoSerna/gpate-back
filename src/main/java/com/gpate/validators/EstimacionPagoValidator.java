package com.gpate.validators;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.gpate.model.Contrato;
import com.gpate.model.EstimacionPago;
import com.gpate.repository.ContratoRepository;
import com.gpate.repository.EstimacionPagoRepository;

@Component("afterCreateEstimacionPagoValidator")
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
		Contrato contrato = new Contrato();

		if (estimacionPago.getContrato() != null && estimacionPago.getImporte() != null
				&& (estimacionPago.getConcepto() != null && !estimacionPago.getConcepto().isEmpty()
						&& "ESTIMACIÓN".equals(estimacionPago.getConcepto()))) {

			contrato = contratoRepository.findById(estimacionPago.getContrato()).get();

			if (contrato.getId() != null) {
				BigDecimal total = contrato.getEstimacionesProgramadas() != null ? contrato.getEstimacionesProgramadas()
						: new BigDecimal("0");
				total = total.add(estimacionPago.getImporte());
				System.out.println(total);
				contrato.setEstimacionesProgramadas(total);

				contratoRepository.save(contrato);
			}
		}

		if (estimacionPago.getContrato() != null && estimacionPago.getImporte() != null
				&& (estimacionPago.getConcepto() != null && !estimacionPago.getConcepto().isEmpty()
						&& "ABONO".equals(estimacionPago.getConcepto()))) {
			contrato = contratoRepository.findById(estimacionPago.getContrato()).get();

			if (contrato.getId() != null) {
				BigDecimal total = contrato.getEstimacionesPagadas() != null ? contrato.getEstimacionesPagadas()
						: new BigDecimal("0");
				total = total.add(estimacionPago.getImporte());
				System.out.println(total);
				contrato.setEstimacionesPagadas(total);

				contratoRepository.save(contrato);
			}

			List<EstimacionPago> estimacionPagos = estimacionPagoRepository
					.findByNumeroAbonoAndContrato(estimacionPago.getNumeroAbono(), estimacionPago.getContrato());

			for (EstimacionPago estimacionPago2 : estimacionPagos) {
				if (estimacionPago2.getConcepto().equals("ESTIMACIÓN")
						&& estimacionPago2.getNumeroAbono().equals(estimacionPago.getNumeroAbono())
						&& estimacionPago2.getContrato().equals(estimacionPago.getContrato())) {
					if (estimacionPago2.getImporteAbono() != null ) {
						BigDecimal total = estimacionPago2.getImporteAbono();
						total = total.add(estimacionPago.getImporte());
						estimacionPago2.setImporteAbono(total);
					} else {
						estimacionPago2.setImporteAbono(estimacionPago.getImporte());
					}
					System.out.println("ESTIMACIÓN ACTUALIZADA");
					estimacionPagoRepository.save(estimacionPago2);
				}
			}
			
//			
		}
	}

	public EstimacionPagoValidator(ContratoRepository contratoRepository,
			EstimacionPagoRepository estimacionPagoRepository) {
		this.contratoRepository = contratoRepository;
		this.estimacionPagoRepository = estimacionPagoRepository;
	}

}
