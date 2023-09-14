package com.zwash.auth.utility;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtil {

    public static String uploadFile(MultipartFile file, String uploadDir) throws Exception {
        String fileName = file.getOriginalFilename();
        Path filePath = Path.of(uploadDir, fileName);

        // Copy the file to the upload directory
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return filePath.toString();
    }
}
