package com.jobportal.util;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileStorageUtil {

    public static String saveFile(MultipartFile file, String uploadDir) {

        if (file.isEmpty()) {
            throw new RuntimeException("Cannot upload empty file.");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new RuntimeException("Invalid file name.");
        }

        String lowerCaseName = originalFilename.toLowerCase();
        if (!lowerCaseName.endsWith(".pdf") &&
            !lowerCaseName.endsWith(".doc") &&
            !lowerCaseName.endsWith(".docx")) {
            throw new RuntimeException("Only PDF, DOC, DOCX allowed.");
        }

        String uniqueFileName = System.currentTimeMillis() + "_" + originalFilename;
        Path uploadPath = Paths.get(uploadDir);

        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(uniqueFileName);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return uploadDir + "/" + uniqueFileName;

        } catch (IOException e) {
            throw new RuntimeException("File upload failed: " + e.getMessage(), e);
        }
    }
}