package com.backend.versions;

import com.backend.versions.application.ManifestService;
import com.backend.versions.application.entities.Manifest;
import com.backend.versions.application.usecases.GenerateUpdateZipUseCase;
import com.backend.versions.controller.UpdateController;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UpdateController.class)
@Import(SecurityTestConfig.class)
class UpdateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ManifestService manifestService;

    @MockitoBean
    private GenerateUpdateZipUseCase generateUpdateZipUseCase;

    @Test
    void testGetManifest() throws Exception {
        Manifest manifest = new Manifest();
        manifest.setVersion("1.0.0");
        manifest.setFiles(List.of());

        when(manifestService.getManifest(any())).thenReturn(manifest);

        mockMvc.perform(get("/api/update/manifest"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.version").value("1.0.0"));
    }
}

