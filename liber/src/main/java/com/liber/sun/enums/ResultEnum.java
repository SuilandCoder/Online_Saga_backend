package com.liber.sun.enums;

/**
 * code 和 msg的一一对应
 * Created by sunlingzhi on 2017/10/24.
 */
public enum ResultEnum {
    SUCCESS(0,"success"),
    NO_QUERY_CONDITION_ERROR(1,"No query condition set!"),
    GET_MODEL_SERVICE_ERROR(2,"Failed to get model service!"),
    CREATE_CONTROL_FILE_ERROR(3,"Failed to generate parameter file!"),
    MODEL_RUN_ERROR(4,"Running model failed"),
    NO_PROJ_ERROR(5,"Lack of projection information"),
    LOAD_DATA_FAILED(6,"Failed to load data"),
    NO_OUTPUT_DATA(7,"Output data is empty"),
    MISSING_REQUIRED_DATA(8,"Lack of mandatory data"),
    ALREADY_EXISTS(9,"User already exists"),
    NOT_EXISTS(10,"User does not exist"),
    PASSWORD_ERROR(11,"Login failed, password is wrong"),
    ADD_USER_FAILED(12,"Registered user failed"),
    CREATE_TOKEN_FAILED(13,"Failed to generate token");



    private  Integer code;

    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    //忽略Set方法，枚举往往不需要Set
    public Integer getCode() {
        return code;
    }
    public String getMsg() {
        return msg;
    }

}
