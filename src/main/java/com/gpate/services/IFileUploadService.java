package com.gpate.services;

import org.springframework.web.multipart.MultipartFile;

public interface IFileUploadService {
	
	String uploadFile(MultipartFile  fileStorageProperties);

}
