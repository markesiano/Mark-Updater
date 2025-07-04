package com.backend.versions;


import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import com.backend.versions.exceptions.GlobalExceptionHandler;
import com.backend.versions.exceptions.ManifestNotFoundException;
import com.backend.versions.exceptions.ManifestParseException;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void testManifestNotFoundException() {
        ResponseEntity<String> response = handler.handleManifestRead(
                new ManifestNotFoundException("No manifest"));
        assertEquals(500, response.getStatusCode().value());
        String body = response.getBody();
        assertNotNull(body);
        assertTrue(body.contains("manifest not found"));
    }

    @Test
    void testManifestParseException() {
        ResponseEntity<String> response = handler.handleManifestParse(
                new ManifestParseException("Bad JSON", new Exception()));
        assertEquals(500, response.getStatusCode().value());
        String body = response.getBody();
        assertNotNull(body);
        assertTrue(body.contains("manifest parse error"));
    }

    @Test
    void testGenericRuntimeException() {
        ResponseEntity<String> response = handler.handleRuntimeException(
                new RuntimeException("Boom"));
        assertEquals(500, response.getStatusCode().value());
        String body = response.getBody();
        assertNotNull(body);
        assertTrue(body.contains("unexpected error"));
    }
}
