package com.gpate.services.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.gpate.dtos.EstimacionPagoDto;
import com.gpate.dtos.FileStorageProperties;
import com.gpate.model.Contrato;
import com.gpate.model.EstimacionPago;
import com.gpate.repository.ContratoRepository;
import com.gpate.repository.EstimacionPagoRepository;
import com.gpate.services.IEstimacionPagoService;
import com.gpate.util.EstimacionPagoUtil;
import com.gpate.util.ExcelEstimacionPagoGenerator;

@Service
public class EstimacionPagoService implements IEstimacionPagoService {

	private final Path fileUploadLocation;

	@Autowired
	private EstimacionPagoRepository estimacionPagoRepository;

	@Autowired
	private ContratoRepository contratoRepository;

	@Autowired
	private CerrarConexionService cerrarConexionService;

	@Autowired
	public EstimacionPagoService(FileStorageProperties fileStorageProperties) {
		this.fileUploadLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();

		try {
			Files.createDirectories(this.fileUploadLocation);
		} catch (Exception e) {
			throw new RuntimeException("No se puede crear el directorio donde desea cargar el archivo.", e);
		}
	}

	@Override
	public String eliminarEstimacionPago(Long id) {
		String mensaje = "";
		EstimacionPago estimacionPago = null;
		try {
			estimacionPago = estimacionPagoRepository.findById(id).get();
			Long idContrato = 0L;
			BigDecimal importe = estimacionPago.getImporte();
			BigDecimal importeBrutoEstimacion = estimacionPago.getImporteBruto();
			BigDecimal retencionViciosOcultosEstimacion = estimacionPago.getRetencionViciosOcultos();
			BigDecimal amortizacionAnticipoEstimacion = estimacionPago.getAmortizacionAnticipo();
			BigDecimal ivaEstimacion = estimacionPago.getIva();
			BigDecimal retencionIvaEstimacion = estimacionPago.getRetencionIva();
			BigDecimal isrEstimacion = estimacionPago.getIsr();
			BigDecimal deduccionesEstimacion = estimacionPago.getDeducciones();
			String concepto = estimacionPago.getConcepto();
			Integer numeroAbono = Integer.parseInt(estimacionPago.getNumeroAbono());
			if (estimacionPago != null) {
				idContrato = estimacionPago.getContrato();
				estimacionPagoRepository.delete(estimacionPago);
				List<EstimacionPago> estimacionPagosAbonoEstimacion = estimacionPagoRepository
						.findByNumeroAbonoAndContrato(estimacionPago.getNumeroAbono(), idContrato);
				BigDecimal sumaAbonoEstimacion = new BigDecimal(0);
				for (EstimacionPago estimacionPagoAbonoEstimacion : estimacionPagosAbonoEstimacion) {
					if ("ESTIMACIÓN".equals(concepto)
							&& "ABONO A ESTIMACIÓN".equals(estimacionPagoAbonoEstimacion.getConcepto())) {
						sumaAbonoEstimacion = sumaAbonoEstimacion.add(estimacionPagoAbonoEstimacion.getImporte());
						estimacionPagoRepository.delete(estimacionPagoAbonoEstimacion);
					}
				}
				Contrato contrato = contratoRepository.findById(idContrato).get();
				if (contrato != null) {
					BigDecimal estimacionProgramada = contrato.getEstimacionesProgramadas();
					BigDecimal estimcacionPagada = contrato.getEstimacionesPagadas();
					BigDecimal pagoAplicado = contrato.getPagosAplicados();
					BigDecimal saldoPendiente = contrato.getSaldoPendienteContrato();
					BigDecimal montoContrato = contrato.getImporteContratado();
					BigDecimal anticipoPagado = contrato.getAnticipoPagado();
					BigDecimal importeBruto = contrato.getImporteBruto();
					BigDecimal retencionViciosOcultos = contrato.getRetencionViciosOcultos();
					BigDecimal amortizacionAnticipo = contrato.getAmortizacionAnticipo();
					BigDecimal iva = contrato.getIva();
					BigDecimal retencionIva = contrato.getRetencionIva();
					BigDecimal isr = contrato.getIsr();
					BigDecimal deducciones = contrato.getDeducciones();

					List<EstimacionPago> estimacionPagos = estimacionPagoRepository.findByContrato(idContrato);
					for (EstimacionPago estimacionPago2 : estimacionPagos) {
						Integer numeroAbono2 = Integer.parseInt(estimacionPago2.getNumeroAbono());
						if (estimacionPago2.getConcepto().equals(concepto)) {
							if (numeroAbono < numeroAbono2) {
								if (numeroAbono < 10) {
									estimacionPago2.setNumeroAbono("0" + numeroAbono);
								} else {
									estimacionPago2.setNumeroAbono(numeroAbono + "");
								}
								numeroAbono++;
								estimacionPagoRepository.save(estimacionPago2);
							}
						}
					}

					if ("ESTIMACIÓN".equals(concepto)) {
						estimacionProgramada = estimacionProgramada.subtract(importe);
						estimcacionPagada = estimcacionPagada.subtract(sumaAbonoEstimacion);
						pagoAplicado = pagoAplicado.subtract(sumaAbonoEstimacion);
						importeBruto = importeBruto.subtract(importeBrutoEstimacion);
						retencionViciosOcultos = retencionViciosOcultos.subtract(retencionViciosOcultosEstimacion);
						amortizacionAnticipo = amortizacionAnticipo.subtract(amortizacionAnticipoEstimacion);
						iva = iva.subtract(ivaEstimacion);
						retencionIva = retencionIva.subtract(retencionIvaEstimacion);
						isr = isr.subtract(isrEstimacion);
						deducciones = deducciones.subtract(deduccionesEstimacion);
						if (contrato.getTieneImporte() != null) {
							if (contrato.getTieneImporte()) {
								montoContrato = montoContrato.subtract(importe);
								contrato.setImporteContratado(montoContrato);
							}
						}
						saldoPendiente = montoContrato.subtract(pagoAplicado);
						contrato.setEstimacionesProgramadas(estimacionProgramada);
						contrato.setEstimacionesPagadas(estimcacionPagada);
						contrato.setPagosAplicados(pagoAplicado);
						contrato.setSaldoPendienteContrato(saldoPendiente);
						contrato.setImporteBruto(importeBruto);
						contrato.setRetencionViciosOcultos(retencionViciosOcultos);
						contrato.setAmortizacionAnticipo(amortizacionAnticipo);
						contrato.setIva(iva);
						contrato.setRetencionIva(retencionIva);
						contrato.setIsr(isr);
						contrato.setDeducciones(deducciones);
					}
					if ("ABONO A ESTIMACIÓN".equals(concepto)) {
						List<EstimacionPago> estimacionPagosEstimacion = estimacionPagoRepository
								.findByNumeroAbonoAndContrato(estimacionPago.getNumeroAbono(), idContrato);
						for (EstimacionPago estimacionPagoEstimacion : estimacionPagosEstimacion) {
							if ("ESTIMACIÓN".equals(estimacionPagoEstimacion.getConcepto())) {
								BigDecimal importeEstimacion = estimacionPagoEstimacion.getImporteAbono();
								importeEstimacion = importeEstimacion.subtract(importe);
								estimacionPagoEstimacion.setImporteAbono(importeEstimacion);
								estimacionPagoRepository.save(estimacionPagoEstimacion);
							}
						}
						estimcacionPagada = estimcacionPagada.subtract(importe);
						pagoAplicado = pagoAplicado.subtract(importe);
						if (contrato.getTieneImporte() != null) {
							if (contrato.getTieneImporte()) {
								montoContrato = montoContrato.subtract(importe);
								contrato.setImporteContratado(montoContrato);
							}
						}
						saldoPendiente = montoContrato.subtract(pagoAplicado);
						contrato.setEstimacionesPagadas(estimcacionPagada);
						contrato.setPagosAplicados(pagoAplicado);
						contrato.setSaldoPendienteContrato(saldoPendiente);
					}
					if ("ABONO A CONTRATO".equals(concepto)) {
						pagoAplicado = pagoAplicado.subtract(importe);
						if (contrato.getTieneImporte() != null) {
							if (contrato.getTieneImporte()) {
								montoContrato = montoContrato.subtract(importe);
								contrato.setImporteContratado(montoContrato);
							}
						}
						saldoPendiente = montoContrato.subtract(pagoAplicado);
						contrato.setPagosAplicados(pagoAplicado);
						contrato.setSaldoPendienteContrato(saldoPendiente);
					}
					if ("ABONO A ANTICIPO".equals(concepto)) {
						pagoAplicado = pagoAplicado.subtract(importe);
						anticipoPagado = anticipoPagado.subtract(importe);
						if (contrato.getTieneImporte() != null) {
							if (contrato.getTieneImporte()) {
								montoContrato = montoContrato.subtract(importe);
								contrato.setImporteContratado(montoContrato);
							}
						}
						saldoPendiente = montoContrato.subtract(pagoAplicado);
						contrato.setPagosAplicados(pagoAplicado);
						contrato.setSaldoPendienteContrato(saldoPendiente);
					}
					contratoRepository.save(contrato);
				}
			}
			mensaje = "Ítem eliminado correctamente.";
		} catch (Exception e) {
			mensaje = "ïtem no encontrado.";
		}

		return mensaje;
	}

	@Override
	public void generarEstadoCuenta(HttpServletResponse response) throws IOException {
		response.setContentType("application/octet-stream");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=listado_estimaciones_pagos_" + currentDateTime + "_.xlsx";
		response.setHeader(headerKey, headerValue);

		List<EstimacionPago> estimacionPagos = estimacionPagoRepository.findAll();
		List<EstimacionPagoDto> estimacionPagoDtos = new ArrayList<>();

		for (EstimacionPago estimacionPago : estimacionPagos) {
			EstimacionPagoDto estimacionPagoDto = new EstimacionPagoDto();
			Contrato objContrato = contratoRepository.findById(estimacionPago.getContrato()).get();
			estimacionPagoDto.setConcepto(estimacionPago.getConcepto());
			estimacionPagoDto.setFolio(objContrato.getFolio());

			dateFormatter = new SimpleDateFormat("dd/MM/yyyy");

			String fechaOPeracion = dateFormatter.format(estimacionPago.getFechaOperacion());

			estimacionPagoDto.setFechaOperacion(fechaOPeracion);
			estimacionPagoDto.setHipervinculo(estimacionPago.getHipervinculo());

			Locale locale = new Locale("en", "US");
			NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);

			DecimalFormat df = new DecimalFormat("#.##");
			df.setRoundingMode(RoundingMode.CEILING);

			estimacionPagoDto.setImporte(numberFormat.format(estimacionPago.getImporte().doubleValue()));
			estimacionPagoDto.setImporteAbono(numberFormat
					.format(estimacionPago.getImporteAbono() != null ? estimacionPago.getImporteAbono().doubleValue()
							: new Double(0)));
			estimacionPagoDto.setNumeroAbono(estimacionPago.getNumeroAbono());
			estimacionPagoDto.setObservaciones(estimacionPago.getObservaciones());
			estimacionPagoDto.setImporteBruto(numberFormat
					.format(estimacionPago.getImporteBruto() != null ? estimacionPago.getImporteBruto().doubleValue()
							: new Double(0)));
			estimacionPagoDto
					.setRetencionViciosOcultos(numberFormat.format(estimacionPago.getRetencionViciosOcultos() != null
							? estimacionPago.getRetencionViciosOcultos().doubleValue()
							: new Double(0)));
			estimacionPagoDto
					.setAmortizacionAnticipo(numberFormat.format(estimacionPago.getAmortizacionAnticipo() != null
							? estimacionPago.getAmortizacionAnticipo().doubleValue()
							: new Double(0)));
			estimacionPagoDto.setIva(numberFormat
					.format(estimacionPago.getIva() != null ? estimacionPago.getIva().doubleValue() : new Double(0)));
			estimacionPagoDto.setRetencionIva(numberFormat
					.format(estimacionPago.getRetencionIva() != null ? estimacionPago.getRetencionIva().doubleValue()
							: new Double(0)));
			estimacionPagoDto.setIsr(numberFormat
					.format(estimacionPago.getIsr() != null ? estimacionPago.getIsr().doubleValue() : new Double(0)));
			estimacionPagoDto.setDeducciones(numberFormat
					.format(estimacionPago.getDeducciones() != null ? estimacionPago.getDeducciones().doubleValue()
							: new Double(0)));

			estimacionPagoDtos.add(estimacionPagoDto);
		}
		ExcelEstimacionPagoGenerator estimacionPagoGenerator = new ExcelEstimacionPagoGenerator(estimacionPagoDtos);
		estimacionPagoGenerator.generateExcelFile(response);
	}

	@Override
	@Transactional
	public String uploadFile(MultipartFile fileStorageProperties) throws ParseException {
		String fileName = StringUtils.cleanPath(fileStorageProperties.getOriginalFilename());
		try {
			if (fileName.contains("..")) {
				throw new RuntimeException(
						"Lo siento. El nombre del archivo contiene una secuencia de ruta no válida." + fileName);
			}
			// Copy file to the target path (replacing existing file with the same name)
			Path targetLocation = this.fileUploadLocation.resolve(fileName);
			Files.copy(fileStorageProperties.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

			BufferedReader lector = new BufferedReader(new FileReader(targetLocation.toString()));
			String line = null;

			estimacionPagoRepository.deleteAll();

			boolean esPrimeraLinea = true;
			int count = 0;
			while ((line = lector.readLine()) != null) {
				if (esPrimeraLinea) {
					esPrimeraLinea = false;
					continue;
				}
				String[] parts = line.split(";");
				for (int i = 0; i < parts.length; i++) {
					parts[i] = parts[i].replace("-", "");
				}
				List<Contrato> contratos = new ArrayList<>();
				count++;
				try {
					System.out.println("FOLIO: " + parts[0]);
					contratos = contratoRepository.findByFolio(parts[0]);
				} catch (Exception e) {
					fileName = "El archivo no pudo ser procesado. Error en la línea " + count;
					lector.close();
					return fileName;
				}

				DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
				Date fechaOperacion = null;

				if (!parts[4].isEmpty()) {
					try {
						fechaOperacion = originalFormat.parse(parts[4]);
					} catch (ParseException e) {
						fileName = "El archivo no pudo ser procesado. Error en la línea " + count;
						e.printStackTrace();
					}
				}

				BigDecimal importeNeto = new BigDecimal(0);
				importeNeto = importeNeto.add(new BigDecimal(parts[7]));
				importeNeto = importeNeto.subtract(new BigDecimal(parts[8]));
				importeNeto = importeNeto.subtract(new BigDecimal(parts[9]));
				importeNeto = importeNeto.add(new BigDecimal(parts[10]));
				importeNeto = importeNeto.subtract(new BigDecimal(parts[11]));
				importeNeto = importeNeto.subtract(new BigDecimal(parts[12]));
				importeNeto = importeNeto.subtract(new BigDecimal(parts[13]));

				EstimacionPago estimacionPago = new EstimacionPago();
				estimacionPago.setConcepto(parts[1]);
				estimacionPago.setNumeroAbono(parts[2]);
				estimacionPago.setImporte(importeNeto);
				estimacionPago.setFechaOperacion(fechaOperacion);
				estimacionPago.setObservaciones(parts[5]);
				estimacionPago.setHipervinculo(parts[6]);
				estimacionPago.setImporteBruto(!parts[7].contains("-") ? new BigDecimal(parts[7]) : new BigDecimal(0));
				estimacionPago.setRetencionViciosOcultos(
						!parts[8].contains("-") ? new BigDecimal(parts[8]) : new BigDecimal(0));
				estimacionPago.setAmortizacionAnticipo(
						!parts[9].contains("-") ? new BigDecimal(parts[9]) : new BigDecimal(0));
				estimacionPago.setIva(!parts[10].contains("-") ? new BigDecimal(parts[10]) : new BigDecimal(0));
				estimacionPago
						.setRetencionIva(!parts[11].contains("-") ? new BigDecimal(parts[11]) : new BigDecimal(0));
				estimacionPago.setIsr(!parts[12].contains("-") ? new BigDecimal(parts[12]) : new BigDecimal(0));
				estimacionPago.setDeducciones(!parts[13].contains("-") ? new BigDecimal(parts[13]) : new BigDecimal(0));

				if (contratos.size() > 0) {
					estimacionPago.setContrato(contratos.get(0).getId());
					EstimacionPagoUtil.calcularEstimacionPago(estimacionPago, estimacionPagoRepository,
							contratoRepository);
					estimacionPagoRepository.save(estimacionPago);
				} else {
					fileName = "El archivo no pudo ser procesado. Error en la línea " + count;
				}

			}
			cerrarConexionService.cerrarConexion();
			return fileName;
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}
}
