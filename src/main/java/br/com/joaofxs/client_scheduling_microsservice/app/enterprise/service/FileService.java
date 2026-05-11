package br.com.joaofxs.client_scheduling_microsservice.app.enterprise.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    String uploadFile(MultipartFile file) throws IOException;
}
