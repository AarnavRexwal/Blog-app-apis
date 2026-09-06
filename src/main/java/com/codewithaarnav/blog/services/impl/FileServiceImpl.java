package com.codewithaarnav.blog.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.codewithaarnav.blog.services.FileService;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public String uploadImage(String path, MultipartFile file) {

        String originalFileName = file.getOriginalFilename();

        String randomId = UUID.randomUUID().toString();

        String fileName = randomId + "_" + originalFileName;

        try {

            // Create the folder if it does not exist
            File folder = new File(path);

            if (!folder.exists()) {
                folder.mkdirs();
            }

            // Create complete file path
            Path filePath = Paths.get(path, fileName);

            // Save the file
            Files.copy(
                    file.getInputStream(),
                    filePath
            );

            System.out.println("Image saved at: "
                    + filePath.toAbsolutePath());

            return fileName;

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException(
                    "Could not upload file: " + e.getMessage()
            );
        }
    }


    @Override
    public boolean deleteImage(String path, String fileName) {

        try {

            Path filePath =
                    Paths.get(path, fileName);

            return Files.deleteIfExists(filePath);

        } catch (Exception e) {

            return false;
        }
    }


    @Override
    public InputStream getResource(
            String path,
            String fileName) {

        try {

            Path filePath =
                    Paths.get(path, fileName);

            return new FileInputStream(
                    filePath.toFile()
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "File not found"
            );
        }
    }
}