package com.liber.sun.domain;

/**
 * Created by SongJie on 2019/3/20 19:08
 */
public class DataUploadInfo {
    private String fileName;
    private String sourceStoreId;
    private String suffix;

    public DataUploadInfo() {
    }

    public DataUploadInfo(String fileName, String sourceStoreId, String suffix) {
        this.fileName = fileName;
        this.sourceStoreId = sourceStoreId;
        this.suffix = suffix;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSourceStoreId() {
        return sourceStoreId;
    }

    public void setSourceStoreId(String sourceStoreId) {
        this.sourceStoreId = sourceStoreId;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    @Override
    public String toString() {
        return "DataUploadInfo{" +
                "fileName='" + fileName + '\'' +
                ", sourceStoreId='" + sourceStoreId + '\'' +
                ", suffix='" + suffix + '\'' +
                '}';
    }
}
