package com.liber.sun.domain;

/**
 * Created by SongJie on 2019/4/2 20:01
 */
public class ToolDataParam {
    public String identifier;
    public String name;
    public String optional;
    public String type;

    public ToolDataParam() {
    }

    public ToolDataParam(String identifier, String name, String optional, String type) {
        this.identifier = identifier;
        this.name = name;
        this.optional = optional;
        this.type = type;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOptional() {
        return optional;
    }

    public void setOptional(String optional) {
        this.optional = optional;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ToolDataParam{" +
                "identifier='" + identifier + '\'' +
                ", name='" + name + '\'' +
                ", optional='" + optional + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
