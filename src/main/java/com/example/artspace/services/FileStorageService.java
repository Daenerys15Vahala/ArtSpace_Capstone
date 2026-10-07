package com.example.artspace.services;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {
    private final Path uploadDirectory = Paths.get("uploads");

    public String saveFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File can not be empty!");
        }
        if (!Files.exists(uploadDirectory)) {
            Files.createDirectories(uploadDirectory);
        }
        String originalFileName = file.getOriginalFilename();
        String fileName = UUID.randomUUID() + "-" + originalFileName;
        Path filePath = uploadDirectory.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);
        return filePath.toString();
    }
}
