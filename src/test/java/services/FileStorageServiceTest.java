package com.example.artspace.services;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileStorageServiceTest {

    private final FileStorageService fileStorageService =
            new FileStorageService();

    private byte[] createImage(String format) throws Exception {
        BufferedImage image =
                new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        boolean written = ImageIO.write(image, format, output);
        assertTrue(written, "Could not generate test image");

        return output.toByteArray();
    }

    @Test
    void saveFile_shouldSaveValidJpgImage() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "painting.jpg", "image/jpeg", createImage("jpg")
        );

        String imagePath = fileStorageService.saveFile(file);

        try {
            assertTrue(imagePath.startsWith("/uploads/"));
            assertTrue(imagePath.endsWith(".jpg"));
            assertTrue(fileStorageService.isValidImagePath(imagePath));
        } finally {
            Files.deleteIfExists(Path.of("." + imagePath));
        }
    }

    @Test
    void saveFile_shouldSaveValidPngImage() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "artwork.png", "image/png", createImage("png")
        );

        String imagePath = fileStorageService.saveFile(file);

        try {
            assertTrue(imagePath.startsWith("/uploads/"));
            assertTrue(imagePath.endsWith(".png"));
            assertTrue(fileStorageService.isValidImagePath(imagePath));
        } finally {
            Files.deleteIfExists(Path.of("." + imagePath));
        }
    }

    @Test
    void saveFile_shouldRejectEmptyFile() {
        MockMultipartFile file = new MockMultipartFile(
                "file", "empty.png", "image/png", new byte[0]
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> fileStorageService.saveFile(file)
        );

        assertEquals("File cannot be empty!", exception.getMessage());
    }

    @Test
    void saveFile_shouldRejectFileLargerThan5MB() {
        byte[] largeFile = new byte[5 * 1024 * 1024 + 1];

        MockMultipartFile file = new MockMultipartFile(
                "file", "large.png", "image/png", largeFile
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> fileStorageService.saveFile(file)
        );

        assertEquals(
                "File size cannot exceed 5 MB!",
                exception.getMessage()
        );
    }

    @Test
    void saveFile_shouldRejectUnsupportedFileType() {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "document.txt",
                "text/plain",
                "Hello ArtSpace".getBytes(StandardCharsets.UTF_8)
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> fileStorageService.saveFile(file)
        );

        assertEquals(
                "Only JPG and PNG images are allowed!",
                exception.getMessage()
        );
    }

    @Test
    void saveFile_shouldRejectFakeImage() {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "fake.png",
                "image/png",
                "This is not a real image".getBytes(StandardCharsets.UTF_8)
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> fileStorageService.saveFile(file)
        );

        assertTrue(exception.getMessage().contains("Invalid image file"));
    }

    @Test
    void isValidImagePath_shouldRejectNull() {
        assertFalse(fileStorageService.isValidImagePath(null));
    }

    @Test
    void isValidImagePath_shouldRejectInvalidPath() {
        assertFalse(fileStorageService.isValidImagePath(
                "/images/painting.jpg"
        ));
    }

    @Test
    void isValidImagePath_shouldRejectNonexistentFile() {
        String imagePath =
                "/uploads/123e4567-e89b-12d3-a456-426614174000.png";

        assertFalse(fileStorageService.isValidImagePath(imagePath));
    }
}
