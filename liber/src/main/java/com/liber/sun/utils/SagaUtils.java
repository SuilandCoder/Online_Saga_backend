package com.liber.sun.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.liber.sun.utils.MyFileUtils.getSpecificFile;

/**
 * Created by SongJie on 2018/12/25 21:34
 */
public class SagaUtils {

    public static boolean sgrdToBmp(String sgrdDir, String bmpPath) {
        //获取sgrd文件路径
        String sgrdPath = getSpecificFile(sgrdDir, "sgrd");
        if(!sgrdPath.isEmpty()){
            String[] cmd = {"cmd", "/C", ""};
            String saga_cmd = "saga_cmd io_grid_image 0 -GRID=" + "\"" + sgrdPath + "\"" + " -FILE=" + "\"" + bmpPath + "\"" + " -FILE_KML=1";
            cmd[2] = saga_cmd;
            try {
                Runtime.getRuntime().exec(cmd);
                //生成bmp需要耗时，此处循环判断文件是否已经生成，最多5秒后返回结果
                File bmpFile = new File(bmpPath);
                System.out.println("bmpPath:"+bmpPath);
                int time = 0;
                while(!bmpFile.exists() && time<=5){
                    System.out.println("转换sgrd文件耗时:"+time);
                    Thread.sleep(1000);
                    time++;
                }
                if(bmpFile.exists()) return true;
                else return false;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static List<String> sgrdListToBmp(String sgrdDir, String bmpParentPath) {
        //获取sgrd文件路径
        List<String> bmpPathList = new ArrayList<>();
        List<String> sgrdList = MyFileUtils.getFileListInDir(sgrdDir, "sgrd");
        for(int i =0; i<sgrdList.size();i++){
            String sgrdPath = sgrdList.get(i);
            if(!sgrdPath.isEmpty()){
                String sgrdName = new File(sgrdPath).getName();
                String sgrdNameNoExt = sgrdName.substring(0,sgrdName.lastIndexOf('.'));
                String bmpPath = bmpParentPath + sgrdNameNoExt+".bmp";
                bmpPathList.add(bmpPath);
                String[] cmd = {"cmd", "/C", ""};
                String saga_cmd = "saga_cmd io_grid_image 0 -GRID=" + "\"" + sgrdPath + "\"" + " -FILE=" + "\"" + bmpPath + "\"" + " -FILE_KML=1";
                cmd[2] = saga_cmd;
                try {
                    Runtime.getRuntime().exec(cmd);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bmpPathList;
    }
}
