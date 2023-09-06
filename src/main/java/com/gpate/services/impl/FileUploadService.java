package com.gpate.services.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.gpate.dtos.FileStorageProperties;
import com.gpate.services.IFileUploadService;

@Service
public class FileUploadService implements IFileUploadService {

	private final Path fileUploadLocation;

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
	public String uploadFile(MultipartFile file) {
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
			StringBuilder cadena = new StringBuilder();
			String line = null;

			while ((line = lector.readLine()) != null) {
				cadena.append(line);
				String[] parts = line.split(";");
				for (String string : parts) {
					System.out.println(string);
				}
			}
			lector.close();
			String contenido = cadena.toString();
			System.out.println(contenido);

			return fileName;
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}

	}

}
