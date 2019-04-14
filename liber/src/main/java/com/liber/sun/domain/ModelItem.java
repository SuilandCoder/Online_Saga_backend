package com.liber.sun.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by sunlingzhi on 2017/11/1.
 */
@Document(collection = "myModelItem") //默认将ModelItem类映射到名为myModelItem的集合中
public class ModelItem {

    @Id  // automatically generated
    private String id;

    private String modelId;

    private String value;

    private String parentId;


    @Override
    public String toString() {
        return "ModelItem{"+
                "id='"+id+"'"+
                ",modelId='"+modelId+"'"+
                ",value='"+value+"'"+
                ",parentId='"+parentId+"'"+
                "}";
    }

    public ModelItem() {
    }

    public ModelItem(String id, String modelId, String value, String parentId) {
        this.id = id;
        this.modelId = modelId;
        this.value = value;
        this.parentId = parentId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
