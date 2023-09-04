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
		Contrato contrato = new Contrato();
		EstimacionPago estimacionPagoBD = new EstimacionPago();

		// EDITAR
		if (estimacionPago.getId() != null) {

			// CALCULAR ESTIMACIONES PROGRAMADAS
			if (estimacionPago.getContrato() != null && estimacionPago.getImporte() != null
					&& (estimacionPago.getConcepto() != null && !estimacionPago.getConcepto().isEmpty()
							&& "ESTIMACIÓN".equals(estimacionPago.getConcepto()))) {
				estimacionPagoBD = estimacionPagoRepository.findById(estimacionPago.getId()).get();

				contrato = contratoRepository.findById(estimacionPago.getContrato()).get();

				if (estimacionPago.getImporte().compareTo(estimacionPagoBD.getImporte()) == 1) {
					if (contrato.getId() != null) {
						BigDecimal total = contrato.getEstimacionesProgramadas() != null
								? contrato.getEstimacionesProgramadas()
								: new BigDecimal("0");
						BigDecimal estimacionImporteBD = estimacionPagoBD.getImporte() != null
								? estimacionPagoBD.getImporte()
								: new BigDecimal("0");
						BigDecimal estimacionImporte = estimacionPago.getImporte();
						estimacionImporte = estimacionImporte.subtract(estimacionImporteBD);
						
						total = total.add(estimacionImporte);
						System.out.println("Total Estimación programada. " + total);
						contrato.setEstimacionesProgramadas(total);

						contratoRepository.save(contrato);

						total = new BigDecimal("0");
					}
				} else {
					if (estimacionPago.getImporte().compareTo(estimacionPagoBD.getImporte()) == -1) {
						if (contrato.getId() != null) {
							BigDecimal total = contrato.getEstimacionesProgramadas() != null
									? contrato.getEstimacionesProgramadas()
									: new BigDecimal("0");
							BigDecimal estimacionImporteBD = estimacionPagoBD.getImporte() != null
									? estimacionPagoBD.getImporte()
									: new BigDecimal("0");
							BigDecimal estimacionImporte = estimacionPago.getImporte();
							estimacionImporte = estimacionImporte.subtract(estimacionImporteBD);
							
							total = total.add(estimacionImporte);

							System.out.println("Total Estimación programada. " + total);
							contrato.setEstimacionesProgramadas(total);

							contratoRepository.save(contrato);

							total = new BigDecimal("0");
						}
					}
				}
			}

			// CALCULAR ESTIMACIONES PAGADAS
			if (estimacionPago.getContrato() != null && estimacionPago.getImporte() != null
					&& (estimacionPago.getConcepto() != null && !estimacionPago.getConcepto().isEmpty()
							&& "ABONO A ESTIMACIÓN".equals(estimacionPago.getConcepto()))) {
				contrato = contratoRepository.findById(estimacionPago.getContrato()).get();

				estimacionPagoBD = estimacionPagoRepository.findById(estimacionPago.getId()).get();

				if (estimacionPago.getImporte().compareTo(estimacionPagoBD.getImporte()) == 1) {
					if (contrato.getId() != null) {
						BigDecimal estimacionPagada = contrato.getEstimacionesPagadas() != null
								? contrato.getEstimacionesPagadas()
								: new BigDecimal("0");
						BigDecimal importe = estimacionPago.getImporte() != null ? estimacionPago.getImporte()
								: new BigDecimal("0");
						importe = importe.subtract(estimacionPagada);

						estimacionPagada = estimacionPagada.add(importe);
						System.out.println("Total Estimación pagada. " + estimacionPagada);

						BigDecimal importePago = estimacionPago.getImporte() != null ? estimacionPago.getImporte()
								: new BigDecimal("0");
						BigDecimal estimacionImporteBD = estimacionPagoBD.getImporte() != null
								? estimacionPagoBD.getImporte()
								: new BigDecimal("0");
						
						BigDecimal totalPagoAplicado = contrato.getPagosAplicados() != null
								? contrato.getPagosAplicados()
								: new BigDecimal("0");
						importePago = importePago.subtract(estimacionImporteBD);

						totalPagoAplicado = totalPagoAplicado.add(importePago);
						BigDecimal montoContrato = contrato.getImporteContratado() != null
								? contrato.getImporteContratado()
								: new BigDecimal("0");

						montoContrato = montoContrato.subtract(totalPagoAplicado);

						contrato.setEstimacionesPagadas(estimacionPagada);
						contrato.setPagosAplicados(totalPagoAplicado);
						contrato.setSaldoPendienteContrato(montoContrato);
						contratoRepository.save(contrato);
					}

					List<EstimacionPago> estimacionPagos = estimacionPagoRepository.findByNumeroAbonoAndContrato(
							estimacionPago.getNumeroAbono(), estimacionPago.getContrato());

					for (EstimacionPago estimacionPago2 : estimacionPagos) {
						if (estimacionPago2.getConcepto().equals("ESTIMACIÓN")
								&& estimacionPago2.getNumeroAbono().equals(estimacionPago.getNumeroAbono())
								&& estimacionPago2.getContrato().equals(estimacionPago.getContrato())) {
							if (estimacionPago2.getImporteAbono() != null) {
								BigDecimal total = estimacionPago2.getImporteAbono();
								BigDecimal importe = estimacionPago.getImporte() != null ? estimacionPago.getImporte()
										: new BigDecimal("0");
								importe = importe.subtract(total);
								total = total.add(importe);
								estimacionPago2.setImporteAbono(total);
								total = new BigDecimal("0");
							} else {
								estimacionPago2.setImporteAbono(estimacionPago.getImporte());
							}
							System.out.println("ESTIMACIÓN ACTUALIZADA");
							estimacionPagoRepository.save(estimacionPago2);
						}
					}
				} else {
					if (estimacionPago.getImporte().compareTo(estimacionPagoBD.getImporte()) == -1) {
						if (contrato.getId() != null) {
							BigDecimal estimacionPagada = contrato.getEstimacionesPagadas() != null
									? contrato.getEstimacionesPagadas()
									: new BigDecimal("0");
							BigDecimal importe = estimacionPago.getImporte() != null ? estimacionPago.getImporte()
									: new BigDecimal("0");
							importe = importe.subtract(estimacionPagada);

							estimacionPagada = estimacionPagada.add(importe);
							System.out.println("Total Estimación pagada. " + estimacionPagada);

							BigDecimal importePago = estimacionPago.getImporte() != null ? estimacionPago.getImporte()
									: new BigDecimal("0");
							BigDecimal estimacionImporteBD = estimacionPagoBD.getImporte() != null
									? estimacionPagoBD.getImporte()
									: new BigDecimal("0");
							
							BigDecimal totalPagoAplicado = contrato.getPagosAplicados() != null
									? contrato.getPagosAplicados()
									: new BigDecimal("0");
							importePago = importePago.subtract(estimacionImporteBD);

							totalPagoAplicado = totalPagoAplicado.add(importePago);
							BigDecimal montoContrato = contrato.getImporteContratado() != null
									? contrato.getImporteContratado()
									: new BigDecimal("0");

							montoContrato = montoContrato.subtract(totalPagoAplicado);

							contrato.setEstimacionesPagadas(estimacionPagada);
							contrato.setPagosAplicados(totalPagoAplicado);
							contrato.setSaldoPendienteContrato(montoContrato);
							contratoRepository.save(contrato);

						}
						List<EstimacionPago> estimacionPagos = estimacionPagoRepository.findByNumeroAbonoAndContrato(
								estimacionPago.getNumeroAbono(), estimacionPago.getContrato());

						for (EstimacionPago estimacionPago2 : estimacionPagos) {
							if (estimacionPago2.getConcepto().equals("ESTIMACIÓN")
									&& estimacionPago2.getNumeroAbono().equals(estimacionPago.getNumeroAbono())
									&& estimacionPago2.getContrato().equals(estimacionPago.getContrato())) {
								if (estimacionPago2.getImporteAbono() != null) {
									BigDecimal total = estimacionPago2.getImporteAbono();
									BigDecimal importe = estimacionPago.getImporte() != null
											? estimacionPago.getImporte()
											: new BigDecimal("0");
									importe = importe.subtract(total);
									total = total.add(importe);
									estimacionPago2.setImporteAbono(total);
									total = new BigDecimal("0");
								} else {
									estimacionPago2.setImporteAbono(estimacionPago.getImporte());
								}
								System.out.println("ESTIMACIÓN ACTUALIZADA");
								estimacionPagoRepository.save(estimacionPago2);
							}
						}
					}
				}
			}
			// CALCULAR PAGOS APLICADOS - ANTICIPO
			if (estimacionPago.getContrato() != null && estimacionPago.getImporte() != null
					&& (estimacionPago.getConcepto() != null && !estimacionPago.getConcepto().isEmpty()
							&& "ABONO A ANTICIPO".equals(estimacionPago.getConcepto()))) {
				contrato = contratoRepository.findById(estimacionPago.getContrato()).get();

				estimacionPagoBD = estimacionPagoRepository.findById(estimacionPago.getId()).get();

				if (estimacionPago.getImporte().compareTo(estimacionPagoBD.getImporte()) == 1) {
					if (contrato.getId() != null) {

						BigDecimal totalPagoAplicado = contrato.getPagosAplicados() != null
								? contrato.getPagosAplicados()
								: new BigDecimal("0");
						BigDecimal importeEstimacion = estimacionPago.getImporte() != null ? estimacionPago.getImporte()
								: new BigDecimal("0");
						importeEstimacion = importeEstimacion.subtract(estimacionPagoBD.getImporte());
						totalPagoAplicado = totalPagoAplicado.add(importeEstimacion);

						BigDecimal montoContrato = contrato.getImporteContratado() != null
								? contrato.getImporteContratado()
								: new BigDecimal("0");

						montoContrato = montoContrato.subtract(totalPagoAplicado);

						contrato.setPagosAplicados(totalPagoAplicado);
						contrato.setSaldoPendienteContrato(montoContrato);

						contratoRepository.save(contrato);

						totalPagoAplicado = new BigDecimal("0");
					}
				} else {
					if (estimacionPago.getImporte().compareTo(estimacionPagoBD.getImporte()) == -1) {
						if (contrato.getId() != null) {
							BigDecimal totalPagoAplicado = contrato.getPagosAplicados() != null
									? contrato.getPagosAplicados()
									: new BigDecimal("0");
							BigDecimal importe = estimacionPago.getImporte() != null ? estimacionPago.getImporte()
									: new BigDecimal("0");
							importe = importe.subtract(estimacionPagoBD.getImporte());
							totalPagoAplicado = totalPagoAplicado.add(importe);

							BigDecimal montoContrato = contrato.getImporteContratado() != null
									? contrato.getImporteContratado()
									: new BigDecimal("0");

							montoContrato = montoContrato.subtract(totalPagoAplicado);

							contrato.setPagosAplicados(totalPagoAplicado);
							contrato.setSaldoPendienteContrato(montoContrato);

							contratoRepository.save(contrato);

							totalPagoAplicado = new BigDecimal("0");
						}
					}
				}

			}

			// CALCULAR PAGOS APLICADOS - ABONO A CONTRATO
			if (estimacionPago.getContrato() != null && estimacionPago.getImporte() != null
					&& (estimacionPago.getConcepto() != null && !estimacionPago.getConcepto().isEmpty()
							&& "ABONO A CONTRATO".equals(estimacionPago.getConcepto()))) {
				contrato = contratoRepository.findById(estimacionPago.getContrato()).get();

				estimacionPagoBD = estimacionPagoRepository.findById(estimacionPago.getId()).get();

				if (estimacionPago.getImporte().compareTo(estimacionPagoBD.getImporte()) == 1) {
					if (contrato.getId() != null) {

						BigDecimal totalPagoAplicado = contrato.getPagosAplicados() != null
								? contrato.getPagosAplicados()
								: new BigDecimal("0");
						BigDecimal importeEstimacion = estimacionPago.getImporte() != null ? estimacionPago.getImporte()
								: new BigDecimal("0");
						importeEstimacion = importeEstimacion.subtract(estimacionPagoBD.getImporte());
						totalPagoAplicado = totalPagoAplicado.add(importeEstimacion);

						BigDecimal montoContrato = contrato.getImporteContratado() != null
								? contrato.getImporteContratado()
								: new BigDecimal("0");

						montoContrato = montoContrato.subtract(totalPagoAplicado);

						contrato.setPagosAplicados(totalPagoAplicado);
						contrato.setSaldoPendienteContrato(montoContrato);

						contratoRepository.save(contrato);

						totalPagoAplicado = new BigDecimal("0");
					}
				} else {
					if (estimacionPago.getImporte().compareTo(estimacionPagoBD.getImporte()) == -1) {
						if (contrato.getId() != null) {
							BigDecimal totalPagoAplicado = contrato.getPagosAplicados() != null
									? contrato.getPagosAplicados()
									: new BigDecimal("0");
							BigDecimal importe = estimacionPago.getImporte() != null ? estimacionPago.getImporte()
									: new BigDecimal("0");
							importe = importe.subtract(estimacionPagoBD.getImporte());
							totalPagoAplicado = totalPagoAplicado.add(importe);

							BigDecimal montoContrato = contrato.getImporteContratado() != null
									? contrato.getImporteContratado()
									: new BigDecimal("0");

							montoContrato = montoContrato.subtract(totalPagoAplicado);

							contrato.setPagosAplicados(totalPagoAplicado);
							contrato.setSaldoPendienteContrato(montoContrato);

							contratoRepository.save(contrato);

							totalPagoAplicado = new BigDecimal("0");
						}
					}
				}
			}
			// GUARDAR
		} else {
			// CALCULAR ESTIMACIONES PROGRAMADAS
			if (estimacionPago.getContrato() != null && estimacionPago.getImporte() != null
					&& (estimacionPago.getConcepto() != null && !estimacionPago.getConcepto().isEmpty()
							&& "ESTIMACIÓN".equals(estimacionPago.getConcepto()))) {

				contrato = contratoRepository.findById(estimacionPago.getContrato()).get();

				if (contrato.getId() != null) {
					BigDecimal total = contrato.getEstimacionesProgramadas() != null
							? contrato.getEstimacionesProgramadas()
							: new BigDecimal("0");
					total = total.add(estimacionPago.getImporte());
					System.out.println("Total Estimación programada. " + total);
					contrato.setEstimacionesProgramadas(total);

					contratoRepository.save(contrato);

					total = new BigDecimal("0");
				}
			}

			// CALCULAR ESTIMACIONES PAGADAS
			if (estimacionPago.getContrato() != null && estimacionPago.getImporte() != null
					&& (estimacionPago.getConcepto() != null && !estimacionPago.getConcepto().isEmpty()
							&& "ABONO A ESTIMACIÓN".equals(estimacionPago.getConcepto()))) {
				contrato = contratoRepository.findById(estimacionPago.getContrato()).get();

				if (contrato.getId() != null) {
					BigDecimal totalEstimacion = contrato.getEstimacionesPagadas() != null
							? contrato.getEstimacionesPagadas()
							: new BigDecimal("0");
					totalEstimacion = totalEstimacion.add(estimacionPago.getImporte());
					System.out.println("Total Estimación pagada. " + totalEstimacion);

					BigDecimal totalPagoAplicado = contrato.getPagosAplicados() != null ? contrato.getPagosAplicados()
							: new BigDecimal("0");
					totalPagoAplicado = totalPagoAplicado.add(estimacionPago.getImporte());

					BigDecimal montoContrato = contrato.getImporteContratado() != null ? contrato.getImporteContratado()
							: new BigDecimal("0");

					montoContrato = montoContrato.subtract(totalPagoAplicado);

					contrato.setEstimacionesPagadas(totalEstimacion);
					contrato.setPagosAplicados(totalPagoAplicado);
					contrato.setSaldoPendienteContrato(montoContrato);

					contratoRepository.save(contrato);

					totalEstimacion = new BigDecimal("0");
					totalPagoAplicado = new BigDecimal("0");
				}

				List<EstimacionPago> estimacionPagos = estimacionPagoRepository
						.findByNumeroAbonoAndContrato(estimacionPago.getNumeroAbono(), estimacionPago.getContrato());

				for (EstimacionPago estimacionPago2 : estimacionPagos) {
					if (estimacionPago2.getConcepto().equals("ESTIMACIÓN")
							&& estimacionPago2.getNumeroAbono().equals(estimacionPago.getNumeroAbono())
							&& estimacionPago2.getContrato().equals(estimacionPago.getContrato())) {
						if (estimacionPago2.getImporteAbono() != null) {
							BigDecimal total = estimacionPago2.getImporteAbono();
							total = total.add(estimacionPago.getImporte());
							estimacionPago2.setImporteAbono(total);
							total = new BigDecimal("0");
						} else {
							estimacionPago2.setImporteAbono(estimacionPago.getImporte());
						}
						System.out.println("ESTIMACIÓN ACTUALIZADA");
						estimacionPagoRepository.save(estimacionPago2);
					}
				}
			}

			// CALCULAR PAGOS APLICADOS
			if (estimacionPago.getContrato() != null && estimacionPago.getImporte() != null
					&& (estimacionPago.getConcepto() != null && !estimacionPago.getConcepto().isEmpty()
							&& "ABONO A ANTICIPO".equals(estimacionPago.getConcepto()))) {
				contrato = contratoRepository.findById(estimacionPago.getContrato()).get();

				if (contrato.getId() != null) {
					BigDecimal totalPagoAplicado = contrato.getPagosAplicados() != null ? contrato.getPagosAplicados()
							: new BigDecimal("0");
					totalPagoAplicado = totalPagoAplicado.add(estimacionPago.getImporte());

					BigDecimal montoContrato = contrato.getImporteContratado() != null ? contrato.getImporteContratado()
							: new BigDecimal("0");

					montoContrato = montoContrato.subtract(totalPagoAplicado);

					contrato.setPagosAplicados(totalPagoAplicado);
					contrato.setSaldoPendienteContrato(montoContrato);

					contratoRepository.save(contrato);

					totalPagoAplicado = new BigDecimal("0");
				}
			}

			// CALCULAR PAGOS APLICADOS
			if (estimacionPago.getContrato() != null && estimacionPago.getImporte() != null
					&& (estimacionPago.getConcepto() != null && !estimacionPago.getConcepto().isEmpty()
							&& "ABONO A CONTRATO".equals(estimacionPago.getConcepto()))) {
				contrato = contratoRepository.findById(estimacionPago.getContrato()).get();

				if (contrato.getId() != null) {
					BigDecimal totalPagoAplicado = contrato.getPagosAplicados() != null ? contrato.getPagosAplicados()
							: new BigDecimal("0");
					totalPagoAplicado = totalPagoAplicado.add(estimacionPago.getImporte());

					BigDecimal montoContrato = contrato.getImporteContratado() != null ? contrato.getImporteContratado()
							: new BigDecimal("0");

					montoContrato = montoContrato.subtract(totalPagoAplicado);

					contrato.setPagosAplicados(totalPagoAplicado);
					contrato.setSaldoPendienteContrato(montoContrato);

					contratoRepository.save(contrato);

					totalPagoAplicado = new BigDecimal("0");
				}
			}
		}

	}

	public EstimacionPagoValidator(ContratoRepository contratoRepository,
			EstimacionPagoRepository estimacionPagoRepository) {
		this.contratoRepository = contratoRepository;
		this.estimacionPagoRepository = estimacionPagoRepository;
	}

}
