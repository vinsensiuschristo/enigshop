package com.enigmacamp.enigshop.services.Impl;

import com.enigmacamp.enigshop.exceptions.RequestValidationException;
import com.enigmacamp.enigshop.exceptions.ResourceNotFoundException;
import com.enigmacamp.enigshop.models.entities.Image;
import com.enigmacamp.enigshop.repositories.ImageRepository;
import com.enigmacamp.enigshop.services.ImageService;
import com.enigmacamp.enigshop.utils.FileHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;

    @Value("${app.enigshop.upload.path}")
    private Path uploadDir;

    @Override
    public Image saveImage(MultipartFile multipartFile, String firstNameFile) {
        List<String> allowedContentTypes = List.of("image/jpeg", "image/png", "image/gif", "image/jpg");
        if (!allowedContentTypes.contains(multipartFile.getContentType())) {
            throw new RequestValidationException("Invalid file type");
        }

        String uniqueFileName = firstNameFile + System.currentTimeMillis() + "_" +
                StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename())
                        .toLowerCase()).replaceAll("\\s+", "_");

        FileHandler.createDirectoryIfNotExist(uploadDir); // Buat directory jika belum ada

        try {
            Path filePath = uploadDir.resolve(uniqueFileName);
            Files.copy(multipartFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RequestValidationException("Could not save the file!", e);
        }

        Image image = Image.builder()
                .name(uniqueFileName)
                .path(uniqueFileName)
                .contentType(multipartFile.getContentType())
                .size(multipartFile.getSize())
                .build();

        return imageRepository.save(image);
    }

    @Override
    public void deleteImage(String id) {
        Image image = getImageById(id);
        imageRepository.delete(image);
    }

    @Override
    public Image getImageById(String id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found"));
    }

    @Override
    public Path getImageFile(String id) {
        Image image = getImageById(id);
        return uploadDir.resolve(image.getName());
    }

}
