package com.liber.sun.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * Created by SongJie on 2019/3/26 22:20
 */

@Document(collection = "ToolRecord")
public class ToolRecord {
    private String userId;
    @Id
    private String recordId;
    private String excuteTime;
    private String toolDescription;
    private String toolName;
    private String timeSpan;
    private int excuteState;// 0 运行中 1 成功 -1 失败
    private List<ToolData> inputList;
    private List<ToolData> outputList;

    public ToolRecord() {
    }

    public ToolRecord(String userId, String recordId, String excuteTime, String toolDescription, String toolName, String timeSpan, int excuteState, List<ToolData> inputList, List<ToolData> outputList) {
        this.userId = userId;
        this.recordId = recordId;
        this.excuteTime = excuteTime;
        this.toolDescription = toolDescription;
        this.toolName = toolName;
        this.timeSpan = timeSpan;
        this.excuteState = excuteState;
        this.inputList = inputList;
        this.outputList = outputList;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getExcuteTime() {
        return excuteTime;
    }

    public void setExcuteTime(String excuteTime) {
        this.excuteTime = excuteTime;
    }

    public String getToolDescription() {
        return toolDescription;
    }

    public void setToolDescription(String toolDescription) {
        this.toolDescription = toolDescription;
    }

    public String getToolName() {
        return toolName;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    public String getTimeSpan() {
        return timeSpan;
    }

    public void setTimeSpan(String timeSpan) {
        this.timeSpan = timeSpan;
    }

    public int getExcuteState() {
        return excuteState;
    }

    public void setExcuteState(int excuteState) {
        this.excuteState = excuteState;
    }

    public List<ToolData> getInputList() {
        return inputList;
    }

    public void setInputList(List<ToolData> inputList) {
        this.inputList = inputList;
    }

    public List<ToolData> getOutputList() {
        return outputList;
    }

    public void setOutputList(List<ToolData> outputList) {
        this.outputList = outputList;
    }

    @Override
    public String toString() {
        return "ToolRecord{" +
                "userId='" + userId + '\'' +
                ", recordId='" + recordId + '\'' +
                ", excuteTime=" + excuteTime +
                ", toolDescription='" + toolDescription + '\'' +
                ", toolName='" + toolName + '\'' +
                ", timeSpan='" + timeSpan + '\'' +
                ", excuteState='" + excuteState + '\'' +
                ", inputList=" + inputList +
                ", outputList=" + outputList +
                '}';
    }
}


