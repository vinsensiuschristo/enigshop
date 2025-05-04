package com.enigmacamp.enigshop.services;

import com.enigmacamp.enigshop.models.entities.Image;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface ImageService {
    Image saveImage(MultipartFile multipartFile, String firstNameFile);
    void deleteImage(String id);
    Image getImageById(String id);
    Path getImageFile(String id);

}
