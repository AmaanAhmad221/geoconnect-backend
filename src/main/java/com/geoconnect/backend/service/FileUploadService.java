package com.geoconnect.backend.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {

	String uploadProfilePhoto(MultipartFile file, String username);

	void deleteFile(String fileName);
}
