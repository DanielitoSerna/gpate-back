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
		String headerValue = "attachment; filename=estado_cuenta_" + currentDateTime + "_.xlsx";
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
			estimacionPagoDto.setImporteAbono(estimacionPago.getImporteAbono());
			estimacionPagoDto.setNumeroAbono(estimacionPago.getNumeroAbono());
			estimacionPagoDto.setObservaciones(estimacionPago.getObservaciones());

			estimacionPagoDtos.add(estimacionPagoDto);
		}
		ExcelEstimacionPagoGenerator estimacionPagoGenerator = new ExcelEstimacionPagoGenerator(estimacionPagoDtos);
		estimacionPagoGenerator.generateExcelFile(response);
	}

	@Override
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
				List<EstimacionPago> estimacionPagos = new ArrayList<>();
				try {
					if (contratos.size() > 0) {
						estimacionPagos = estimacionPagoRepository.findByContrato(contratos.get(0).getId());
					}
				} catch (Exception e) {
					fileName = "El archivo no pudo ser procesado. Error en la línea " + count;
					lector.close();
					return fileName;
				}

				DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
				Date fechaOperacion = null;

				if (!parts[5].isEmpty()) {
					try {
						fechaOperacion = originalFormat.parse(parts[5]);
					} catch (ParseException e) {
						fileName = "El archivo no pudo ser procesado. Error en la línea " + count;
						e.printStackTrace();
					}
				}
				// EDITAR
				if (estimacionPagos.size() > 0) {
					for (EstimacionPago estimacionPago : estimacionPagos) {
						if (estimacionPago.getConcepto().equals(parts[1])
								&& estimacionPago.getNumeroAbono().equals(parts[2])) {
							estimacionPago.setFechaOperacion(fechaOperacion);
							estimacionPago.setHipervinculo(parts[7]);
							estimacionPago
									.setImporte(!parts[3].isEmpty() ? new BigDecimal(parts[3]) : new BigDecimal(0));
							estimacionPago.setImporteAbono(
									!parts[4].isEmpty() ? new BigDecimal(parts[4]) : new BigDecimal(0));
							estimacionPago.setObservaciones(parts[6]);

							EstimacionPagoUtil.calcularEstimacionPago(estimacionPago, estimacionPagoRepository,
									contratoRepository);
							
							estimacionPagoRepository.save(estimacionPago);
						}
					}
				} else {
					EstimacionPago estimacionPago = new EstimacionPago();
					estimacionPago.setConcepto(parts[1]);
					estimacionPago.setNumeroAbono(parts[2]);
					estimacionPago.setImporte(!parts[3].isEmpty() ? new BigDecimal(parts[3]) : new BigDecimal(0));
					estimacionPago.setImporteAbono(!parts[4].isEmpty() ? new BigDecimal(parts[4]) : new BigDecimal(0));
					estimacionPago.setFechaOperacion(fechaOperacion);
					estimacionPago.setObservaciones(parts[6]);
					estimacionPago.setHipervinculo(parts[7]);
					if (contratos.size() > 0) {
						estimacionPago.setContrato(contratos.get(0).getId());
						estimacionPagoRepository.save(estimacionPago);
					} else {
						fileName = "El archivo no pudo ser procesado. Error en la línea " + count;
					}
				}
			}
			return fileName;
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}
}
