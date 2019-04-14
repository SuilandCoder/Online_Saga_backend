package com.liber.sun.domain;

import com.alibaba.fastjson.JSONArray;

import java.util.List;

/**
 * Created by SongJie on 2019/1/25 16:34
 */
public class TableInformation {
    List<String> fieldArr;
    JSONArray fieldValue;
    String dataPath;

    public TableInformation() {
    }

    public TableInformation(List<String> fieldArr, JSONArray fieldValue, String dataPath) {
        this.fieldArr = fieldArr;
        this.fieldValue = fieldValue;
        this.dataPath = dataPath;
    }

    public List<String> getFieldArr() {
        return fieldArr;
    }

    public void setFieldArr(List<String> fieldArr) {
        this.fieldArr = fieldArr;
    }

    public List getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(JSONArray fieldValue) {
        this.fieldValue = fieldValue;
    }

    public String getDataPath() {
        return dataPath;
    }

    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
    }

    @Override
    public String toString() {
        return "TableInformation{" +
                "fieldArr=" + fieldArr +
                ", fieldValue=" + fieldValue +
                ", dataPath='" + dataPath + '\'' +
                '}';
    }
}
