package com.backend.versions.application;

import com.backend.versions.application.entities.Manifest;

public interface ManifestService {
    Manifest getManifest(String path);
}
