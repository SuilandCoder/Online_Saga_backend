package com.liber.sun.utils;

/**
 * Created by sunlingzhi on 2017/10/31.
 */
public class GeoUtils {
    public static String  shapefile2geojson(String ShpFile,String geojsonFile,String PluginPath){

        if(!ShpFile.equals("")){
            String toGeoJsonCmd = "";
            String ogr2ogrPath = PluginPath+"\\ogr2ogr.exe";
            toGeoJsonCmd+=ogr2ogrPath +" -f \"GeoJSON\" "+"\""+geojsonFile+"\""+" "+"\""+ShpFile+"\"";
            if(!ProcessUtils.runModel(PluginPath,toGeoJsonCmd)){
                System.out.println("transform error");
                return "transform error";
            }else {
                return geojsonFile;
            }
        }else{
            return "shapfile NULL";
        }
    }

    public static String geojson2shapefile(String geojsonFile,String ShpFile,String projFile,String PluginPath){
        if(!ShpFile.equals("")){
            String Cmd = "";
            String GeoToolPath = PluginPath+"\\Kun\\GDAL_GeoTool.exe ";
            Cmd+=GeoToolPath +"-m geojson2shp -p "+geojsonFile+" "+ShpFile+" "+projFile;
            if(!ProcessUtils.runModel(PluginPath+"\\Kun",Cmd)){
                System.out.println("transform error");
                return "transform error";
            }else {
                return geojsonFile;
            }
        }else{
            return "shapfile NULL";
        }
    }


    public static String colorMapping(String tifFile,String colorBandFile,String outImageFile,String PluginPath){
        if(!tifFile.equals("")&&!colorBandFile.equals("")){
            String cmd = "";
            String GeoToolPath = PluginPath+"\\Kun"+"\\GDAL_GeoTool.exe ";
            cmd+=GeoToolPath +"-m colormapping -p "+tifFile+" "+colorBandFile+" "+outImageFile;
            if(!ProcessUtils.runModel(PluginPath+"\\Kun",cmd)){
                System.out.println("transform error");
                return "transform error";
            }else {
                return outImageFile;
            }
        }else{
            return "tifFile or  colorBandFile NULL";
        }

    }
}
