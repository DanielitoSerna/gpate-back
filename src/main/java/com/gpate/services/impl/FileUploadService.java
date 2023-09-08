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
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.gpate.dtos.FileStorageProperties;
import com.gpate.model.Contrato;
import com.gpate.repository.ContratoRepository;
import com.gpate.services.IFileUploadService;
import com.gpate.util.ContratoUtil;

@Service
public class FileUploadService implements IFileUploadService {

	private final Path fileUploadLocation;

	@Autowired
	private ContratoRepository contratoRepository;

	@Autowired
	public FileUploadService(FileStorageProperties fileStorageProperties) {
		this.fileUploadLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();

		try {
			Files.createDirectories(this.fileUploadLocation);
		} catch (Exception e) {
			throw new RuntimeException("No se puede crear el directorio donde desea cargar el archivo.", e);
		}
	}

	@Override
	public String uploadFile(MultipartFile file) throws ParseException {
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());

		try {
			// Verify if the file's name is containing invalid characters
			if (fileName.contains("..")) {
				throw new RuntimeException(
						"Lo siento. El nombre del archivo contiene una secuencia de ruta no válida " + fileName);
			}
			// Copy file to the target path (replacing existing file with the same name)
			Path targetLocation = this.fileUploadLocation.resolve(fileName);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

			BufferedReader lector = new BufferedReader(new FileReader(targetLocation.toString()));
			String line = null;

			boolean esPrimeraLinea = true;
			while ((line = lector.readLine()) != null) {
				if (esPrimeraLinea) {
					esPrimeraLinea = false;
					continue;
				}
				String[] parts = line.split(";");
				List<Contrato> contratos = contratoRepository.findByFolio(parts[1]);
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
					contrato.setTieneImporte(Boolean.parseBoolean(parts[15]));

					ContratoUtil.obtenerDatosContrato(contrato);

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

					ContratoUtil.obtenerDatosContrato(contrato);

					contratoRepository.save(contrato);
				}

			}
			lector.close();

			return fileName;
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}

	}

}
