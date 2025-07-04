package com.backend.versions.exceptions;

public class FileNotAvailableException extends RuntimeException {
    public FileNotAvailableException(String message) {
        super(message);
    }
}
