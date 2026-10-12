package com.example.artspace.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.UUID;

@Service
public class FileStorageService {
    private final Path uploadDirectory = Paths.get("uploads");
    public String saveFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty!");}
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("File size cannot exceed 5 MB!");}
        String contentType = file.getContentType();
        if (!"image/jpeg".equals(contentType) && !"image/png".equals(contentType)) {
            throw new IllegalArgumentException("Only JPG and PNG images are allowed!");}
        BufferedImage image;
        try (var inputStream = file.getInputStream()) {
            image = ImageIO.read(inputStream);}
        if (image == null) {throw new IllegalArgumentException(
                    "Invalid image file. Please upload a real JPG or PNG image.");}
        Files.createDirectories(uploadDirectory);
        String extension = "image/jpeg".equals(contentType) ? ".jpg" : ".png";
        String fileName = UUID.randomUUID() + extension;
        Path filePath = uploadDirectory.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);
        return "/uploads/" + fileName;
    }

    public boolean isValidImagePath(String imagePath) {
        if (imagePath == null || !imagePath.matches("^/uploads/[a-fA-F0-9-]{36}\\.(jpg|png)$")) {
            return false;}
        String fileName = imagePath.substring("/uploads/".length());
        Path filePath = uploadDirectory.resolve(fileName);
        return Files.isRegularFile(filePath);
    }
}