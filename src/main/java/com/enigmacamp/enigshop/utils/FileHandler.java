package com.enigmacamp.enigshop.utils;

import com.enigmacamp.enigshop.exceptions.RequestValidationException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileHandler {
    public static void createDirectoryIfNotExist(Path path) {
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                throw new RequestValidationException("Could not create upload directory!", e);
            }
        }
    }
}
