package com.backend.versions.exceptions;

public class ManifestNotFoundException extends RuntimeException {
    public ManifestNotFoundException(String message) {
        super(message);
    }

}
