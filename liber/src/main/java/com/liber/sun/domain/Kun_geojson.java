package com.liber.sun.domain;

/**
 * Created by sunlingzhi on 2017/12/14.
 */
public class Kun_geojson {
    String geojson;
    String proj;
    String dataPath;//shp 压缩文件路径

    public Kun_geojson(String geojson, String proj) {
        this.geojson = geojson;
        this.proj = proj;
    }

    public String getGeojson() {
        return geojson;
    }

    public void setGeojson(String geojson) {
        this.geojson = geojson;
    }

    public String getProj() {
        return proj;
    }

    public void setProj(String proj) {
        this.proj = proj;
    }

    public String getDataPath() {
        return dataPath;
    }

    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
    }
}
