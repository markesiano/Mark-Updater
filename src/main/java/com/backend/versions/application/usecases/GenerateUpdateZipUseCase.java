package com.backend.versions.application.usecases;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.backend.versions.util.ZipUtils;
@Service
public class GenerateUpdateZipUseCase {
    @Value("${app.directory.base}")
    private String BASE_DIR;
    @Value("${app.directory.update.dist}")
    private String UPDATE_DIST_DIR;

   

    public File execute(List<String> filePaths){
        try {
            String PATH = BASE_DIR + UPDATE_DIST_DIR;
            return ZipUtils.createZip(filePaths, PATH, "update");
        } catch (Exception e) {
            throw new RuntimeException("Error generating update zip file", e);
        }
    }

}
