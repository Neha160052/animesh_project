package com.ttn.e_commerce_project.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Locale;
import java.util.Set;

import static com.ttn.e_commerce_project.constants.UserConstants.ALLOWED_EXTENSIONS;
import static com.ttn.e_commerce_project.constants.UserConstants.BASE_PATH;

@Slf4j
@Component
public class ImageStorageUtil {


    public String saveImage(String userType, Long userId, MultipartFile file) throws IOException {
        // Create the subfolder for the userType (customers or sellers)
        Path folder = Paths.get(BASE_PATH, userType).toAbsolutePath().normalize();
        if (!Files.exists(folder)) {
            Files.createDirectories(folder); // make directory if missing
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new IOException("File must have an extension");
        }

        String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1)
                .toLowerCase(Locale.ROOT);

        // Validate allowed extensions
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IOException("Unsupported file type: " + extension);
        }

        // Final path format: uploads/{userType}/{userId}.{ext}
        Path filePath = folder.resolve(userId + "." + extension);

        // Save file bytes (overwrite if exists)
        Files.write(filePath, file.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        log.info("Image saved at {}", filePath);
        return filePath.toString();
    }
    public byte[] loadImage(String userType, Long userId) throws IOException {
        Path folder = Paths.get(BASE_PATH, userType).toAbsolutePath().normalize();
        log.info(folder.toString());

        // Check each allowed extension to see if file exists
        for (String ext : ALLOWED_EXTENSIONS) {
            Path filePath = folder.resolve(userId + "." + ext);
            log.info(filePath.toString());
            if (Files.exists(filePath)) {
                log.info("Loading image: {}", filePath);
                return Files.readAllBytes(filePath);
            }
        }

        throw new FileNotFoundException("No image found for " + userType + " with id " + userId);
    }

    public String buildProfileImageUrl(String userType, Long id) {
        return "/" + userType + "/" + id + "/get-profile-image";
    }

    public String buildSecondaryImageName(Long variationId, int index) {
        if (index < 0 || index >= ALPHABETS.length) {
            throw new IllegalArgumentException("Index out of range (supports up to 26 images per variation)");
        }
        return variationId + "(" + ALPHABETS[index] + ")";
    }
}
