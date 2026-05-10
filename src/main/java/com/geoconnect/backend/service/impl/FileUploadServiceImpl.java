package com.geoconnect.backend.service.impl;

import com.geoconnect.backend.exception.BadRequestException;
import com.geoconnect.backend.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Override
    public String uploadProfilePhoto(MultipartFile file, String username) {

        // Validate file
        if (file.isEmpty()) {
            throw new BadRequestException("Please select a file!");
        }

        // Validate file type
        String contentType = file.getContentType();
        if (contentType == null ||
            !contentType.startsWith("image/")) {
            throw new BadRequestException(
                "Only image files are allowed!"
            );
        }

        // Validate file size (max 5MB)
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new BadRequestException(
                "File size must be less than 5MB!"
            );
        }

        try {
            // Create upload directory if not exists
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generate unique filename
            String extension = getFileExtension(
                file.getOriginalFilename()
            );
            String fileName = username + "_" +
                UUID.randomUUID() + "." + extension;

            // Save file
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath,
                StandardCopyOption.REPLACE_EXISTING);

            return fileName;

        } catch (IOException e) {
            throw new BadRequestException(
                "Failed to upload file: " + e.getMessage()
            );
        }
    }

    @Override
    public void deleteFile(String fileName) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(fileName);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            System.out.println("Failed to delete file: " + fileName);
        }
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "jpg";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1)
            .toLowerCase();
    }
}