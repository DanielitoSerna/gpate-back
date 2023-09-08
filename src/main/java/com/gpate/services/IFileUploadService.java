package com.gpate.services;

import java.text.ParseException;

import org.springframework.web.multipart.MultipartFile;

public interface IFileUploadService {
	
	String uploadFile(MultipartFile  fileStorageProperties) throws ParseException;

}
