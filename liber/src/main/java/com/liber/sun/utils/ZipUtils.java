package com.liber.sun.utils;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;


/**
 * Created by sunlingzhi on 2017/10/19.
 */
public class ZipUtils {
    public static void zipFiles(ZipOutputStream out, String path, File... srcFiles) {
        path = path.replaceAll("\\*", "/");
        if (path.equals("")) {

        } else {
            if (!path.endsWith("/")) {
                path += "/";
            }
        }

        byte[] buf = new byte[1024];
        try {
            for (File srcFile : srcFiles) {
                if(null==srcFile) continue;
                if (srcFile.isDirectory()) {
                    File[] files = srcFile.listFiles();
                    String srcPath = srcFile.getName();
                    srcPath = srcPath.replaceAll("\\*", "/");
                    if (!srcPath.endsWith("/")) {
                        srcPath += "/";
                    }
                    out.putNextEntry(new ZipEntry(path + srcPath));
                    zipFiles(out, path + srcPath, files);
                } else {
                    try (FileInputStream in = new FileInputStream(srcFile)) {
                        out.putNextEntry(new ZipEntry(path + srcFile.getName()));
                        int len;
                        while ((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                        }
                        out.closeEntry();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void unZipFiles(File zipFile, String descDir) throws IOException {
        if (!descDir.endsWith("/")) {
            descDir += "/";
        }

        File pathFile = new File(descDir);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        ZipFile zip = new ZipFile(zipFile);
        long tag= System.currentTimeMillis();
        for (Enumeration entries = zip.entries(); entries.hasMoreElements(); ) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            String zipEntryName = entry.getName();
            InputStream in = zip.getInputStream(entry);
            String outPath = (descDir + zipEntryName).replaceAll("\\*", "/");
            //判断路径是否存在,不存在则创建文件路径
            File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
            if (!file.exists()) {
                file.mkdirs();
            }
            //判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
            if (new File(outPath).isDirectory()) {
                continue;
            }
            StringBuilder sb = new StringBuilder(outPath);
            if(outPath.lastIndexOf('.')!=-1){
                sb.insert(outPath.lastIndexOf("."),tag);
            }else{
                sb.append(tag);
            }
            outPath = sb.toString();
            OutputStream out = new FileOutputStream(outPath);
            byte[] buf1 = new byte[1024];
            int len;
            while ((len = in.read(buf1)) > 0) {
                out.write(buf1, 0, len);
            }
            in.close();
            out.close();
        }
        zip.close();
    }

    //不解压读取其中的文件,返回String
    public static String readFileStringWithOutUnZip(File file, String fileName) throws IOException {
        InputStream inputStream = null;
        try {
            ZipFile zip = new ZipFile(file);
            ZipEntry entry = zip.getEntry(fileName);
            inputStream = zip.getInputStream(entry);
        } catch (ZipException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return MyIoUtils.inputStreamToString(inputStream);
    }

    //不解压读取其中的文件,InputStream
    public static InputStream readFileInputStreamWithOutUnZip(File file, String fileName) {
        try {
            ZipFile zip = new ZipFile(file);
            ZipEntry entry = zip.getEntry(fileName);
            return zip.getInputStream(entry);
        } catch (ZipException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
