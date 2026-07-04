package com.bhawanisingh.airesume.common.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Component
public class FileUtil {

    private static final List<String> ALLOWED_EXTENSIONS =
            List.of("pdf", "doc", "docx");

    public String validateAndGetExtension(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Resume file is required");
        }

        String originalFileName = file.getOriginalFilename();

        if (originalFileName == null || originalFileName.isBlank()) {
            throw new IllegalArgumentException("Invalid file name");
        }

        int lastDotIndex = originalFileName.lastIndexOf(".");
        if (lastDotIndex == -1 || lastDotIndex == originalFileName.length() - 1) {
            throw new IllegalArgumentException("File extension is missing");
        }

        String extension = originalFileName.substring(lastDotIndex + 1).toLowerCase();

        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException(
                    "Only PDF, DOC and DOCX files are allowed"
            );
        }

        return extension;
    }

    public String generateStoredFileName(String extension) {
        return UUID.randomUUID() + "." + extension;
    }

    public String saveFile(MultipartFile file, String uploadDir, String storedFileName) {

        try {
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path targetPath = uploadPath.resolve(storedFileName);

            Files.copy(
                    file.getInputStream(),
                    targetPath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            return targetPath.toString();
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }
}