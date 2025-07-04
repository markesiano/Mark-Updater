package com.backend.versions.infrastructure;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Service;

import com.backend.versions.application.ManifestService;
import com.backend.versions.application.entities.Manifest;
import com.backend.versions.exceptions.ManifestNotFoundException;
import com.backend.versions.exceptions.ManifestParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
@Service
public class FileSystemManifestService implements ManifestService{

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Manifest getManifest(String path) {
        File manifestFile = new File(path);

        if (!manifestFile.exists()) {
            throw new ManifestNotFoundException("El archivo manifest.json no fue encontrado.");
        }

        try {
            return objectMapper.readValue(manifestFile, Manifest.class);
        } catch (IOException e) {
            throw new ManifestParseException("Error al procesar manifest.json", e);
        }
    }

}
