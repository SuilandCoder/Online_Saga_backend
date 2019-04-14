package com.liber.sun.domain;



/**
 * Created by sunlingzhi on 2017/12/12.
 */
public class TifInformation {

    String filePosition;
    String SRS;
    String GeoTransform;
    String dataPath;

    public TifInformation() {
    }

    public TifInformation(String filePosition, String SRS, String geoTransform, String dataPath) {
        this.filePosition = filePosition;
        this.SRS = SRS;
        GeoTransform = geoTransform;
        this.dataPath = dataPath;
    }

    public String getFilePosition() {
        return filePosition;
    }

    public void setFilePosition(String filePosition) {
        this.filePosition = filePosition;
    }

    public String getSRS() {
        return SRS;
    }

    public void setSRS(String SRS) {
        this.SRS = SRS;
    }

    public String getGeoTransform() {
        return GeoTransform;
    }

    public void setGeoTransform(String geoTransform) {
        GeoTransform = geoTransform;
    }

    public String getDataPath() {
        return dataPath;
    }

    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
    }
}
