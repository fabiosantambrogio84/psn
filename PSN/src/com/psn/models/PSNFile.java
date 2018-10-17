package com.psn.models;

import com.psn.Utils;

public class PSNFile {

    private String name;

    private String extension;

    private String absolutePath;

    private FileStatus status;

    private String statusLabel;

    public PSNFile(String absolutePath) {
        this.absolutePath = absolutePath;
        this.name = Utils.getFileNameNoExt(absolutePath);
        this.extension = Utils.getFileExtension(absolutePath);
        this.status = FileStatus.START;
        this.setStatusLabel(FileStatus.START.getLabel());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public FileStatus getStatus() {
        return status;
    }

    public void setStatus(FileStatus status) {
        this.status = status;
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
    }

}
