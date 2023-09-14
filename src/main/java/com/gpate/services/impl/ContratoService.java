package com.gpate.services.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.gpate.dtos.FileStorageProperties;
import com.gpate.model.Contrato;
import com.gpate.model.EstimacionPago;
import com.gpate.repository.ContratoRepository;
import com.gpate.repository.EstimacionPagoRepository;
import com.gpate.services.IContratoService;
import com.gpate.util.ContratoUtil;
import com.gpate.util.ExcelGenerator;
import com.gpate.util.PDFGenerator;
import com.lowagie.text.DocumentException;

@Service
public class ContratoService implements IContratoService {
	
	private final Path fileUploadLocation;

	@Autowired
	private ContratoRepositoryCustom contratoRepositoryCustom;
	
	@Autowired
	private ContratoRepository contratoRepository;
	
	@Autowired
	private EstimacionPagoRepository estimacionPagoRepository;
	
	@Autowired
	public ContratoService(FileStorageProperties fileStorageProperties) {
		this.fileUploadLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();

		try {
			Files.createDirectories(this.fileUploadLocation);
		} catch (Exception e) {
			throw new RuntimeException("No se puede crear el directorio donde desea cargar el archivo.", e);
		}
	}

	@Override
	public void generatePdf(HttpServletResponse response, String proyecto, String folio, String especialidad,
			String proveedor, String estado) throws DocumentException, IOException {
		response.setContentType("application/pdf");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormat.format(new Date());
		String headerkey = "Content-Disposition";
		String headervalue = "attachment; filename=contratos_" + currentDateTime + "_.pdf";
		response.setHeader(headerkey, headervalue);
		List<Contrato> contratos = contratoRepositoryCustom.getContractsByFilters(proyecto, folio, especialidad,
				proveedor, estado);
		
		PDFGenerator generator = new PDFGenerator();
		generator.setContratos(contratos);
		generator.generate(response);
	}

	@Override
	public void generateExcel(HttpServletResponse response, String proyecto, String folio, String especialidad,
			String proveedor, String estado) throws IOException {
		response.setContentType("application/octet-stream");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
		
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=contratos_" + currentDateTime + "_.xlsx";
		response.setHeader(headerKey, headerValue);
		
		List<Contrato> contratos = contratoRepositoryCustom.getContractsByFilters(proyecto, folio, especialidad,
				proveedor, estado);
		ExcelGenerator generator = new ExcelGenerator(contratos);
		generator.generateExcelFile(response);
		
	}

	@Override
	public Contrato getInfoContrato(Long idContrato) {
		Contrato contrato = contratoRepository.findById(idContrato).get();
		
		return contrato; 
	}

	@Override
	public String eliminarContrato(Long idContrato) {
		String mensaje = "";
		List<EstimacionPago> estimacionPagos = estimacionPagoRepository.findByContrato(idContrato);
		for (EstimacionPago estimacionPago : estimacionPagos) {
			estimacionPagoRepository.delete(estimacionPago);
		}
		try {
			Contrato contrato = contratoRepository.findById(idContrato).get();
			if (contrato != null) {
				contratoRepository.delete(contrato);
				mensaje = "Contrato eliminado correctamente";
			}
		} catch (Exception e) {
			mensaje = "Error al eliminar contrato";
		}
		
		return mensaje;
	}

	@Override
	public String uploadFile(MultipartFile file) throws ParseException {
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());

		try {
			// Verify if the file's name is containing invalid characters
			if (fileName.contains("..")) {
				throw new RuntimeException(
						"Lo siento. El nombre del archivo contiene una secuencia de ruta no válida." + fileName);
			}
			// Copy file to the target path (replacing existing file with the same name)
			Path targetLocation = this.fileUploadLocation.resolve(fileName);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

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
				List<Contrato> contratos = new ArrayList<>();
				count++;
				try {
					contratos = contratoRepository.findByFolio(parts[1]);
				} catch (Exception e) {
					fileName = "El archivo no pudo ser procesado. Error en la línea "+ count;
					lector.close();
					return fileName;
				}
				
				Contrato contrato = new Contrato();

				DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");

				Date fechaInicioContrato = null;
				Date fechaVencimiento = null;
				Date fechaFallo = null;
				Date fechaSolicitud = null;
				Date fechaProgramada = null;
				Date fechaJuridico = null;

				if (!parts[7].isEmpty()) {
					fechaInicioContrato = originalFormat.parse(parts[7]);
				}
				if (!parts[8].isEmpty()) {
					fechaVencimiento = originalFormat.parse(parts[8]);
				}
				if (!parts[9].isEmpty()) {
					fechaFallo = originalFormat.parse(parts[9]);
				}
				if (!parts[10].isEmpty()) {
					fechaSolicitud = originalFormat.parse(parts[10]);
				}
				if (!parts[11].isEmpty()) {
					fechaProgramada = originalFormat.parse(parts[11]);
				}
				if (!parts[12].isEmpty()) {
					fechaJuridico = originalFormat.parse(parts[12]);
				}

				if (contratos.size() > 0) {
					contrato.setId(contratos.get(0).getId());
					contrato.setProyecto(parts[0]);
					contrato.setFolio(parts[1]);
					contrato.setEspecialidad(parts[2]);
					contrato.setProveedor(parts[3]);
					contrato.setCentroCosto(parts[4]);
					contrato.setImporteContratado(new BigDecimal(parts[5]));
					contrato.setAnticipoContratado(new BigDecimal(parts[6]));
					contrato.setEstado(contratos.get(0).getEstado());
					contrato.setFechaFirmadoCliente(fechaInicioContrato);
					contrato.setFechaFallo(fechaFallo);
					contrato.setFechaVencimientoContrato(fechaVencimiento);
					contrato.setFechaSolicitudContrato(fechaSolicitud);
					contrato.setFechaJuridico(fechaJuridico);
					contrato.setFechaProgramadaEntrega(fechaProgramada);
					contrato.setHipervinculo(parts[13]);
					contrato.setObservaciones(parts[14]);
					contrato.setTieneImporte(!parts[15].isEmpty() ? Boolean.parseBoolean(parts[15]) : false);

					ContratoUtil.obtenerDatosContrato(contrato, estimacionPagoRepository);

					contratoRepository.save(contrato);
				} else {
					contrato.setProyecto(parts[0]);
					contrato.setFolio(parts[1]);
					contrato.setEspecialidad(parts[2]);
					contrato.setProveedor(parts[3]);
					contrato.setCentroCosto(parts[4]);
					contrato.setImporteContratado(new BigDecimal(parts[5]));
					contrato.setAnticipoContratado(new BigDecimal(parts[6]));
					contrato.setEstado("Activo");
					contrato.setFechaFirmadoCliente(fechaInicioContrato);
					contrato.setFechaFallo(fechaFallo);
					contrato.setFechaVencimientoContrato(fechaVencimiento);
					contrato.setFechaSolicitudContrato(fechaSolicitud);
					contrato.setFechaJuridico(fechaJuridico);
					contrato.setFechaProgramadaEntrega(fechaProgramada);
					contrato.setHipervinculo(parts[13]);
					contrato.setObservaciones(parts[14]);
					contrato.setTieneImporte(!parts[15].isEmpty() ? Boolean.parseBoolean(parts[15]) : false);

					ContratoUtil.obtenerDatosContrato(contrato, estimacionPagoRepository);

					contratoRepository.save(contrato);
				}

			}
			count = 0;
			lector.close();

			return fileName;
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}

	}

}
