package com.gpate.rest;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.gpate.services.impl.FileUploadService;

@RestController
@RequestMapping("api")
@CrossOrigin("*")
public class UploadFileRest {

	@Autowired
	private FileUploadService fileUploadService;

	@PostMapping("/cargarContrato")
	public String uploadFile(@RequestParam("file") MultipartFile file) throws ParseException {
		String fileName = fileUploadService.uploadFile(file);
		ServletUriComponentsBuilder.fromCurrentContextPath().path(fileName).toUriString();
		return "Upload Successfully";
	}
}
