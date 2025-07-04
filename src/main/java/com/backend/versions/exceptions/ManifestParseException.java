package com.backend.versions.exceptions;


public class ManifestParseException extends RuntimeException {
    public ManifestParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
