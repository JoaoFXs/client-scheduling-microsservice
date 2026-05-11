package br.com.joaofxs.client_scheduling_microsservice.app.enterprise.service.impl;

import br.com.joaofxs.client_scheduling_microsservice.app.enterprise.service.FileService;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class FileServiceImpl implements FileService {
    // Retorna a URL pública (assinada ou direta dependendo das regras do bucket)
    // Gera uma URL assinada que expira em 10 anos (ou o tempo que você desejar)
    // Isso já resolve o problema do Token e das permissões de leitura

    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString().concat(this.getExtension(file.getOriginalFilename()));
        Bucket bucket = StorageClient.getInstance().bucket();
        // Faz o upload do arquivo
        Blob blob = bucket.create(fileName, file.getInputStream(), file.getContentType());
        // Retorna a URL pública (assinada ou direta dependendo das regras do bucket)
        // Gera uma URL assinada que expira em 10 anos (ou o tempo que você desejar)
        // Isso já resolve o problema do Token e das permissões de leitura
        return blob.signUrl(3650, TimeUnit.DAYS).toString();
    }

    private String getExtension(String fileName) {
        if (fileName == null) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
