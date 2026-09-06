package com.codewithaarnav.blog.services;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    String uploadImage(String path, MultipartFile file);

    boolean deleteImage(String path, String fileName);

    java.io.InputStream getResource(String path, String fileName);
}