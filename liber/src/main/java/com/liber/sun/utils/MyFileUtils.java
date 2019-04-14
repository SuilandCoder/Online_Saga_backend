package com.liber.sun.utils;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.liber.sun.domain.TableInformation;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.lang.reflect.Array;
import java.net.URL;

import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Created by sunlingzhi on 2017/10/19.
 */
public class MyFileUtils {

    public static String getExtension(String fileName) {
        if (StringUtils.INDEX_NOT_FOUND == StringUtils.indexOf(fileName, "."))
            return StringUtils.EMPTY;
        String ext = StringUtils.substring(fileName, StringUtils.lastIndexOf(fileName, ".") + 1);
        return StringUtils.trimToEmpty(ext);
    }

    public static String getFileNameWithoutExtension(String fileName) {
        if (StringUtils.INDEX_NOT_FOUND == StringUtils.indexOf(fileName, "."))
            return StringUtils.EMPTY;
        String fileNameWithoutExtension = StringUtils.substring(fileName, 0, StringUtils.lastIndexOf(fileName, "."));
        return StringUtils.trimToEmpty(fileNameWithoutExtension);
    }

    public static boolean writeInputStreamToFile(InputStream inputStream, File f) {
        try {
            FileUtils.copyInputStreamToFile(inputStream, f);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean writeURLToFile(URL url, File f) {
        try {
            FileUtils.copyURLToFile(url, f);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static boolean writeStringToFile(String data, File file, String encoding) {
        try {
            FileUtils.writeStringToFile(file, data, encoding);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean AppendStringToFile(String data, File file, String encoding) {
        try {
            FileUtils.writeStringToFile(file, data, encoding, true);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean writeByteArrayToFile(byte[] data, File file) {
        try {
            FileUtils.writeByteArrayToFile(file, data);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean AppendByteArrayToFile(byte[] data, File file) {
        try {

            FileUtils.writeByteArrayToFile(file, data, true);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static void mkFolder(String fileName) {
        File f = new File(fileName);
        if (!f.exists()) {
            f.mkdir();
        }
    }

    public static File mkFile(String fileName) {
        File f = new File(fileName);
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f;
    }

    public static void deleteFiles(String directoryForDel, boolean keepItself) throws Exception {
        File file = new File(directoryForDel);
        if (keepItself) {
            FileUtils.cleanDirectory(file);
        } else {
            FileUtils.deleteDirectory(file);
        }
    }

    public static void deleteFile(File file) throws Exception {
        FileUtils.forceDelete(file);
    }


    public static void copyFileWithSuffix(File srcDir, File destDir, String suffix) {
        IOFileFilter suffixFileFilter = FileFilterUtils.suffixFileFilter(suffix);
        IOFileFilter fileFilter = FileFilterUtils.and(FileFileFilter.FILE, suffixFileFilter);
        try {
            FileUtils.copyDirectory(srcDir, destDir, fileFilter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void moveDirectory(File srcDir, File destDir) {
        try {
            FileUtils.moveDirectory(srcDir, destDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void moveFile(File srcFile, File destFile) {
        try {
            FileUtils.moveFile(srcFile, destFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String getFileContentByString(File file, String encode) {
        try {
            return FileUtils.readFileToString(file, encode);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] getFileContentByByte(File file) {
        try {
            return FileUtils.readFileToByteArray(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static InputStream getFileContentByInputStream(File file) {
        try {
            return FileUtils.openInputStream(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static OutputStream getFileOutputStream(File file, Boolean appendFlag) {
        try {
            if (appendFlag == true) {
                return FileUtils.openOutputStream(file, true);
            } else {
                return FileUtils.openOutputStream(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param directory  遍历的文件夹
     * @param extensions 文件后缀过滤
     * @param recursive  true 为递归，false为非递归
     * @return
     */
    public static Collection<File> getFileList(File directory, String[] extensions, boolean recursive) {
        return FileUtils.listFiles(directory, extensions, recursive);
    }

    /**
     * @param directory  进行遍历的文件夹
     * @param fileFilter 文件需要满足的过滤器条件后缀
     *                   比如EmptyFileFilter、NameFileFilter、PrefixFileFilter
     *                   并且可以通过FileFilterUtils.and()进行组合
     * @param dirFilter  为空表示不进行递归遍历，DirectoryFileFilter.INSTANCE表示递归遍历文件夹
     * @return 文件
     */
    public static Collection<File> getFileList(File directory, IOFileFilter fileFilter, IOFileFilter dirFilter) {
        return FileUtils.listFiles(directory, fileFilter, dirFilter);
    }

    public static void directoryContains(File directory, File child) {
        try {
            FileUtils.directoryContains(directory, child);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File multipartToFile(MultipartFile multfile) throws IOException {
        File f = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") +
                multfile.getOriginalFilename());
        MyFileUtils.writeInputStreamToFile(multfile.getInputStream(), f);
        return f;
    }


    public static List<String> getFileListInDir(String dirPath, String ext) {
        List<String> list = new ArrayList<>();
        File directory = new File(dirPath);
        Collection<File> fileCollection = getFileList(directory, new String[]{ext}, false);
        Iterator<File> it = fileCollection.iterator();
        while (it.hasNext()) {
            File file = it.next();
            list.add(file.getAbsolutePath());
        }
        return list;
    }


    public static String getShpFileInDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        Collection<File> fileCollection = getFileList(directory, new String[]{"shp","SHP","Shp"}, false);
        if (fileCollection.size() == 0) {
            return "无shp文件";
        } else if (fileCollection.size() == 1) {
            Iterator<File> it = fileCollection.iterator();
            File files = it.next();
            return files.getAbsolutePath();
        } else {
            return "shp文件数目有问题";
        }
    }

    public static List<String> getShpFileListDir(String directoryPath) {
        List<String> list = new ArrayList<>();
        File directory = new File(directoryPath);
        Collection<File> fileCollection = getFileList(directory, new String[]{"shp","SHP","Shp"}, false);
        Iterator<File> it = fileCollection.iterator();
        while (it.hasNext()) {
            File file = it.next();
            list.add(file.getAbsolutePath());
        }
        return list;
    }

    public static String getTifFileInDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        Collection<File> fileCollection = getFileList(directory, new String[]{"tif","TIF"}, false);
        if (fileCollection.size() == 0) {
            return "无tif文件";
        } else if (fileCollection.size() == 1) {
            Iterator<File> it = fileCollection.iterator();
            File files = it.next();
            return files.getAbsolutePath();
        } else {
            return "tif文件数目有问题";
        }
    }

    public static String getProjInDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        Collection<File> fileCollection = getFileList(directory, new String[]{"prj","PRJ"}, false);
        if (fileCollection.size() == 0) {
            return "prj";
        } else if (fileCollection.size() == 1) {
            Iterator<File> it = fileCollection.iterator();
            File files = it.next();
            return files.getAbsolutePath();
        } else {
            return "prj文件数目有问题";
        }
    }

    public static String getProjInDirectory(String directoryPath, String fileName) {
        File directory = new File(directoryPath);
        Collection<File> fileCollection = getFileList(directory, new String[]{"prj","PRF"}, false);
        Iterator<File> it = fileCollection.iterator();
        while (it.hasNext()) {
            File file = it.next();
            String fileNameNoExt = file.getName().substring(0, file.getName().lastIndexOf('.'));
            if (fileName.equals(fileNameNoExt)) {
                return file.getAbsolutePath();
            }
        }
        return "";
    }


    //创建模型的 control udx文件，
    public static boolean createControlXml(JSONObject jobj, String path) {

        DocumentBuilder db = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            db = dbf.newDocumentBuilder();
            Document document = db.newDocument();
            //隐藏xml文件 standalone属性
            document.setXmlStandalone(true);
            //创建根节点
            Element dataset = document.createElement("dataset");
            //创建子节点
            Iterator<String> keyIterator = jobj.keySet().iterator();
            while (keyIterator.hasNext()) {
                String jsonKey = keyIterator.next();
                String value = jobj.getString(jsonKey);
                Element xdo = document.createElement("XDO");
                xdo.setAttribute("name", jsonKey);
                xdo.setAttribute("kernelType", "string");
                xdo.setAttribute("value", value);
                if (value == null || value.length() <= 0) {
                    break;
                }
                dataset.appendChild(xdo);
            }
            document.appendChild(dataset);
            TransformerFactory tff = TransformerFactory.newInstance();
            Transformer tf = tff.newTransformer();
            //设置生成的xml文件自动换行
            tf.setOutputProperty(OutputKeys.INDENT, "yes");
            //使用Transformer的 transform方法把Dom树转换成xml文件
            tf.transform(new DOMSource(document), new StreamResult(new File(path)));
            return true;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return false;
    }

    //创建文件夹
    public static String createDir(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        return path;
    }

    //查找指定后缀的文件  返回绝对路径
    public static String getSpecificFile(String dirPath, String fileExt) {
        File file = new File(dirPath);
        File[] fs = file.listFiles();
        for (File f : fs) {
            if (!f.isDirectory()) {
                String fName = f.getName();
                String extension = fName.substring(fName.lastIndexOf('.') + 1);
                if (fileExt.equals(extension)) {
                    return f.getAbsolutePath();
                }
            }
        }
        return "";
    }


    /**
     * 读取文件内容，将文件按行读取到 String 数组中
     *
     * @param filePath 文件的路径
     * @return
     */
    public static String[] readToString(String filePath) {
        File file = new File(filePath);
        Long length = file.length();
        byte[] content = new byte[length.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(content);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] contentArr = new String(content).split("\r\n");
//        System.out.println(new String(content));
        return contentArr;
    }


    /**
     * 将一个文件夹中文件名相同的文件压缩成一个压缩包
     * 针对文件夹中有 shp-list/sgrd-list 文件
     *
     * @param dirPath 文件夹路径
     * @param fileExt 文件类型
     * @return 压缩后的文件名集合
     */
    public static List<String> zipFilesInDir(String dirPath, String fileExt) throws IOException {
        List<String> zipNames = new ArrayList<>();
        File file = new File(dirPath);
        File[] files = file.listFiles();
        for (File f : files) {
            if (!f.isDirectory()) {
                String fName = f.getName();
                String extension = fName.substring(fName.lastIndexOf('.') + 1);
                if (fileExt.equals(extension)) {
                    String fNameWithoutExt = fName.substring(0, fName.indexOf('.'));
                    String zipName = fNameWithoutExt + ".zip";
                    ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(dirPath + "/" + zipName));
                    File[] filesInDir = getFilesInDir(dirPath, fNameWithoutExt);
                    ZipUtils.zipFiles(zipOut, "", filesInDir);
                    zipOut.closeEntry();
                    zipOut.close();
                    zipNames.add(zipName);
                }
            }
        }
        return zipNames;
    }

    public static File[] getFilesInDir(String dirPath, String fileName) {
        File file = new File(dirPath);
        File[] files = file.listFiles();
        File[] result = new File[files.length];
        int i = 0;
        for (File f : files) {
            if (!f.isDirectory()) {
                String fName = f.getName();
                String fNameWithoutExt = fName.substring(0, fName.indexOf('.'));
                String extension = getExtension(fName);
                if (fileName.equals(fNameWithoutExt) && !"zip".equals(extension)) {
                    result[i] = f;
                    i++;
                }
            }
        }
        return result;
    }


    public static void addExtSgrd(String sgrdFilePath) throws IOException {
        File file = new File(sgrdFilePath);
        File[] allFiles = file.listFiles();
        boolean hasSgrd = false;
        boolean hasMgrd = false;
        for(File f:allFiles){
            String extension = MyFileUtils.getExtension(f.getName());
            if (extension.equals("sgrd")) {
                hasSgrd = true;
            } else if (extension.equals("mgrd")) {
                hasMgrd = true;
            }
        }
        if (hasMgrd && !hasSgrd) {
            for (int i = 0; i < allFiles.length; i++) {
                File currentFile = allFiles[i];
                if (!currentFile.isDirectory()) {
                    String extension = MyFileUtils.getExtension(currentFile.getName());
                    if (StringUtils.EMPTY.equals(extension)) {
                        String newName = currentFile.getName() + ".sgrd";
                        File newFile = new File(sgrdFilePath, newName);
                        currentFile.renameTo(newFile);
                    }
                }
            }
        }
    }


    public static TableInformation getTableInfo(String filePath) throws IOException {
        TableInformation ti = new TableInformation();
        JSONArray ja = new JSONArray();
        File file = new File(filePath);
        InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file));
        BufferedReader br = new BufferedReader(inputStreamReader);
        String line = "";
        boolean firstLine = true;
        line = br.readLine();
        while (line!=null){
            String[] fieldArr = line.split("\t");
            if(firstLine){
                ti.setFieldArr(Arrays.asList(fieldArr));
            }else{
                JSONObject jsonObject = new JSONObject();
                for(int i=0;i<fieldArr.length;i++){
                    jsonObject.put(ti.getFieldArr().get(i),fieldArr[i]);
                }
                ja.add(jsonObject);
            }
            line = br.readLine();
            firstLine = false;
        }
        ti.setFieldValue(ja);
        int index = filePath.indexOf("dataProcess");
        String dataPath = filePath.substring(index);
        ti.setDataPath(dataPath);
        return ti;
    }
}
