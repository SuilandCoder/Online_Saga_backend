package com.liber.sun.domain;

/**
 * Created by SongJie on 2019/3/27 21:10
 */
public class ToolData{
    private String dataId;
    private String dataName;//eventName
    private String stateName;
    private String tag;
    private String downloadUrl;
    private String dataResourceId;
    private String type;

    public ToolData() {
    }

    public ToolData(String dataId, String dataName, String stateName, String tag, String downloadUrl, String dataResourceId, String type) {
        this.dataId = dataId;
        this.dataName = dataName;
        this.stateName = stateName;
        this.tag = tag;
        this.downloadUrl = downloadUrl;
        this.dataResourceId = dataResourceId;
        this.type = type;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getDataResourceId() {
        return dataResourceId;
    }

    public void setDataResourceId(String dataResourceId) {
        this.dataResourceId = dataResourceId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ToolData{" +
                "dataId='" + dataId + '\'' +
                ", dataName='" + dataName + '\'' +
                ", stateName='" + stateName + '\'' +
                ", tag='" + tag + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                ", dataResourceId='" + dataResourceId + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}