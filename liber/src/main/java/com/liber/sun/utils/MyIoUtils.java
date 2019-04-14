package com.liber.sun.utils;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sunlingzhi on 2017/10/19.
 */
public class MyIoUtils {

    public static String inputStreamToString(InputStream in) throws IOException {
        return  IOUtils.toString(in);
    }

    public static InputStream stringToInputStream(String str){
        return  IOUtils.toInputStream(str);
    }

    public static byte [] inputStreamToByte(InputStream inputStream) throws IOException {
        return  IOUtils.toByteArray(inputStream);
    }




    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

}
