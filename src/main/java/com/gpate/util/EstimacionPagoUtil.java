package com.gpate.util;

import java.math.BigDecimal;
import java.util.List;

import com.gpate.enums.TipoConceptoEnum;
import com.gpate.model.Contrato;
import com.gpate.model.EstimacionPago;
import com.gpate.repository.ContratoRepository;
import com.gpate.repository.EstimacionPagoRepository;

public class EstimacionPagoUtil {

	public static void calcularEstimacionPago(EstimacionPago estimacionPago,
			EstimacionPagoRepository estimacionPagoRepository, ContratoRepository contratoRepository) {

		Contrato contrato = new Contrato();
		EstimacionPago estimacionPagoBD = new EstimacionPago();

		// EDITAR
		if (estimacionPago.getId() != null) {

			// CALCULAR ESTIMACIONES PROGRAMADAS
			if (estimacionPago.getContrato() != null && estimacionPago.getImporte() != null
					&& (estimacionPago.getConcepto() != null && !estimacionPago.getConcepto().isEmpty()
							&& TipoConceptoEnum.ESTIMACION.getDescripcion().equals(estimacionPago.getConcepto()))) {
				estimacionPagoBD = estimacionPagoRepository.findById(estimacionPago.getId()).get();

				if (estimacionPago.getImporte().compareTo(estimacionPagoBD.getImporte()) == 1) {
					contrato = contratoRepository.findById(estimacionPago.getContrato()).get();
					if (contrato.getId() != null) {
						BigDecimal total = contrato.getEstimacionesProgramadas() != null
								? contrato.getEstimacionesProgramadas()
								: new BigDecimal(0);
						BigDecimal estimacionImporteBD = estimacionPagoBD.getImporte() != null
								? estimacionPagoBD.getImporte()
								: new BigDecimal(0);
						BigDecimal estimacionImporte = estimacionPago.getImporte();
						estimacionImporte = estimacionImporte.subtract(estimacionImporteBD);

						total = total.add(estimacionImporte);
						System.out.println("Total Estimación programada. " + total);
						contrato.setEstimacionesProgramadas(total);

						// IMPORTE BRUTO
						BigDecimal estimacionImporteBruto = estimacionPago.getImporteBruto();
						BigDecimal estimacionImporteBrutoDB = estimacionPagoBD.getImporteBruto() != null
								? estimacionPagoBD.getImporteBruto()
								: new BigDecimal(0);
						estimacionImporteBruto = estimacionImporteBruto.subtract(estimacionImporteBrutoDB);
						BigDecimal importeBruto = contrato.getImporteBruto() != null ? contrato.getImporteBruto()
								: new BigDecimal(0);
						importeBruto = importeBruto.add(estimacionImporteBruto);
						contrato.setImporteBruto(importeBruto);

						// RETENCION VICIOS OCULTOS
						BigDecimal retencionViciosOcultos = estimacionPago.getRetencionViciosOcultos();
						BigDecimal retencionViciosOcultosBD = estimacionPagoBD.getRetencionViciosOcultos() != null
								? estimacionPagoBD.getRetencionViciosOcultos()
								: new BigDecimal(0);
						retencionViciosOcultos = retencionViciosOcultos.subtract(retencionViciosOcultosBD);
						BigDecimal retencion = contrato.getRetencionViciosOcultos() != null
								? contrato.getRetencionViciosOcultos()
								: new BigDecimal(0);
						retencion = retencion.add(retencionViciosOcultos);
						contrato.setRetencionViciosOcultos(retencion);

						// AMORTIZACIÓN ANTICIPO
						BigDecimal amortizcacionEstimacion = estimacionPago.getAmortizacionAnticipo();
						BigDecimal amortizcacionEstimacionBD = estimacionPagoBD.getAmortizacionAnticipo() != null
								? estimacionPagoBD.getAmortizacionAnticipo()
								: new BigDecimal(0);
						amortizcacionEstimacion = amortizcacionEstimacion.subtract(amortizcacionEstimacionBD);
						BigDecimal amortizacion = contrato.getAmortizacionAnticipo() != null
								? contrato.getAmortizacionAnticipo()
								: new BigDecimal(0);
						amortizacion = amortizacion.add(amortizcacionEstimacion);
						contrato.setAmortizacionAnticipo(amortizacion);

						// IVA
						BigDecimal ivaEstimacion = estimacionPago.getIva();
						BigDecimal ivaEstimacionBD = estimacionPagoBD.getIva() != null ? estimacionPagoBD.getIva()
								: new BigDecimal(0);
						ivaEstimacion = ivaEstimacion.subtract(ivaEstimacionBD);
						BigDecimal iva = contrato.getIva() != null ? contrato.getIva() : new BigDecimal(0);
						iva = iva.add(ivaEstimacion);
						contrato.setIva(iva);

						// RETENCIÓN IVA
						BigDecimal retencionIvaEstimacion = estimacionPago.getRetencionIva();
						BigDecimal retencionIvaEstimacionBD = estimacionPagoBD.getRetencionIva() != null
								? estimacionPagoBD.getRetencionIva()
								: new BigDecimal(0);
						retencionIvaEstimacion = retencionIvaEstimacion.subtract(retencionIvaEstimacionBD);
						BigDecimal retencionIva = contrato.getRetencionIva() != null ? contrato.getRetencionIva()
								: new BigDecimal(0);
						retencionIva = retencionIva.add(retencionIvaEstimacion);
						contrato.setRetencionIva(retencionIva);

						// ISR
						BigDecimal estimacionIsr = estimacionPago.getDeducciones();
						BigDecimal estimacionIsrBD = estimacionPagoBD.getIsr() != null ? estimacionPagoBD.getIsr()
								: new BigDecimal(0);
						estimacionIsr = estimacionIsr.subtract(estimacionIsrBD);
						BigDecimal isr = contrato.getIsr() != null ? contrato.getIsr() : new BigDecimal(0);
						isr = isr.add(estimacionIsr);
						contrato.setIsr(isr);

						// DEDUCCIONES
						BigDecimal estimacionDeducciones = estimacionPago.getDeducciones();
						BigDecimal estimacionDeduccionesBD = estimacionPagoBD.getDeducciones() != null
								? estimacionPagoBD.getDeducciones()
								: new BigDecimal(0);
						estimacionDeducciones = estimacionDeducciones.subtract(estimacionDeduccionesBD);
						BigDecimal deducciones = contrato.getDeducciones() != null ? contrato.getDeducciones()
								: new BigDecimal(0);
						deducciones = deducciones.add(estimacionDeducciones);
						contrato.setDeducciones(deducciones);

						if (contrato.getTieneImporte() != null) {
							if (contrato.getTieneImporte()) {
								BigDecimal montoContratado = contrato.getImporteContratado() != null
										? contrato.getImporteContratado()
										: new BigDecimal(0);
								montoContratado = montoContratado.add(estimacionImporte);
								contrato.setImporteContratado(montoContratado);
								BigDecimal pagosAplicados = contrato.getPagosAplicados() != null
										? contrato.getPagosAplicados()
										: new BigDecimal(0);
								montoContratado = montoContratado.subtract(pagosAplicados);
								contrato.setSaldoPendienteContrato(total);
							}
						}

						contratoRepository.save(contrato);

						total = new BigDecimal(0);
					}
				} else {
					if (estimacionPago.getImporte().compareTo(estimacionPagoBD.getImporte()) == -1) {
						contrato = contratoRepository.findById(estimacionPago.getContrato()).get();
						if (contrato.getId() != null) {
							BigDecimal total = contrato.getEstimacionesProgramadas() != null
									? contrato.getEstimacionesProgramadas()
									: new BigDecimal(0);
							BigDecimal estimacionImporteBD = estimacionPagoBD.getImporte() != null
									? estimacionPagoBD.getImporte()
									: new BigDecimal(0);
							BigDecimal estimacionImporte = estimacionPago.getImporte();
							estimacionImporte = estimacionImporte.subtract(estimacionImporteBD);

							total = total.add(estimacionImporte);

							System.out.println("Total Estimación programada. " + total);
							contrato.setEstimacionesProgramadas(total);

							// IMPORTE BRUTO
							BigDecimal estimacionImporteBruto = estimacionPago.getImporteBruto();
							BigDecimal estimacionImporteBrutoDB = estimacionPagoBD.getImporteBruto() != null
									? estimacionPagoBD.getImporteBruto()
									: new BigDecimal(0);
							estimacionImporteBruto = estimacionImporteBruto.subtract(estimacionImporteBrutoDB);
							BigDecimal importeBruto = contrato.getImporteBruto() != null ? contrato.getImporteBruto()
									: new BigDecimal(0);
							importeBruto = importeBruto.add(estimacionImporteBruto);
							contrato.setImporteBruto(importeBruto);

							// RETENCION VICIOS OCULTOS
							BigDecimal retencionViciosOcultos = estimacionPago.getRetencionViciosOcultos();
							BigDecimal retencionViciosOcultosBD = estimacionPagoBD.getRetencionViciosOcultos() != null
									? estimacionPagoBD.getRetencionViciosOcultos()
									: new BigDecimal(0);
							retencionViciosOcultos = retencionViciosOcultos.subtract(retencionViciosOcultosBD);
							BigDecimal retencion = contrato.getRetencionViciosOcultos() != null
									? contrato.getRetencionViciosOcultos()
									: new BigDecimal(0);
							retencion = retencion.add(retencionViciosOcultos);
							contrato.setRetencionViciosOcultos(retencion);

							// AMORTIZACIÓN ANTICIPO
							BigDecimal amortizcacionEstimacion = estimacionPago.getAmortizacionAnticipo();
							BigDecimal amortizcacionEstimacionBD = estimacionPagoBD.getAmortizacionAnticipo() != null
									? estimacionPagoBD.getAmortizacionAnticipo()
									: new BigDecimal(0);
							amortizcacionEstimacion = amortizcacionEstimacion.subtract(amortizcacionEstimacionBD);
							BigDecimal amortizacion = contrato.getAmortizacionAnticipo() != null
									? contrato.getAmortizacionAnticipo()
									: new BigDecimal(0);
							amortizacion = amortizacion.add(amortizcacionEstimacion);
							contrato.setAmortizacionAnticipo(amortizacion);

							// IVA
							BigDecimal ivaEstimacion = estimacionPago.getIva();
							BigDecimal ivaEstimacionBD = estimacionPagoBD.getIva() != null ? estimacionPagoBD.getIva()
									: new BigDecimal(0);
							ivaEstimacion = ivaEstimacion.subtract(ivaEstimacionBD);
							BigDecimal iva = contrato.getIva() != null ? contrato.getIva() : new BigDecimal(0);
							iva = iva.add(ivaEstimacion);
							contrato.setIva(iva);

							// RETENCIÓN IVA
							BigDecimal retencionIvaEstimacion = estimacionPago.getRetencionIva();
							BigDecimal retencionIvaEstimacionBD = estimacionPagoBD.getRetencionIva() != null
									? estimacionPagoBD.getRetencionIva()
									: new BigDecimal(0);
							retencionIvaEstimacion = retencionIvaEstimacion.subtract(retencionIvaEstimacionBD);
							BigDecimal retencionIva = contrato.getRetencionIva() != null ? contrato.getRetencionIva()
									: new BigDecimal(0);
							retencionIva = retencionIva.add(retencionIvaEstimacion);
							contrato.setRetencionIva(retencionIva);

							// ISR
							BigDecimal estimacionIsr = estimacionPago.getDeducciones();
							BigDecimal estimacionIsrBD = estimacionPagoBD.getIsr() != null ? estimacionPagoBD.getIsr()
									: new BigDecimal(0);
							estimacionIsr = estimacionIsr.subtract(estimacionIsrBD);
							BigDecimal isr = contrato.getIsr() != null ? contrato.getIsr() : new BigDecimal(0);
							isr = isr.add(estimacionIsr);
							contrato.setIsr(isr);

							// DEDUCCIONES
							BigDecimal estimacionDeducciones = estimacionPago.getDeducciones();
							BigDecimal estimacionDeduccionesBD = estimacionPagoBD.getDeducciones() != null
									? estimacionPagoBD.getDeducciones()
									: new BigDecimal(0);
							estimacionDeducciones = estimacionDeducciones.subtract(estimacionDeduccionesBD);
							BigDecimal deducciones = contrato.getDeducciones() != null ? contrato.getDeducciones()
									: new BigDecimal(0);
							deducciones = deducciones.add(estimacionDeducciones);
							contrato.setDeducciones(deducciones);

							if (contrato.getTieneImporte() != null) {
								if (contrato.getTieneImporte()) {
									BigDecimal montoContratado = contrato.getImporteContratado() != null
											? contrato.getImporteContratado()
											: new BigDecimal(0);
									montoContratado = montoContratado.add(estimacionImporte);
									contrato.setImporteContratado(montoContratado);
									BigDecimal pagosAplicados = contrato.getPagosAplicados() != null
											? contrato.getPagosAplicados()
											: new BigDecimal(0);
									montoContratado = montoContratado.subtract(pagosAplicados);
									contrato.setSaldoPendienteContrato(total);
								}
							}

							contratoRepository.save(contrato);

							total = new BigDecimal(0);
						}
					} else {
						if (estimacionPago.getImporte().compareTo(estimacionPagoBD.getImporte()) == 0) {
							if (!estimacionPago.getContrato().equals(estimacionPagoBD.getContrato())) {
								Contrato contrarUpdate = new Contrato();
								contrato = contratoRepository.findById(estimacionPagoBD.getContrato()).get();
								contrarUpdate = contratoRepository.findById(estimacionPago.getContrato()).get();
								if (contrato.getId() != null) {
									BigDecimal total = contrato.getEstimacionesProgramadas() != null
											? contrato.getEstimacionesProgramadas()
											: new BigDecimal(0);
									BigDecimal estimacionImporte = estimacionPago.getImporte();

									total = total.subtract(estimacionImporte);
									System.out.println("Total Estimación programada. " + total);
									contrato.setEstimacionesProgramadas(total);

									// IMPORTE BRUTO
									BigDecimal estimacionImporteBruto = estimacionPago.getImporteBruto();
									BigDecimal importeBruto = contrato.getImporteBruto() != null
											? contrato.getImporteBruto()
											: new BigDecimal(0);
									importeBruto = importeBruto.subtract(estimacionImporteBruto);
									contrato.setImporteBruto(importeBruto);

									// RETENCION VICIOS OCULTOS
									BigDecimal retencionViciosOcultos = estimacionPago.getRetencionViciosOcultos();
									BigDecimal retencion = contrato.getRetencionViciosOcultos() != null
											? contrato.getRetencionViciosOcultos()
											: new BigDecimal(0);
									retencion = retencion.subtract(retencionViciosOcultos);
									contrato.setRetencionViciosOcultos(retencion);

									// AMORTIZACIÓN ANTICIPO
									BigDecimal amortizcacionEstimacion = estimacionPago.getAmortizacionAnticipo();
									BigDecimal amortizacion = contrato.getAmortizacionAnticipo() != null
											? contrato.getAmortizacionAnticipo()
											: new BigDecimal(0);
									amortizacion = amortizacion.subtract(amortizcacionEstimacion);
									contrato.setAmortizacionAnticipo(amortizacion);

									// IVA
									BigDecimal ivaEstimacion = estimacionPago.getIva();
									BigDecimal iva = contrato.getIva() != null ? contrato.getIva() : new BigDecimal(0);
									iva = iva.subtract(ivaEstimacion);
									contrato.setIva(iva);

									// RETENCIÓN IVA
									BigDecimal retencionIvaEstimacion = estimacionPago.getRetencionIva();
									BigDecimal retencionIva = contrato.getRetencionIva() != null
											? contrato.getRetencionIva()
											: new BigDecimal(0);
									retencionIva = retencionIva.subtract(retencionIvaEstimacion);
									contrato.setRetencionIva(retencionIva);

									// ISR
									BigDecimal estimacionIsr = estimacionPago.getDeducciones();
									BigDecimal isr = contrato.getIsr() != null ? contrato.getIsr() : new BigDecimal(0);
									isr = isr.subtract(estimacionIsr);
									contrato.setIsr(isr);

									// DEDUCCIONES
									BigDecimal estimacionDeducciones = estimacionPago.getDeducciones();
									BigDecimal deducciones = contrato.getDeducciones() != null
											? contrato.getDeducciones()
											: new BigDecimal(0);
									deducciones = deducciones.subtract(estimacionDeducciones);
									contrato.setDeducciones(deducciones);

									if (contrato.getTieneImporte() != null) {
										if (contrato.getTieneImporte()) {
											BigDecimal totalPagoAplicado = contrato.getPagosAplicados() != null
													? contrato.getPagosAplicados()
													: new BigDecimal(0);
											totalPagoAplicado = totalPagoAplicado.add(total);
											contrato.setImporteContratado(totalPagoAplicado);
										}
									}

									contratoRepository.save(contrato);

									total = new BigDecimal(0);

									List<EstimacionPago> estimacionPagos = estimacionPagoRepository
											.findByNumeroAbonoAndContrato(estimacionPagoBD.getNumeroAbono(),
													estimacionPagoBD.getContrato());

									for (EstimacionPago estimacionPago2 : estimacionPagos) {
										if (estimacionPago2.getConcepto().equals("ESTIMACIÓN")
												&& estimacionPago2.getNumeroAbono()
														.equals(estimacionPagoBD.getNumeroAbono())
												&& estimacionPago2.getContrato()
														.equals(estimacionPagoBD.getContrato())) {
											estimacionPago2.setImporteAbono(null);
											System.out.println("ESTIMACIÓN ACTUALIZADA");
											estimacionPagoRepository.save(estimacionPago2);
										}
									}

								}
								if (contrarUpdate.getId() != null) {
									BigDecimal total = contrarUpdate.getEstimacionesProgramadas() != null
											? contrarUpdate.getEstimacionesProgramadas()
											: new BigDecimal(0);
									BigDecimal estimacionImporte = estimacionPago.getImporte();

									total = total.add(estimacionImporte);
									System.out.println("Total Estimación programada. " + total);
									contrarUpdate.setEstimacionesProgramadas(total);

									if (contrarUpdate.getTieneImporte() != null) {
										if (contrarUpdate.getTieneImporte()) {
											BigDecimal totalPagoAplicado = contrarUpdate.getPagosAplicados() != null
													? contrarUpdate.getPagosAplicados()
													: new BigDecimal(0);
											totalPagoAplicado = totalPagoAplicado.add(total);
											contrarUpdate.setImporteContratado(totalPagoAplicado);
										}
									}

									// IMPORTE BRUTO
									BigDecimal estimacionImporteBruto = estimacionPago.getImporteBruto();
									BigDecimal importeBruto = contrarUpdate.getImporteBruto() != null
											? contrarUpdate.getImporteBruto()
											: new BigDecimal(0);
									importeBruto = importeBruto.add(estimacionImporteBruto);
									contrarUpdate.setImporteBruto(importeBruto);

									// RETENCION VICIOS OCULTOS
									BigDecimal retencionViciosOcultos = estimacionPago.getRetencionViciosOcultos();
									BigDecimal retencion = contrarUpdate.getRetencionViciosOcultos() != null
											? contrarUpdate.getRetencionViciosOcultos()
											: new BigDecimal(0);
									retencion = retencion.add(retencionViciosOcultos);
									contrarUpdate.setRetencionViciosOcultos(retencion);

									// AMORTIZACIÓN ANTICIPO
									BigDecimal amortizcacionEstimacion = estimacionPago.getAmortizacionAnticipo();
									BigDecimal amortizacion = contrarUpdate.getAmortizacionAnticipo() != null
											? contrarUpdate.getAmortizacionAnticipo()
											: new BigDecimal(0);
									amortizacion = amortizacion.add(amortizcacionEstimacion);
									contrarUpdate.setAmortizacionAnticipo(amortizacion);

									// IVA
									BigDecimal ivaEstimacion = estimacionPago.getIva();
									BigDecimal iva = contrarUpdate.getIva() != null ? contrarUpdate.getIva()
											: new BigDecimal(0);
									iva = iva.add(ivaEstimacion);
									contrarUpdate.setIva(iva);

									// RETENCIÓN IVA
									BigDecimal retencionIvaEstimacion = estimacionPago.getRetencionIva();
									BigDecimal retencionIva = contrarUpdate.getRetencionIva() != null
											? contrarUpdate.getRetencionIva()
											: new BigDecimal(0);
									retencionIva = retencionIva.add(retencionIvaEstimacion);
									contrarUpdate.setRetencionIva(retencionIva);

									// ISR
									BigDecimal estimacionIsr = estimacionPago.getDeducciones();
									BigDecimal isr = contrarUpdate.getIsr() != null ? contrarUpdate.getIsr()
											: new BigDecimal(0);
									isr = isr.add(estimacionIsr);
									contrarUpdate.setIsr(isr);

									// DEDUCCIONES
									BigDecimal estimacionDeducciones = estimacionPago.getDeducciones();
									BigDecimal deducciones = contrarUpdate.getDeducciones() != null
											? contrarUpdate.getDeducciones()
											: new BigDecimal(0);
									deducciones = deducciones.add(estimacionDeducciones);
									contrarUpdate.setDeducciones(deducciones);

									contratoRepository.save(contrarUpdate);

									List<EstimacionPago> estimacionPagos = estimacionPagoRepository
											.findByNumeroAbonoAndContrato(estimacionPago.getNumeroAbono(),
													estimacionPago.getContrato());

									for (EstimacionPago estimacionPago2 : estimacionPagos) {
										if (estimacionPago2.getConcepto().equals("ESTIMACIÓN")
												&& estimacionPago2.getNumeroAbono()
														.equals(estimacionPago.getNumeroAbono())
												&& estimacionPago2.getContrato().equals(estimacionPago.getContrato())) {
											estimacionPago2.setImporteAbono(null);
											System.out.println("ESTIMACIÓN ACTUALIZADA");
											estimacionPagoRepository.save(estimacionPago2);
										}
									}

									total = new BigDecimal(0);
								}
							} else {
								contrato = contratoRepository.findById(estimacionPago.getContrato()).get();
								if (contrato.getId() != null) {
									BigDecimal total = contrato.getEstimacionesProgramadas() != null
											? contrato.getEstimacionesProgramadas()
											: new BigDecimal(0);
									BigDecimal estimacionImporteBD = estimacionPagoBD.getImporte() != null
											? estimacionPagoBD.getImporte()
											: new BigDecimal(0);
									BigDecimal estimacionImporte = estimacionPago.getImporte();
									estimacionImporte = estimacionImporte.subtract(estimacionImporteBD);

									total = total.add(estimacionImporte);
									System.out.println("Total Estimación programada. " + total);
									contrato.setEstimacionesProgramadas(total);

									if (contrato.getTieneImporte() != null) {
										if (contrato.getTieneImporte()) {
											contrato.setImporteContratado(total);
										}
									}
									contratoRepository.save(contrato);

									total = new BigDecimal(0);
								}
							}
						}
					}
				}
			}

			// CALCULAR ESTIMACIONES PAGADAS
			if (estimacionPago.getContrato() != null && estimacionPago.getImporte() != null
					&& (estimacionPago.getConcepto() != null && !estimacionPago.getConcepto().isEmpty()
							&& TipoConceptoEnum.ABONO_ESTIMACION.getDescripcion()
									.equals(estimacionPago.getConcepto()))) {
				estimacionPagoBD = estimacionPagoRepository.findById(estimacionPago.getId()).get();

				if (estimacionPago.getImporte().compareTo(estimacionPagoBD.getImporte()) == 1) {
					contrato = contratoRepository.findById(estimacionPago.getContrato()).get();
					if (contrato.getId() != null) {
						BigDecimal estimacionPagada = contrato.getEstimacionesPagadas() != null
								? contrato.getEstimacionesPagadas()
								: new BigDecimal(0);
						BigDecimal importe = estimacionPago.getImporte() != null ? estimacionPago.getImporte()
								: new BigDecimal(0);
						importe = importe.subtract(estimacionPagoBD.getImporte());

						estimacionPagada = estimacionPagada.add(importe);
						System.out.println("Total Estimación pagada. " + estimacionPagada);

						BigDecimal importePago = estimacionPago.getImporte() != null ? estimacionPago.getImporte()
								: new BigDecimal(0);
						BigDecimal estimacionImporteBD = estimacionPagoBD.getImporte() != null
								? estimacionPagoBD.getImporte()
								: new BigDecimal(0);

						BigDecimal totalPagoAplicado = contrato.getPagosAplicados() != null
								? contrato.getPagosAplicados()
								: new BigDecimal(0);
						importePago = importePago.subtract(estimacionImporteBD);

						totalPagoAplicado = totalPagoAplicado.add(importePago);
						BigDecimal montoContrato = contrato.getImporteContratado() != null
								? contrato.getImporteContratado()
								: new BigDecimal(0);

						montoContrato = montoContrato.subtract(totalPagoAplicado);

						contrato.setEstimacionesPagadas(estimacionPagada);
						contrato.setPagosAplicados(totalPagoAplicado);
						contrato.setSaldoPendienteContrato(montoContrato);

						// IMPORTE BRUTO
						BigDecimal estimacionImporteBruto = estimacionPago.getImporteBruto();
						BigDecimal importeBruto = contrato.getImporteBruto() != null ? contrato.getImporteBruto()
								: new BigDecimal(0);
						importeBruto = importeBruto.subtract(estimacionImporteBruto);
						contrato.setImporteBruto(importeBruto);

						// RETENCION VICIOS OCULTOS
						BigDecimal retencionViciosOcultos = estimacionPago.getRetencionViciosOcultos();
						BigDecimal retencion = contrato.getRetencionViciosOcultos() != null
								? contrato.getRetencionViciosOcultos()
								: new BigDecimal(0);
						retencion = retencion.subtract(retencionViciosOcultos);
						contrato.setRetencionViciosOcultos(retencion);

						// AMORTIZACIÓN ANTICIPO
						BigDecimal amortizcacionEstimacion = estimacionPago.getAmortizacionAnticipo();
						BigDecimal amortizacion = contrato.getAmortizacionAnticipo() != null
								? contrato.getAmortizacionAnticipo()
								: new BigDecimal(0);
						amortizacion = amortizacion.subtract(amortizcacionEstimacion);
						contrato.setAmortizacionAnticipo(amortizacion);

						// IVA
						BigDecimal ivaEstimacion = estimacionPago.getIva();
						BigDecimal iva = contrato.getIva() != null ? contrato.getIva() : new BigDecimal(0);
						iva = iva.subtract(ivaEstimacion);
						contrato.setIva(iva);

						// RETENCIÓN IVA
						BigDecimal retencionIvaEstimacion = estimacionPago.getRetencionIva();
						BigDecimal retencionIva = contrato.getRetencionIva() != null ? contrato.getRetencionIva()
								: new BigDecimal(0);
						retencionIva = retencionIva.subtract(retencionIvaEstimacion);
						contrato.setRetencionIva(retencionIva);

						// ISR
						BigDecimal estimacionIsr = estimacionPago.getDeducciones();
						BigDecimal isr = contrato.getIsr() != null ? contrato.getIsr() : new BigDecimal(0);
						isr = isr.subtract(estimacionIsr);
						contrato.setIsr(isr);

						// DEDUCCIONES
						BigDecimal estimacionDeducciones = estimacionPago.getDeducciones();
						BigDecimal deducciones = contrato.getDeducciones() != null ? contrato.getDeducciones()
								: new BigDecimal(0);
						deducciones = deducciones.subtract(estimacionDeducciones);
						contrato.setDeducciones(deducciones);

						contratoRepository.save(contrato);

						List<EstimacionPago> estimacionPagos = estimacionPagoRepository.findByNumeroAbonoAndContrato(
								estimacionPago.getNumeroAbono(), estimacionPago.getContrato());

						for (EstimacionPago estimacionPago2 : estimacionPagos) {
							if (estimacionPago2.getConcepto().equals("ESTIMACIÓN")
									&& estimacionPago2.getNumeroAbono().equals(estimacionPago.getNumeroAbono())
									&& estimacionPago2.getContrato().equals(estimacionPago.getContrato())) {
								if (estimacionPago2.getImporteAbono() != null) {
									BigDecimal total = estimacionPago2.getImporteAbono();
									total = total.add(importe);
									estimacionPago2.setImporteAbono(total);
									total = new BigDecimal(0);
								} else {
									estimacionPago2.setImporteAbono(estimacionPago.getImporte());
								}
								System.out.println("ESTIMACIÓN ACTUALIZADA");
								estimacionPagoRepository.save(estimacionPago2);
							}
						}
					}

				} else {
					if (estimacionPago.getImporte().compareTo(estimacionPagoBD.getImporte()) == -1) {
						contrato = contratoRepository.findById(estimacionPago.getContrato()).get();
						if (contrato.getId() != null) {
							BigDecimal estimacionPagada = contrato.getEstimacionesPagadas() != null
									? contrato.getEstimacionesPagadas()
									: new BigDecimal(0);
							BigDecimal importe = estimacionPago.getImporte() != null ? estimacionPago.getImporte()
									: new BigDecimal(0);
							importe = importe.subtract(estimacionPagoBD.getImporte());

							estimacionPagada = estimacionPagada.add(importe);
							System.out.println("Total Estimación pagada. " + estimacionPagada);

							BigDecimal importePago = estimacionPago.getImporte() != null ? estimacionPago.getImporte()
									: new BigDecimal(0);
							BigDecimal estimacionImporteBD = estimacionPagoBD.getImporte() != null
									? estimacionPagoBD.getImporte()
									: new BigDecimal(0);

							BigDecimal totalPagoAplicado = contrato.getPagosAplicados() != null
									? contrato.getPagosAplicados()
									: new BigDecimal(0);
							importePago = importePago.subtract(estimacionImporteBD);

							totalPagoAplicado = totalPagoAplicado.add(importePago);
							BigDecimal montoContrato = contrato.getImporteContratado() != null
									? contrato.getImporteContratado()
									: new BigDecimal(0);

							montoContrato = montoContrato.subtract(totalPagoAplicado);

							contrato.setEstimacionesPagadas(estimacionPagada);
							contrato.setPagosAplicados(totalPagoAplicado);
							contrato.setSaldoPendienteContrato(montoContrato);

							// IMPORTE BRUTO
							BigDecimal estimacionImporteBruto = estimacionPago.getImporteBruto();
							BigDecimal importeBruto = contrato.getImporteBruto() != null ? contrato.getImporteBruto()
									: new BigDecimal(0);
							importeBruto = importeBruto.subtract(estimacionImporteBruto);
							contrato.setImporteBruto(importeBruto);

							// RETENCION VICIOS OCULTOS
							BigDecimal retencionViciosOcultos = estimacionPago.getRetencionViciosOcultos();
							BigDecimal retencion = contrato.getRetencionViciosOcultos() != null
									? contrato.getRetencionViciosOcultos()
									: new BigDecimal(0);
							retencion = retencion.subtract(retencionViciosOcultos);
							contrato.setRetencionViciosOcultos(retencion);

							// AMORTIZACIÓN ANTICIPO
							BigDecimal amortizcacionEstimacion = estimacionPago.getAmortizacionAnticipo();
							BigDecimal amortizacion = contrato.getAmortizacionAnticipo() != null
									? contrato.getAmortizacionAnticipo()
									: new BigDecimal(0);
							amortizacion = amortizacion.subtract(amortizcacionEstimacion);
							contrato.setAmortizacionAnticipo(amortizacion);

							// IVA
							BigDecimal ivaEstimacion = estimacionPago.getIva();
							BigDecimal iva = contrato.getIva() != null ? contrato.getIva() : new BigDecimal(0);
							iva = iva.subtract(ivaEstimacion);
							contrato.setIva(iva);

							// RETENCIÓN IVA
							BigDecimal retencionIvaEstimacion = estimacionPago.getRetencionIva();
							BigDecimal retencionIva = contrato.getRetencionIva() != null ? contrato.getRetencionIva()
									: new BigDecimal(0);
							retencionIva = retencionIva.subtract(retencionIvaEstimacion);
							contrato.setRetencionIva(retencionIva);

							// ISR
							BigDecimal estimacionIsr = estimacionPago.getDeducciones();
							BigDecimal isr = contrato.getIsr() != null ? contrato.getIsr() : new BigDecimal(0);
							isr = isr.subtract(estimacionIsr);
							contrato.setIsr(isr);

							// DEDUCCIONES
							BigDecimal estimacionDeducciones = estimacionPago.getDeducciones();
							BigDecimal deducciones = contrato.getDeducciones() != null ? contrato.getDeducciones()
									: new BigDecimal(0);
							deducciones = deducciones.subtract(estimacionDeducciones);
							contrato.setDeducciones(deducciones);

							contratoRepository.save(contrato);

							List<EstimacionPago> estimacionPagos = estimacionPagoRepository
									.findByNumeroAbonoAndContrato(estimacionPago.getNumeroAbono(),
											estimacionPago.getContrato());

							for (EstimacionPago estimacionPago2 : estimacionPagos) {
								if (estimacionPago2.getConcepto().equals("ESTIMACIÓN")
										&& estimacionPago2.getNumeroAbono().equals(estimacionPago.getNumeroAbono())
										&& estimacionPago2.getContrato().equals(estimacionPago.getContrato())) {
									if (estimacionPago2.getImporteAbono() != null) {
										BigDecimal total = estimacionPago2.getImporteAbono();
										total = total.add(importe);
										estimacionPago2.setImporteAbono(total);
										total = new BigDecimal(0);
									} else {
										estimacionPago2.setImporteAbono(estimacionPago.getImporte());
									}
									System.out.println("ESTIMACIÓN ACTUALIZADA");
									estimacionPagoRepository.save(estimacionPago2);
								}
							}
						}
					} else {
						if (estimacionPago.getImporte().compareTo(estimacionPagoBD.getImporte()) == 0) {
							if (!estimacionPago.getContrato().equals(estimacionPagoBD.getContrato())) {
								Contrato contrarUpdate = new Contrato();
								contrato = contratoRepository.findById(estimacionPagoBD.getContrato()).get();
								contrarUpdate = contratoRepository.findById(estimacionPago.getContrato()).get();
								if (contrato.getId() != null) {
									BigDecimal estimacionPagada = contrato.getEstimacionesPagadas() != null
											? contrato.getEstimacionesPagadas()
											: new BigDecimal(0);
									BigDecimal importe = estimacionPago.getImporte() != null
											? estimacionPago.getImporte()
											: new BigDecimal(0);
									BigDecimal totalPagoAplicado = contrato.getPagosAplicados() != null
											? contrato.getPagosAplicados()
											: new BigDecimal(0);
									BigDecimal montoContrato = contrato.getImporteContratado() != null
											? contrato.getImporteContratado()
											: new BigDecimal(0);

									estimacionPagada = estimacionPagada.subtract(importe);
									totalPagoAplicado = totalPagoAplicado.subtract(importe);
									System.out.println("Total Estimación pagada. " + estimacionPagada);

									montoContrato = montoContrato.subtract(totalPagoAplicado);

									contrato.setEstimacionesPagadas(estimacionPagada);
									contrato.setPagosAplicados(totalPagoAplicado);
									contrato.setSaldoPendienteContrato(montoContrato);

									// IMPORTE BRUTO
									BigDecimal estimacionImporteBruto = estimacionPago.getImporteBruto();
									BigDecimal importeBruto = contrato.getImporteBruto() != null
											? contrato.getImporteBruto()
											: new BigDecimal(0);
									importeBruto = importeBruto.subtract(estimacionImporteBruto);
									contrato.setImporteBruto(importeBruto);

									// RETENCION VICIOS OCULTOS
									BigDecimal retencionViciosOcultos = estimacionPago.getRetencionViciosOcultos();
									BigDecimal retencion = contrato.getRetencionViciosOcultos() != null
											? contrato.getRetencionViciosOcultos()
											: new BigDecimal(0);
									retencion = retencion.subtract(retencionViciosOcultos);
									contrato.setRetencionViciosOcultos(retencion);

									// AMORTIZACIÓN ANTICIPO
									BigDecimal amortizcacionEstimacion = estimacionPago.getAmortizacionAnticipo();
									BigDecimal amortizacion = contrato.getAmortizacionAnticipo() != null
											? contrato.getAmortizacionAnticipo()
											: new BigDecimal(0);
									amortizacion = amortizacion.subtract(amortizcacionEstimacion);
									contrato.setAmortizacionAnticipo(amortizacion);

									// IVA
									BigDecimal ivaEstimacion = estimacionPago.getIva();
									BigDecimal iva = contrato.getIva() != null ? contrato.getIva() : new BigDecimal(0);
									iva = iva.subtract(ivaEstimacion);
									contrato.setIva(iva);

									// RETENCIÓN IVA
									BigDecimal retencionIvaEstimacion = estimacionPago.getRetencionIva();
									BigDecimal retencionIva = contrato.getRetencionIva() != null
											? contrato.getRetencionIva()
											: new BigDecimal(0);
									retencionIva = retencionIva.subtract(retencionIvaEstimacion);
									contrato.setRetencionIva(retencionIva);

									// ISR
									BigDecimal estimacionIsr = estimacionPago.getDeducciones();
									BigDecimal isr = contrato.getIsr() != null ? contrato.getIsr() : new BigDecimal(0);
									isr = isr.subtract(estimacionIsr);
									contrato.setIsr(isr);

									// DEDUCCIONES
									BigDecimal estimacionDeducciones = estimacionPago.getDeducciones();
									BigDecimal deducciones = contrato.getDeducciones() != null
											? contrato.getDeducciones()
											: new BigDecimal(0);
									deducciones = deducciones.subtract(estimacionDeducciones);
									contrato.setDeducciones(deducciones);

									contratoRepository.save(contrato);

									List<EstimacionPago> estimacionPagos = estimacionPagoRepository
											.findByNumeroAbonoAndContrato(estimacionPagoBD.getNumeroAbono(),
													estimacionPagoBD.getContrato());

									for (EstimacionPago estimacionPago2 : estimacionPagos) {
										if (estimacionPago2.getConcepto().equals("ESTIMACIÓN")
												&& estimacionPago2.getNumeroAbono()
														.equals(estimacionPagoBD.getNumeroAbono())
												&& estimacionPago2.getContrato()
														.equals(estimacionPagoBD.getContrato())) {
											if (estimacionPago2.getImporteAbono() != null) {
												BigDecimal total = estimacionPago2.getImporteAbono();
												total = total.subtract(importe);
												estimacionPago2.setImporteAbono(total);
												total = new BigDecimal(0);
											} else {
												estimacionPago2.setImporteAbono(estimacionPago.getImporte());
											}
											System.out.println("ESTIMACIÓN ACTUALIZADA");
											estimacionPagoRepository.save(estimacionPago2);
										}
									}
								}
								if (contrarUpdate.getId() != null) {
									BigDecimal estimacionPagada = contrarUpdate.getEstimacionesPagadas() != null
											? contrarUpdate.getEstimacionesPagadas()
											: new BigDecimal(0);
									BigDecimal importe = estimacionPago.getImporte() != null
											? estimacionPago.getImporte()
											: new BigDecimal(0);
									BigDecimal totalPagoAplicado = contrarUpdate.getPagosAplicados() != null
											? contrarUpdate.getPagosAplicados()
											: new BigDecimal(0);
									BigDecimal montoContrato = contrarUpdate.getImporteContratado() != null
											? contrarUpdate.getImporteContratado()
											: new BigDecimal(0);

									estimacionPagada = estimacionPagada.add(importe);
									totalPagoAplicado = totalPagoAplicado.add(importe);
									System.out.println("Total Estimación pagada. " + estimacionPagada);

									montoContrato = montoContrato.subtract(totalPagoAplicado);

									contrarUpdate.setEstimacionesPagadas(estimacionPagada);
									contrarUpdate.setPagosAplicados(totalPagoAplicado);
									contrarUpdate.setSaldoPendienteContrato(montoContrato);

									contratoRepository.save(contrarUpdate);

									List<EstimacionPago> estimacionPagos = estimacionPagoRepository
											.findByNumeroAbonoAndContrato(estimacionPago.getNumeroAbono(),
													estimacionPago.getContrato());

									for (EstimacionPago estimacionPago2 : estimacionPagos) {
										if (estimacionPago2.getConcepto().equals("ESTIMACIÓN")
												&& estimacionPago2.getNumeroAbono()
														.equals(estimacionPago.getNumeroAbono())
												&& estimacionPago2.getContrato().equals(estimacionPago.getContrato())) {
											if (estimacionPago2.getImporteAbono() != null) {
												BigDecimal total = estimacionPago2.getImporteAbono();
												total = total.add(importe);
												estimacionPago2.setImporteAbono(total);
												total = new BigDecimal(0);
											} else {
												estimacionPago2.setImporteAbono(estimacionPago.getImporte());
											}
											System.out.println("ESTIMACIÓN ACTUALIZADA");
											estimacionPagoRepository.save(estimacionPago2);
										}
									}
								}
							} else {
								if (!estimacionPagoBD.getConcepto().equals(estimacionPago.getConcepto())) {
									contrato = contratoRepository.findById(estimacionPago.getContrato()).get();
									BigDecimal estimacionPagada = contrato.getEstimacionesPagadas() != null
											? contrato.getEstimacionesPagadas()
											: new BigDecimal(0);
									BigDecimal importe = estimacionPago.getImporte() != null
											? estimacionPago.getImporte()
											: new BigDecimal(0);
									BigDecimal totalPagoAplicado = contrato.getPagosAplicados() != null
											? contrato.getPagosAplicados()
											: new BigDecimal(0);
									BigDecimal montoContrato = contrato.getImporteContratado() != null
											? contrato.getImporteContratado()
											: new BigDecimal(0);

									estimacionPagada = estimacionPagada.add(importe);
									totalPagoAplicado = totalPagoAplicado.add(importe);
									System.out.println("Total Estimación pagada. " + estimacionPagada);

									montoContrato = montoContrato.subtract(totalPagoAplicado);

									contrato.setEstimacionesPagadas(estimacionPagada);
									contrato.setPagosAplicados(totalPagoAplicado);
									contrato.setSaldoPendienteContrato(montoContrato);

									contratoRepository.save(contrato);

									List<EstimacionPago> estimacionPagos = estimacionPagoRepository
											.findByNumeroAbonoAndContrato(estimacionPago.getNumeroAbono(),
													estimacionPago.getContrato());

									for (EstimacionPago estimacionPago2 : estimacionPagos) {
										if (estimacionPago2.getConcepto().equals("ESTIMACIÓN")
												&& estimacionPago2.getNumeroAbono()
														.equals(estimacionPago.getNumeroAbono())
												&& estimacionPago2.getContrato().equals(estimacionPago.getContrato())) {
											if (estimacionPago2.getImporteAbono() != null) {
												BigDecimal total = estimacionPago2.getImporteAbono();
												total = total.add(importe);
												estimacionPago2.setImporteAbono(total);
												total = new BigDecimal(0);
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
					}
				}
			}
			// CALCULAR PAGOS APLICADOS - ANTICIPO
			if (estimacionPago.getContrato() != null && estimacionPago.getImporte() != null
					&& (estimacionPago.getConcepto() != null && !estimacionPago.getConcepto().isEmpty()
							&& TipoConceptoEnum.ABONO_ANTICIPO.getDescripcion().equals(estimacionPago.getConcepto()))) {
				estimacionPagoBD = estimacionPagoRepository.findById(estimacionPago.getId()).get();

				if (estimacionPago.getImporte().compareTo(estimacionPagoBD.getImporte()) == 1) {
					contrato = contratoRepository.findById(estimacionPago.getContrato()).get();
					if (contrato.getId() != null) {

						BigDecimal totalPagoAplicado = contrato.getPagosAplicados() != null
								? contrato.getPagosAplicados()
								: new BigDecimal(0);
						BigDecimal importeEstimacion = estimacionPago.getImporte() != null ? estimacionPago.getImporte()
								: new BigDecimal(0);
						BigDecimal anticipoPagado = contrato.getAnticipoPagado() != null ? contrato.getAnticipoPagado()
								: new BigDecimal(0);
						importeEstimacion = importeEstimacion.subtract(estimacionPagoBD.getImporte());
						totalPagoAplicado = totalPagoAplicado.add(importeEstimacion);
						anticipoPagado = anticipoPagado.add(importeEstimacion);

						BigDecimal montoContrato = contrato.getImporteContratado() != null
								? contrato.getImporteContratado()
								: new BigDecimal(0);

						if (contrato.getTieneImporte() != null) {
							if (contrato.getTieneImporte()) {
								montoContrato = montoContrato.add(importeEstimacion);
								contrato.setImporteContratado(montoContrato);
							}
						}

						montoContrato = contrato.getImporteContratado() != null ? contrato.getImporteContratado()
								: new BigDecimal(0);

						montoContrato = montoContrato.subtract(totalPagoAplicado);

						contrato.setPagosAplicados(totalPagoAplicado);
						contrato.setSaldoPendienteContrato(montoContrato);
						contrato.setAnticipoPagado(anticipoPagado);

						// IMPORTE BRUTO
						BigDecimal estimacionImporteBruto = estimacionPago.getImporteBruto();
						BigDecimal importeBruto = contrato.getImporteBruto() != null ? contrato.getImporteBruto()
								: new BigDecimal(0);
						importeBruto = importeBruto.subtract(estimacionImporteBruto);
						contrato.setImporteBruto(importeBruto);

						// RETENCION VICIOS OCULTOS
						BigDecimal retencionViciosOcultos = estimacionPago.getRetencionViciosOcultos();
						BigDecimal retencion = contrato.getRetencionViciosOcultos() != null
								? contrato.getRetencionViciosOcultos()
								: new BigDecimal(0);
						retencion = retencion.subtract(retencionViciosOcultos);
						contrato.setRetencionViciosOcultos(retencion);

						// AMORTIZACIÓN ANTICIPO
						BigDecimal amortizcacionEstimacion = estimacionPago.getAmortizacionAnticipo();
						BigDecimal amortizacion = contrato.getAmortizacionAnticipo() != null
								? contrato.getAmortizacionAnticipo()
								: new BigDecimal(0);
						amortizacion = amortizacion.subtract(amortizcacionEstimacion);
						contrato.setAmortizacionAnticipo(amortizacion);

						// IVA
						BigDecimal ivaEstimacion = estimacionPago.getIva();
						BigDecimal iva = contrato.getIva() != null ? contrato.getIva() : new BigDecimal(0);
						iva = iva.subtract(ivaEstimacion);
						contrato.setIva(iva);

						// RETENCIÓN IVA
						BigDecimal retencionIvaEstimacion = estimacionPago.getRetencionIva();
						BigDecimal retencionIva = contrato.getRetencionIva() != null ? contrato.getRetencionIva()
								: new BigDecimal(0);
						retencionIva = retencionIva.subtract(retencionIvaEstimacion);
						contrato.setRetencionIva(retencionIva);

						// ISR
						BigDecimal estimacionIsr = estimacionPago.getDeducciones();
						BigDecimal isr = contrato.getIsr() != null ? contrato.getIsr() : new BigDecimal(0);
						isr = isr.subtract(estimacionIsr);
						contrato.setIsr(isr);

						// DEDUCCIONES
						BigDecimal estimacionDeducciones = estimacionPago.getDeducciones();
						BigDecimal deducciones = contrato.getDeducciones() != null ? contrato.getDeducciones()
								: new BigDecimal(0);
						deducciones = deducciones.subtract(estimacionDeducciones);
						contrato.setDeducciones(deducciones);

						if (TipoConceptoEnum.ABONO_ESTIMACION.getDescripcion().equals(estimacionPagoBD.getConcepto())) {
							BigDecimal estimacionPagada = contrato.getEstimacionesPagadas() != null
									? contrato.getEstimacionesPagadas()
									: new BigDecimal(0);
							estimacionPagada.subtract(estimacionPagoBD.getImporte());
							contrato.setEstimacionesPagadas(estimacionPagada);
						}

						contratoRepository.save(contrato);
					}
				} else {
					if (estimacionPago.getImporte().compareTo(estimacionPagoBD.getImporte()) == -1) {
						contrato = contratoRepository.findById(estimacionPago.getContrato()).get();
						if (contrato.getId() != null) {
							BigDecimal totalPagoAplicado = contrato.getPagosAplicados() != null
									? contrato.getPagosAplicados()
									: new BigDecimal(0);
							BigDecimal importe = estimacionPago.getImporte() != null ? estimacionPago.getImporte()
									: new BigDecimal(0);
							BigDecimal anticipoPagado = contrato.getAnticipoPagado() != null
									? contrato.getAnticipoPagado()
									: new BigDecimal(0);
							importe = importe.subtract(estimacionPagoBD.getImporte());
							totalPagoAplicado = totalPagoAplicado.add(importe);
							anticipoPagado = anticipoPagado.add(importe);

							BigDecimal montoContrato = contrato.getImporteContratado() != null
									? contrato.getImporteContratado()
									: new BigDecimal(0);

							if (contrato.getTieneImporte() != null) {
								if (contrato.getTieneImporte()) {
									montoContrato = montoContrato.add(importe);
									contrato.setImporteContratado(montoContrato);
								}
							}

							montoContrato = contrato.getImporteContratado() != null ? contrato.getImporteContratado()
									: new BigDecimal(0);

							montoContrato = montoContrato.subtract(totalPagoAplicado);

							contrato.setPagosAplicados(totalPagoAplicado);
							contrato.setSaldoPendienteContrato(montoContrato);
							contrato.setAnticipoPagado(anticipoPagado);

							// IMPORTE BRUTO
							BigDecimal estimacionImporteBruto = estimacionPago.getImporteBruto();
							BigDecimal importeBruto = contrato.getImporteBruto() != null ? contrato.getImporteBruto()
									: new BigDecimal(0);
							importeBruto = importeBruto.subtract(estimacionImporteBruto);
							contrato.setImporteBruto(importeBruto);

							// RETENCION VICIOS OCULTOS
							BigDecimal retencionViciosOcultos = estimacionPago.getRetencionViciosOcultos();
							BigDecimal retencion = contrato.getRetencionViciosOcultos() != null
									? contrato.getRetencionViciosOcultos()
									: new BigDecimal(0);
							retencion = retencion.subtract(retencionViciosOcultos);
							contrato.setRetencionViciosOcultos(retencion);

							// AMORTIZACIÓN ANTICIPO
							BigDecimal amortizcacionEstimacion = estimacionPago.getAmortizacionAnticipo();
							BigDecimal amortizacion = contrato.getAmortizacionAnticipo() != null
									? contrato.getAmortizacionAnticipo()
									: new BigDecimal(0);
							amortizacion = amortizacion.subtract(amortizcacionEstimacion);
							contrato.setAmortizacionAnticipo(amortizacion);

							// IVA
							BigDecimal ivaEstimacion = estimacionPago.getIva();
							BigDecimal iva = contrato.getIva() != null ? contrato.getIva() : new BigDecimal(0);
							iva = iva.subtract(ivaEstimacion);
							contrato.setIva(iva);

							// RETENCIÓN IVA
							BigDecimal retencionIvaEstimacion = estimacionPago.getRetencionIva();
							BigDecimal retencionIva = contrato.getRetencionIva() != null ? contrato.getRetencionIva()
									: new BigDecimal(0);
							retencionIva = retencionIva.subtract(retencionIvaEstimacion);
							contrato.setRetencionIva(retencionIva);

							// ISR
							BigDecimal estimacionIsr = estimacionPago.getDeducciones();
							BigDecimal isr = contrato.getIsr() != null ? contrato.getIsr() : new BigDecimal(0);
							isr = isr.subtract(estimacionIsr);
							contrato.setIsr(isr);

							// DEDUCCIONES
							BigDecimal estimacionDeducciones = estimacionPago.getDeducciones();
							BigDecimal deducciones = contrato.getDeducciones() != null ? contrato.getDeducciones()
									: new BigDecimal(0);
							deducciones = deducciones.subtract(estimacionDeducciones);
							contrato.setDeducciones(deducciones);

							if (TipoConceptoEnum.ABONO_ESTIMACION.getDescripcion()
									.equals(estimacionPagoBD.getConcepto())) {
								BigDecimal estimacionPagada = contrato.getEstimacionesPagadas() != null
										? contrato.getEstimacionesPagadas()
										: new BigDecimal(0);
								estimacionPagada.subtract(estimacionPagoBD.getImporte());
								contrato.setEstimacionesPagadas(estimacionPagada);
							}

							contratoRepository.save(contrato);
						}
					} else {
						if (estimacionPago.getImporte().compareTo(estimacionPagoBD.getImporte()) == 0) {
							if (!estimacionPago.getContrato().equals(estimacionPagoBD.getContrato())) {
								Contrato contrarUpdate = new Contrato();
								contrato = contratoRepository.findById(estimacionPagoBD.getContrato()).get();
								contrarUpdate = contratoRepository.findById(estimacionPago.getContrato()).get();
								if (contrato.getId() != null) {
									BigDecimal totalPagoAplicado = contrato.getPagosAplicados() != null
											? contrato.getPagosAplicados()
											: new BigDecimal(0);
									BigDecimal importe = estimacionPago.getImporte() != null
											? estimacionPago.getImporte()
											: new BigDecimal(0);
									BigDecimal anticipoPagado = contrato.getAnticipoPagado() != null
											? contrato.getAnticipoPagado()
											: new BigDecimal(0);
									totalPagoAplicado = totalPagoAplicado.subtract(importe);
									anticipoPagado = anticipoPagado.subtract(importe);

									BigDecimal montoContrato = contrato.getImporteContratado() != null
											? contrato.getImporteContratado()
											: new BigDecimal(0);

									if (contrato.getTieneImporte() != null) {
										if (contrato.getTieneImporte()) {
											montoContrato = montoContrato.subtract(importe);
											contrato.setImporteContratado(montoContrato);
										}
									}

									montoContrato = contrato.getImporteContratado() != null
											? contrato.getImporteContratado()
											: new BigDecimal(0);

									montoContrato = montoContrato.subtract(totalPagoAplicado);

									contrato.setPagosAplicados(totalPagoAplicado);
									contrato.setSaldoPendienteContrato(montoContrato);
									contrato.setAnticipoPagado(anticipoPagado);

									// IMPORTE BRUTO
									BigDecimal estimacionImporteBruto = estimacionPago.getImporteBruto();
									BigDecimal importeBruto = contrato.getImporteBruto() != null
											? contrato.getImporteBruto()
											: new BigDecimal(0);
									importeBruto = importeBruto.subtract(estimacionImporteBruto);
									contrato.setImporteBruto(importeBruto);

									// RETENCION VICIOS OCULTOS
									BigDecimal retencionViciosOcultos = estimacionPago.getRetencionViciosOcultos();
									BigDecimal retencion = contrato.getRetencionViciosOcultos() != null
											? contrato.getRetencionViciosOcultos()
											: new BigDecimal(0);
									retencion = retencion.subtract(retencionViciosOcultos);
									contrato.setRetencionViciosOcultos(retencion);

									// AMORTIZACIÓN ANTICIPO
									BigDecimal amortizcacionEstimacion = estimacionPago.getAmortizacionAnticipo();
									BigDecimal amortizacion = contrato.getAmortizacionAnticipo() != null
											? contrato.getAmortizacionAnticipo()
											: new BigDecimal(0);
									amortizacion = amortizacion.subtract(amortizcacionEstimacion);
									contrato.setAmortizacionAnticipo(amortizacion);

									// IVA
									BigDecimal ivaEstimacion = estimacionPago.getIva();
									BigDecimal iva = contrato.getIva() != null ? contrato.getIva() : new BigDecimal(0);
									iva = iva.subtract(ivaEstimacion);
									contrato.setIva(iva);

									// RETENCIÓN IVA
									BigDecimal retencionIvaEstimacion = estimacionPago.getRetencionIva();
									BigDecimal retencionIva = contrato.getRetencionIva() != null
											? contrato.getRetencionIva()
											: new BigDecimal(0);
									retencionIva = retencionIva.subtract(retencionIvaEstimacion);
									contrato.setRetencionIva(retencionIva);

									// ISR
									BigDecimal estimacionIsr = estimacionPago.getDeducciones();
									BigDecimal isr = contrato.getIsr() != null ? contrato.getIsr() : new BigDecimal(0);
									isr = isr.subtract(estimacionIsr);
									contrato.setIsr(isr);

									// DEDUCCIONES
									BigDecimal estimacionDeducciones = estimacionPago.getDeducciones();
									BigDecimal deducciones = contrato.getDeducciones() != null
											? contrato.getDeducciones()
											: new BigDecimal(0);
									deducciones = deducciones.subtract(estimacionDeducciones);
									contrato.setDeducciones(deducciones);

									contratoRepository.save(contrato);
								}
								if (contrarUpdate.getId() != null) {
									BigDecimal totalPagoAplicado = contrarUpdate.getPagosAplicados() != null
											? contrarUpdate.getPagosAplicados()
											: new BigDecimal(0);
									BigDecimal importe = estimacionPago.getImporte() != null
											? estimacionPago.getImporte()
											: new BigDecimal(0);
									BigDecimal anticipoPagado = contrato.getAnticipoPagado() != null
											? contrato.getAnticipoPagado()
											: new BigDecimal(0);
									totalPagoAplicado = totalPagoAplicado.add(importe);
									anticipoPagado = anticipoPagado.add(importe);

									BigDecimal montoContrato = contrarUpdate.getImporteContratado() != null
											? contrarUpdate.getImporteContratado()
											: new BigDecimal(0);

									if (contrarUpdate.getTieneImporte()) {
										montoContrato = montoContrato.add(importe);
										contrarUpdate.setImporteContratado(montoContrato);
									}

									montoContrato = contrarUpdate.getImporteContratado() != null
											? contrarUpdate.getImporteContratado()
											: new BigDecimal(0);

									montoContrato = montoContrato.subtract(totalPagoAplicado);

									contrarUpdate.setPagosAplicados(totalPagoAplicado);
									contrarUpdate.setSaldoPendienteContrato(montoContrato);
									contrarUpdate.setAnticipoPagado(anticipoPagado);

									contratoRepository.save(contrarUpdate);
								}
							} else {
								if (TipoConceptoEnum.ABONO_ESTIMACION.getDescripcion()
										.equals(estimacionPagoBD.getConcepto())) {
									contrato = contratoRepository.findById(estimacionPago.getContrato()).get();
									BigDecimal estimacionPagada = contrato.getEstimacionesPagadas() != null
											? contrato.getEstimacionesPagadas()
											: new BigDecimal(0);
									estimacionPagada = estimacionPagada.subtract(estimacionPagoBD.getImporte());
									contrato.setEstimacionesPagadas(estimacionPagada);

									contratoRepository.save(contrato);

									List<EstimacionPago> estimacionPagos = estimacionPagoRepository
											.findByNumeroAbonoAndContrato(estimacionPagoBD.getNumeroAbono(),
													estimacionPagoBD.getContrato());

									for (EstimacionPago estimacionPago2 : estimacionPagos) {
										if (estimacionPago2.getConcepto().equals("ESTIMACIÓN")
												&& estimacionPago2.getNumeroAbono()
														.equals(estimacionPagoBD.getNumeroAbono())
												&& estimacionPago2.getContrato()
														.equals(estimacionPagoBD.getContrato())) {
											BigDecimal total = estimacionPago2.getImporteAbono();
											BigDecimal importe = estimacionPago.getImporte() != null
													? estimacionPago.getImporte()
													: new BigDecimal(0);
											total = total.subtract(importe);
											estimacionPago2.setImporteAbono(total);
											System.out.println("ESTIMACIÓN ACTUALIZADA");
											estimacionPagoRepository.save(estimacionPago2);
										}
									}
								}
							}
						}
					}
				}
			}
			// CALCULAR PAGOS APLICADOS - ABONO A CONTRATO
			if (estimacionPago.getContrato() != null && estimacionPago.getImporte() != null
					&& (estimacionPago.getConcepto() != null && !estimacionPago.getConcepto().isEmpty()
							&& TipoConceptoEnum.ABONO_CONTRATO.getDescripcion().equals(estimacionPago.getConcepto()))) {
				estimacionPagoBD = estimacionPagoRepository.findById(estimacionPago.getId()).get();

				if (estimacionPago.getImporte().compareTo(estimacionPagoBD.getImporte()) == 1) {
					contrato = contratoRepository.findById(estimacionPago.getContrato()).get();
					if (contrato.getId() != null) {

						BigDecimal totalPagoAplicado = contrato.getPagosAplicados() != null
								? contrato.getPagosAplicados()
								: new BigDecimal(0);
						BigDecimal importeEstimacion = estimacionPago.getImporte() != null ? estimacionPago.getImporte()
								: new BigDecimal(0);
						importeEstimacion = importeEstimacion.subtract(estimacionPagoBD.getImporte());
						totalPagoAplicado = totalPagoAplicado.add(importeEstimacion);

						BigDecimal montoContrato = contrato.getImporteContratado() != null
								? contrato.getImporteContratado()
								: new BigDecimal(0);

						if (contrato.getTieneImporte() != null) {
							if (contrato.getTieneImporte()) {
								montoContrato = montoContrato.add(importeEstimacion);
								contrato.setImporteContratado(montoContrato);
							}
						}

						montoContrato = contrato.getImporteContratado() != null ? contrato.getImporteContratado()
								: new BigDecimal(0);

						montoContrato = montoContrato.subtract(totalPagoAplicado);

						contrato.setPagosAplicados(totalPagoAplicado);
						contrato.setSaldoPendienteContrato(montoContrato);

						// IMPORTE BRUTO
						BigDecimal estimacionImporteBruto = estimacionPago.getImporteBruto();
						BigDecimal importeBruto = contrato.getImporteBruto() != null ? contrato.getImporteBruto()
								: new BigDecimal(0);
						importeBruto = importeBruto.subtract(estimacionImporteBruto);
						contrato.setImporteBruto(importeBruto);

						// RETENCION VICIOS OCULTOS
						BigDecimal retencionViciosOcultos = estimacionPago.getRetencionViciosOcultos();
						BigDecimal retencion = contrato.getRetencionViciosOcultos() != null
								? contrato.getRetencionViciosOcultos()
								: new BigDecimal(0);
						retencion = retencion.subtract(retencionViciosOcultos);
						contrato.setRetencionViciosOcultos(retencion);

						// AMORTIZACIÓN ANTICIPO
						BigDecimal amortizcacionEstimacion = estimacionPago.getAmortizacionAnticipo();
						BigDecimal amortizacion = contrato.getAmortizacionAnticipo() != null
								? contrato.getAmortizacionAnticipo()
								: new BigDecimal(0);
						amortizacion = amortizacion.subtract(amortizcacionEstimacion);
						contrato.setAmortizacionAnticipo(amortizacion);

						// IVA
						BigDecimal ivaEstimacion = estimacionPago.getIva();
						BigDecimal iva = contrato.getIva() != null ? contrato.getIva() : new BigDecimal(0);
						iva = iva.subtract(ivaEstimacion);
						contrato.setIva(iva);

						// RETENCIÓN IVA
						BigDecimal retencionIvaEstimacion = estimacionPago.getRetencionIva();
						BigDecimal retencionIva = contrato.getRetencionIva() != null ? contrato.getRetencionIva()
								: new BigDecimal(0);
						retencionIva = retencionIva.subtract(retencionIvaEstimacion);
						contrato.setRetencionIva(retencionIva);

						// ISR
						BigDecimal estimacionIsr = estimacionPago.getDeducciones();
						BigDecimal isr = contrato.getIsr() != null ? contrato.getIsr() : new BigDecimal(0);
						isr = isr.subtract(estimacionIsr);
						contrato.setIsr(isr);

						// DEDUCCIONES
						BigDecimal estimacionDeducciones = estimacionPago.getDeducciones();
						BigDecimal deducciones = contrato.getDeducciones() != null ? contrato.getDeducciones()
								: new BigDecimal(0);
						deducciones = deducciones.subtract(estimacionDeducciones);
						contrato.setDeducciones(deducciones);

						if (TipoConceptoEnum.ABONO_ESTIMACION.getDescripcion().equals(estimacionPagoBD.getConcepto())) {
							BigDecimal estimacionPagada = contrato.getEstimacionesPagadas() != null
									? contrato.getEstimacionesPagadas()
									: new BigDecimal(0);
							estimacionPagada.subtract(estimacionPagoBD.getImporte());
							contrato.setEstimacionesPagadas(estimacionPagada);
						}

						contratoRepository.save(contrato);

						totalPagoAplicado = new BigDecimal(0);
					}
				} else {
					if (estimacionPago.getImporte().compareTo(estimacionPagoBD.getImporte()) == -1) {
						contrato = contratoRepository.findById(estimacionPago.getContrato()).get();
						if (contrato.getId() != null) {
							BigDecimal totalPagoAplicado = contrato.getPagosAplicados() != null
									? contrato.getPagosAplicados()
									: new BigDecimal(0);
							BigDecimal importe = estimacionPago.getImporte() != null ? estimacionPago.getImporte()
									: new BigDecimal(0);
							importe = importe.subtract(estimacionPagoBD.getImporte());
							totalPagoAplicado = totalPagoAplicado.add(importe);

							BigDecimal montoContrato = contrato.getImporteContratado() != null
									? contrato.getImporteContratado()
									: new BigDecimal(0);

							if (contrato.getTieneImporte() != null) {
								if (contrato.getTieneImporte()) {
									montoContrato = montoContrato.add(importe);
									contrato.setImporteContratado(montoContrato);
								}
							}

							montoContrato = contrato.getImporteContratado() != null ? contrato.getImporteContratado()
									: new BigDecimal(0);

							montoContrato = montoContrato.subtract(totalPagoAplicado);

							contrato.setPagosAplicados(totalPagoAplicado);
							contrato.setSaldoPendienteContrato(montoContrato);

							// IMPORTE BRUTO
							BigDecimal estimacionImporteBruto = estimacionPago.getImporteBruto();
							BigDecimal importeBruto = contrato.getImporteBruto() != null ? contrato.getImporteBruto()
									: new BigDecimal(0);
							importeBruto = importeBruto.subtract(estimacionImporteBruto);
							contrato.setImporteBruto(importeBruto);

							// RETENCION VICIOS OCULTOS
							BigDecimal retencionViciosOcultos = estimacionPago.getRetencionViciosOcultos();
							BigDecimal retencion = contrato.getRetencionViciosOcultos() != null
									? contrato.getRetencionViciosOcultos()
									: new BigDecimal(0);
							retencion = retencion.subtract(retencionViciosOcultos);
							contrato.setRetencionViciosOcultos(retencion);

							// AMORTIZACIÓN ANTICIPO
							BigDecimal amortizcacionEstimacion = estimacionPago.getAmortizacionAnticipo();
							BigDecimal amortizacion = contrato.getAmortizacionAnticipo() != null
									? contrato.getAmortizacionAnticipo()
									: new BigDecimal(0);
							amortizacion = amortizacion.subtract(amortizcacionEstimacion);
							contrato.setAmortizacionAnticipo(amortizacion);

							// IVA
							BigDecimal ivaEstimacion = estimacionPago.getIva();
							BigDecimal iva = contrato.getIva() != null ? contrato.getIva() : new BigDecimal(0);
							iva = iva.subtract(ivaEstimacion);
							contrato.setIva(iva);

							// RETENCIÓN IVA
							BigDecimal retencionIvaEstimacion = estimacionPago.getRetencionIva();
							BigDecimal retencionIva = contrato.getRetencionIva() != null ? contrato.getRetencionIva()
									: new BigDecimal(0);
							retencionIva = retencionIva.subtract(retencionIvaEstimacion);
							contrato.setRetencionIva(retencionIva);

							// ISR
							BigDecimal estimacionIsr = estimacionPago.getDeducciones();
							BigDecimal isr = contrato.getIsr() != null ? contrato.getIsr() : new BigDecimal(0);
							isr = isr.subtract(estimacionIsr);
							contrato.setIsr(isr);

							// DEDUCCIONES
							BigDecimal estimacionDeducciones = estimacionPago.getDeducciones();
							BigDecimal deducciones = contrato.getDeducciones() != null ? contrato.getDeducciones()
									: new BigDecimal(0);
							deducciones = deducciones.subtract(estimacionDeducciones);
							contrato.setDeducciones(deducciones);

							if (TipoConceptoEnum.ABONO_ESTIMACION.getDescripcion()
									.equals(estimacionPagoBD.getConcepto())) {
								BigDecimal estimacionPagada = contrato.getEstimacionesPagadas() != null
										? contrato.getEstimacionesPagadas()
										: new BigDecimal(0);
								estimacionPagada.subtract(estimacionPagoBD.getImporte());
								contrato.setEstimacionesPagadas(estimacionPagada);
							}

							totalPagoAplicado = new BigDecimal(0);

							contratoRepository.save(contrato);
						}
					} else {
						if (estimacionPago.getImporte().compareTo(estimacionPagoBD.getImporte()) == 0) {
							if (!estimacionPago.getContrato().equals(estimacionPagoBD.getContrato())) {
								Contrato contrarUpdate = new Contrato();
								contrato = contratoRepository.findById(estimacionPagoBD.getContrato()).get();
								contrarUpdate = contratoRepository.findById(estimacionPago.getContrato()).get();
								if (contrato.getId() != null) {
									BigDecimal totalPagoAplicado = contrato.getPagosAplicados() != null
											? contrato.getPagosAplicados()
											: new BigDecimal(0);
									BigDecimal importe = estimacionPago.getImporte() != null
											? estimacionPago.getImporte()
											: new BigDecimal(0);
									totalPagoAplicado = totalPagoAplicado.subtract(importe);

									BigDecimal montoContrato = contrato.getImporteContratado() != null
											? contrato.getImporteContratado()
											: new BigDecimal(0);

									if (contrato.getTieneImporte() != null) {
										if (contrato.getTieneImporte()) {
											montoContrato = montoContrato.subtract(importe);
											contrato.setImporteContratado(montoContrato);
										}
									}

									montoContrato = contrato.getImporteContratado() != null
											? contrato.getImporteContratado()
											: new BigDecimal(0);

									montoContrato = montoContrato.subtract(totalPagoAplicado);

									contrato.setPagosAplicados(totalPagoAplicado);
									contrato.setSaldoPendienteContrato(montoContrato);

									// IMPORTE BRUTO
									BigDecimal estimacionImporteBruto = estimacionPago.getImporteBruto();
									BigDecimal importeBruto = contrato.getImporteBruto() != null
											? contrato.getImporteBruto()
											: new BigDecimal(0);
									importeBruto = importeBruto.subtract(estimacionImporteBruto);
									contrato.setImporteBruto(importeBruto);

									// RETENCION VICIOS OCULTOS
									BigDecimal retencionViciosOcultos = estimacionPago.getRetencionViciosOcultos();
									BigDecimal retencion = contrato.getRetencionViciosOcultos() != null
											? contrato.getRetencionViciosOcultos()
											: new BigDecimal(0);
									retencion = retencion.subtract(retencionViciosOcultos);
									contrato.setRetencionViciosOcultos(retencion);

									// AMORTIZACIÓN ANTICIPO
									BigDecimal amortizcacionEstimacion = estimacionPago.getAmortizacionAnticipo();
									BigDecimal amortizacion = contrato.getAmortizacionAnticipo() != null
											? contrato.getAmortizacionAnticipo()
											: new BigDecimal(0);
									amortizacion = amortizacion.subtract(amortizcacionEstimacion);
									contrato.setAmortizacionAnticipo(amortizacion);

									// IVA
									BigDecimal ivaEstimacion = estimacionPago.getIva();
									BigDecimal iva = contrato.getIva() != null ? contrato.getIva() : new BigDecimal(0);
									iva = iva.subtract(ivaEstimacion);
									contrato.setIva(iva);

									// RETENCIÓN IVA
									BigDecimal retencionIvaEstimacion = estimacionPago.getRetencionIva();
									BigDecimal retencionIva = contrato.getRetencionIva() != null
											? contrato.getRetencionIva()
											: new BigDecimal(0);
									retencionIva = retencionIva.subtract(retencionIvaEstimacion);
									contrato.setRetencionIva(retencionIva);

									// ISR
									BigDecimal estimacionIsr = estimacionPago.getDeducciones();
									BigDecimal isr = contrato.getIsr() != null ? contrato.getIsr() : new BigDecimal(0);
									isr = isr.subtract(estimacionIsr);
									contrato.setIsr(isr);

									// DEDUCCIONES
									BigDecimal estimacionDeducciones = estimacionPago.getDeducciones();
									BigDecimal deducciones = contrato.getDeducciones() != null
											? contrato.getDeducciones()
											: new BigDecimal(0);
									deducciones = deducciones.subtract(estimacionDeducciones);
									contrato.setDeducciones(deducciones);

									contratoRepository.save(contrato);
								}
								if (contrarUpdate.getId() != null) {

									BigDecimal totalPagoAplicado = contrarUpdate.getPagosAplicados() != null
											? contrarUpdate.getPagosAplicados()
											: new BigDecimal(0);
									BigDecimal importe = estimacionPago.getImporte() != null
											? estimacionPago.getImporte()
											: new BigDecimal(0);
									totalPagoAplicado = totalPagoAplicado.add(importe);

									BigDecimal montoContrato = contrarUpdate.getImporteContratado() != null
											? contrarUpdate.getImporteContratado()
											: new BigDecimal(0);

									if (contrarUpdate.getTieneImporte()) {
										montoContrato = montoContrato.add(importe);
										contrarUpdate.setImporteContratado(montoContrato);
									}

									montoContrato = contrarUpdate.getImporteContratado() != null
											? contrarUpdate.getImporteContratado()
											: new BigDecimal(0);

									montoContrato = montoContrato.subtract(totalPagoAplicado);

									contrarUpdate.setPagosAplicados(totalPagoAplicado);
									contrarUpdate.setSaldoPendienteContrato(montoContrato);

									contratoRepository.save(contrarUpdate);
								}
							} else {
								contrato = contratoRepository.findById(estimacionPago.getContrato()).get();
								if (TipoConceptoEnum.ABONO_ESTIMACION.getDescripcion()
										.equals(estimacionPagoBD.getConcepto())) {
									BigDecimal estimacionPagada = contrato.getEstimacionesPagadas() != null
											? contrato.getEstimacionesPagadas()
											: new BigDecimal(0);
									estimacionPagada = estimacionPagada.subtract(estimacionPagoBD.getImporte());
									contrato.setEstimacionesPagadas(estimacionPagada);

									contratoRepository.save(contrato);

									List<EstimacionPago> estimacionPagos = estimacionPagoRepository
											.findByNumeroAbonoAndContrato(estimacionPagoBD.getNumeroAbono(),
													estimacionPagoBD.getContrato());

									for (EstimacionPago estimacionPago2 : estimacionPagos) {
										if (estimacionPago2.getConcepto().equals("ESTIMACIÓN")
												&& estimacionPago2.getNumeroAbono()
														.equals(estimacionPagoBD.getNumeroAbono())
												&& estimacionPago2.getContrato()
														.equals(estimacionPagoBD.getContrato())) {
											BigDecimal total = estimacionPago2.getImporteAbono();
											BigDecimal importe = estimacionPago.getImporte() != null
													? estimacionPago.getImporte()
													: new BigDecimal(0);
											total = total.subtract(importe);
											estimacionPago2.setImporteAbono(total);
											System.out.println("ESTIMACIÓN ACTUALIZADA");
											estimacionPagoRepository.save(estimacionPago2);
										}
									}
								}
							}
						}
					}
				}
			}
			// GUARDAR
		} else {
			// CALCULAR ESTIMACIONES PROGRAMADAS
			if (estimacionPago.getContrato() != null && estimacionPago.getImporte() != null
					&& (estimacionPago.getConcepto() != null && !estimacionPago.getConcepto().isEmpty()
							&& TipoConceptoEnum.ESTIMACION.getDescripcion().equals(estimacionPago.getConcepto()))) {

				contrato = contratoRepository.findById(estimacionPago.getContrato()).get();

				System.out.println("CONCEPTO ESTIMACIÓN > Contrato " + contrato.getFolio() + " < importe > + "
						+ estimacionPago.getImporte());

				if (contrato.getId() != null) {
					BigDecimal total = contrato.getEstimacionesProgramadas() != null
							? contrato.getEstimacionesProgramadas()
							: new BigDecimal(0);
					total = total.add(estimacionPago.getImporte());
					contrato.setEstimacionesProgramadas(total);

					BigDecimal importeBruto = contrato.getImporteBruto() != null ? contrato.getImporteBruto()
							: new BigDecimal(0);
					importeBruto = importeBruto.add(estimacionPago.getImporteBruto());

					BigDecimal retencion = contrato.getRetencionViciosOcultos() != null
							? contrato.getRetencionViciosOcultos()
							: new BigDecimal(0);
					retencion = retencion.add(estimacionPago.getRetencionViciosOcultos());

					BigDecimal amortizacion = contrato.getAmortizacionAnticipo() != null
							? contrato.getAmortizacionAnticipo()
							: new BigDecimal(0);
					amortizacion = amortizacion.add(estimacionPago.getAmortizacionAnticipo());

					BigDecimal iva = contrato.getIva() != null ? contrato.getIva() : new BigDecimal(0);
					iva = iva.add(estimacionPago.getIva());

					BigDecimal retencionIva = contrato.getRetencionIva() != null ? contrato.getRetencionIva()
							: new BigDecimal(0);
					retencionIva = retencionIva.add(estimacionPago.getRetencionIva());

					BigDecimal isr = contrato.getIsr() != null ? contrato.getIsr() : new BigDecimal(0);
					isr = isr.add(estimacionPago.getIsr());

					BigDecimal deducciones = contrato.getDeducciones() != null ? contrato.getDeducciones()
							: new BigDecimal(0);
					deducciones = deducciones.add(estimacionPago.getDeducciones());

					contrato.setAmortizacionAnticipo(amortizacion);
					contrato.setDeducciones(deducciones);
					contrato.setIsr(isr);
					contrato.setIva(iva);
					contrato.setRetencionIva(retencionIva);
					contrato.setRetencionViciosOcultos(retencion);
					contrato.setImporteBruto(importeBruto);

					if (contrato.getTieneImporte() != null) {
						if (contrato.getTieneImporte()) {
							BigDecimal montoContratado = contrato.getImporteContratado() != null
									? contrato.getImporteContratado()
									: new BigDecimal(0);
							montoContratado = montoContratado.add(estimacionPago.getImporte());
							contrato.setImporteContratado(montoContratado);
							BigDecimal pagosAplicados = contrato.getPagosAplicados() != null
									? contrato.getPagosAplicados()
									: new BigDecimal(0);
							montoContratado = montoContratado.subtract(pagosAplicados);
							contrato.setSaldoPendienteContrato(total);
						}
					}
					contratoRepository.save(contrato);

					total = new BigDecimal(0);
				}
			}

			// CALCULAR ESTIMACIONES PAGADAS
			if (estimacionPago.getContrato() != null && estimacionPago.getImporte() != null
					&& (estimacionPago.getConcepto() != null && !estimacionPago.getConcepto().isEmpty()
							&& TipoConceptoEnum.ABONO_ESTIMACION.getDescripcion()
									.equals(estimacionPago.getConcepto()))) {
				contrato = contratoRepository.findById(estimacionPago.getContrato()).get();
				System.out.println("CONCEPTO ABONO A ESTIMACIÓN > Contrato " + contrato.getFolio() + " < importe > + "
						+ estimacionPago.getImporte());
				if (contrato.getId() != null) {
					BigDecimal totalEstimacion = contrato.getEstimacionesPagadas() != null
							? contrato.getEstimacionesPagadas()
							: new BigDecimal(0);
					totalEstimacion = totalEstimacion.add(estimacionPago.getImporte());

					BigDecimal totalPagoAplicado = contrato.getPagosAplicados() != null ? contrato.getPagosAplicados()
							: new BigDecimal(0);
					totalPagoAplicado = totalPagoAplicado.add(estimacionPago.getImporte());

					BigDecimal montoContrato = contrato.getImporteContratado() != null ? contrato.getImporteContratado()
							: new BigDecimal(0);

					montoContrato = montoContrato.subtract(totalPagoAplicado);

					contrato.setEstimacionesPagadas(totalEstimacion);
					contrato.setPagosAplicados(totalPagoAplicado);
					contrato.setSaldoPendienteContrato(montoContrato);

					contratoRepository.save(contrato);

					totalEstimacion = new BigDecimal(0);
					totalPagoAplicado = new BigDecimal(0);
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
							total = new BigDecimal(0);
						} else {
							estimacionPago2.setImporteAbono(estimacionPago.getImporte());
						}
						estimacionPagoRepository.save(estimacionPago2);
					}
				}
			}

			// CALCULAR PAGOS APLICADOS
			if (estimacionPago.getContrato() != null && estimacionPago.getImporte() != null
					&& (estimacionPago.getConcepto() != null && !estimacionPago.getConcepto().isEmpty()
							&& TipoConceptoEnum.ABONO_ANTICIPO.getDescripcion().equals(estimacionPago.getConcepto()))) {
				contrato = contratoRepository.findById(estimacionPago.getContrato()).get();
				System.out.println("CONCEPTO ABONO A ANTICIPO > Contrato " + contrato.getFolio() + " < importe > + "
						+ estimacionPago.getImporte());
				if (contrato.getId() != null) {
					BigDecimal totalPagoAplicado = contrato.getPagosAplicados() != null ? contrato.getPagosAplicados()
							: new BigDecimal(0);
					BigDecimal anticipoPagado = contrato.getAnticipoPagado() != null ? contrato.getAnticipoPagado()
							: new BigDecimal(0);
					totalPagoAplicado = totalPagoAplicado.add(estimacionPago.getImporte());
					anticipoPagado = anticipoPagado.add(estimacionPago.getImporte());

					BigDecimal montoContrato = contrato.getImporteContratado() != null ? contrato.getImporteContratado()
							: new BigDecimal(0);

					if (contrato.getTieneImporte() != null) {
						if (contrato.getTieneImporte()) {
							montoContrato = montoContrato.add(estimacionPago.getImporte());
							contrato.setImporteContratado(montoContrato);
						}
					}
					montoContrato = contrato.getImporteContratado() != null ? contrato.getImporteContratado()
							: new BigDecimal(0);

					montoContrato = montoContrato.subtract(totalPagoAplicado);

					contrato.setPagosAplicados(totalPagoAplicado);
					contrato.setSaldoPendienteContrato(montoContrato);
					contrato.setAnticipoPagado(anticipoPagado);

					contratoRepository.save(contrato);

					totalPagoAplicado = new BigDecimal(0);
				}
			}

			// CALCULAR PAGOS APLICADOS
			if (estimacionPago.getContrato() != null && estimacionPago.getImporte() != null
					&& (estimacionPago.getConcepto() != null && !estimacionPago.getConcepto().isEmpty()
							&& TipoConceptoEnum.ABONO_CONTRATO.getDescripcion().equals(estimacionPago.getConcepto()))) {
				contrato = contratoRepository.findById(estimacionPago.getContrato()).get();
				System.out.println("CONCEPTO ABONO A ANTICIPO > Contrato " + contrato.getFolio() + " < importe > + "
						+ estimacionPago.getImporte());
				if (contrato.getId() != null) {
					BigDecimal totalPagoAplicado = contrato.getPagosAplicados() != null ? contrato.getPagosAplicados()
							: new BigDecimal(0);
					totalPagoAplicado = totalPagoAplicado.add(estimacionPago.getImporte());

					BigDecimal montoContrato = contrato.getImporteContratado() != null ? contrato.getImporteContratado()
							: new BigDecimal(0);

					if (contrato.getTieneImporte() != null) {
						if (contrato.getTieneImporte()) {
							montoContrato = montoContrato.add(estimacionPago.getImporte());
							contrato.setImporteContratado(montoContrato);
						}
					}
					montoContrato = contrato.getImporteContratado() != null ? contrato.getImporteContratado()
							: new BigDecimal(0);

					montoContrato = montoContrato.subtract(totalPagoAplicado);

					contrato.setPagosAplicados(totalPagoAplicado);
					contrato.setSaldoPendienteContrato(montoContrato);

					contratoRepository.save(contrato);

					totalPagoAplicado = new BigDecimal(0);
				}
			}
		}
	}

}
