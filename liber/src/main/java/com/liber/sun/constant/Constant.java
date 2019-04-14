package com.liber.sun.constant;

/**
 * Created by SongJie on 2018/12/21 23:02
 */
public class Constant {
    public static final String SAGA_SERVER_IP = "172.21.212.75";
    public static final int SAGA_SERVER_PORT = 8060;
    public static final String MODEL_RECORD_JSON_ROUTER = "/modelserrun/json";
    public static final String MODEL_RUN_OUTPUT = "/modelserrun/output";


    public static final String DATA_CONTAINER_IP = "172.21.213.194";
    public static final int DATA_CONTAINER_PORT = 8081;
    public static final String DOWNLOAD_ONE_DATA_ROUTER = "/dataResource/getResource";
    public static final String DOWNLOAD_MULTI_DATA_ROUTER = "/dataResource/getResources/songjie";
    public static final String DATA_CONTAINER_DATARESOURCE = "/dataResource";
    public static final String DATA_CONTAINER_GETMETA = "/getMeta";


    public static final int MODEL_RUN_FAILED = -1;
    public static final int MODEL_RUN_RUNNING = 0;
    public static final int MODEL_RUN_SUCCESS = 1;
}
