package com.backend.versions.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.versions.application.ManifestService;
import com.backend.versions.application.entities.Manifest;
import com.backend.versions.application.usecases.GenerateUpdateZipUseCase;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
@RequestMapping("/api/update")
public class UpdateController {
    @Value("${app.directory.base}")
    private String BASE_DIR;
    @Value("${app.directory.update.manifest}")
    private String UPDATE_MANIFEST_DIR;


    


    private final ManifestService manifestService;
    private final GenerateUpdateZipUseCase generateUpdateZipUseCase;
    
    public UpdateController(ManifestService manifestService, GenerateUpdateZipUseCase generateUpdateZipUseCase) {
        this.manifestService = manifestService;
        this.generateUpdateZipUseCase = generateUpdateZipUseCase;
    }

    @GetMapping("/manifest")
    public ResponseEntity<Manifest> getManifest() {
        String MANIFEST_FILE = BASE_DIR + UPDATE_MANIFEST_DIR;
        return ResponseEntity.ok(manifestService.getManifest(MANIFEST_FILE));
    }

    @PostMapping("/download")
    public ResponseEntity<InputStreamResource> downloadSelectedFiles(@RequestBody List<String> files) throws Exception {
        // Validación de entrada
        if (files == null || files.isEmpty()) {
            throw new IllegalArgumentException("La lista de archivos no puede estar vacía");
        }
        
        // Validar que no haya path traversal attacks
        for (String file : files) {
            if (file.contains("..") || file.contains("/") || file.contains("\\")) {
                throw new IllegalArgumentException("Nombres de archivo no válidos detectados");
            }
        }
        
        File zipFile = generateUpdateZipUseCase.execute(files);
        InputStreamResource resource = new InputStreamResource(Files.newInputStream(zipFile.toPath()));
        return ResponseEntity.ok()
                .contentLength(zipFile.length())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=update.zip")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
