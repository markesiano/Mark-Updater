package com.backend.versions.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtils {
    private static final long MAX_FILE_SIZE = 100 * 1024 * 1024; // 100MB por archivo
    private static final int MAX_FILES = 1000; // Máximo número de archivos
    
    public static File createZip(List<String> filePaths, String baseDir, String outputFileName) throws IOException {
        if (filePaths.size() > MAX_FILES) {
            throw new IllegalArgumentException("Demasiados archivos en el zip");
        }
        
        File zipFile = File.createTempFile(outputFileName, ".zip");
        Path baseDirPath = Paths.get(baseDir).normalize();
        
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile))) {
            for (String relativePath : filePaths) {
                // Validar path traversal
                if (relativePath.contains("..")) {
                    continue; // Saltar archivos con path traversal
                }
                
                File file = new File(baseDir, relativePath);
                Path filePath = file.toPath().normalize();
                
                // Verificar que el archivo esté dentro del directorio base
                if (!filePath.startsWith(baseDirPath)) {
                    continue; // Saltar archivos fuera del directorio base
                }
                
                if (!file.exists() || !file.isFile()) continue;
                
                // Verificar tamaño del archivo
                if (file.length() > MAX_FILE_SIZE) {
                    continue; // Saltar archivos muy grandes
                }
                
                ZipEntry zipEntry = new ZipEntry(relativePath);
                zipOutputStream.putNextEntry(zipEntry);
                Files.copy(file.toPath(), zipOutputStream);
                zipOutputStream.closeEntry();
            }
        }
        return zipFile;
    }
}
