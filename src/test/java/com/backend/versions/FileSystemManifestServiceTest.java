package com.backend.versions;

import org.junit.jupiter.api.Test;

import com.backend.versions.application.entities.Manifest;
import com.backend.versions.exceptions.ManifestNotFoundException;
import com.backend.versions.exceptions.ManifestParseException;
import com.backend.versions.infrastructure.FileSystemManifestService;

import java.io.File;
import java.io.FileWriter;

import static org.junit.jupiter.api.Assertions.*;



import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

class FileSystemManifestServiceTest {

    private final FileSystemManifestService service = new FileSystemManifestService();

    @Test
    void testReadValidManifest(@TempDir Path tempDir) throws Exception {
        File manifest = tempDir.resolve("manifest.json").toFile();
        try (FileWriter writer = new FileWriter(manifest)) {
            writer.write("{\"version\":\"1.0.0\",\"files\":[{\"path\":\"MARK-EPOS.exe\",\"checksum\":\"abc123\"}]}");
        }

        Manifest result = service.getManifest(manifest.getAbsolutePath());

        assertEquals("1.0.0", result.getVersion());
        assertEquals(1, result.getFiles().size());
        assertEquals("MARK-EPOS.exe", result.getFiles().get(0).getPath());
    }

    @Test
    void testManifestNotFound() {
        assertThrows(ManifestNotFoundException.class, () ->
                service.getManifest("non_existent_path/manifest.json"));
    }

    @Test
    void testInvalidManifestFormat(@TempDir Path tempDir) throws Exception {
        File manifest = tempDir.resolve("bad_manifest.json").toFile();
        try (FileWriter writer = new FileWriter(manifest)) {
            writer.write("this is not json");
        }

        assertThrows(ManifestParseException.class, () ->
                service.getManifest(manifest.getAbsolutePath()));
    }
}
