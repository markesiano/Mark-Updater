package com.backend.versions;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.backend.versions.util.ZipUtils;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.util.List;
import java.util.zip.ZipFile;

import static org.junit.jupiter.api.Assertions.*;

class GenerateUpdateZipUseCaseTest {

    @Test
    void testGenerateZipWithValidFiles(@TempDir Path tempDir) throws Exception {
        File file1 = tempDir.resolve("file1.txt").toFile();
        try (FileWriter writer = new FileWriter(file1)) {
            writer.write("Hello");
        }

        File zip = ZipUtils.createZip(List.of("file1.txt"), tempDir.toString(), "test-update");

        assertTrue(zip.exists());
        try (ZipFile zf = new ZipFile(zip)) {
            assertNotNull(zf.getEntry("file1.txt"));
        }
    }

    @Test
    void testGenerateZipWithMissingFile(@TempDir Path tempDir) throws Exception {
        File zip = ZipUtils.createZip(List.of("not_exist.txt"), tempDir.toString(), "test-update");
        assertTrue(zip.exists());
        try (ZipFile zf = new ZipFile(zip)) {
            assertNull(zf.getEntry("not_exist.txt"));
        }
    }
}
