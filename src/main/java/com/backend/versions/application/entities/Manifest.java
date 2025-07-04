package com.backend.versions.application.entities;

import java.util.List;

public class Manifest {
    private String version;
    private List<FileEntryWithSize> files;

    public static class FileEntry {
        private String path;
        private String checksum;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getChecksum() {
            return checksum;
        }

        public void setChecksum(String checksum) {
            this.checksum = checksum;
        }
    }

    public static class FileEntryWithSize extends FileEntry {
        private String size;

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<FileEntryWithSize> getFiles() {
        return files;
    }

    public void setFiles(List<FileEntryWithSize> files) {
        this.files = files;
    }
}

