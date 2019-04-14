package com.liber.sun.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by sunlingzhi on 2017/11/24.
 */
@Document(collection = "Kun_Setting") //
public class Kun_Setting {
    @Id  // automatically generated
    private String id;

    private String name;

    private String value;


    @Override
    public String toString() {
        return "Kun_Setting{"+
                "id='"+id+"'"+
                ",name='"+name+"'"+
                ",value='"+value+"'"+
                "}";
    }

    public Kun_Setting() {

    }

    public Kun_Setting(String id, String name, String value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
