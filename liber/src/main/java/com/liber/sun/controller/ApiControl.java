package com.liber.sun.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.liber.sun.constant.Constant;
import com.liber.sun.domain.*;

import com.liber.sun.enums.ResultEnum;
import com.liber.sun.utils.*;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ResultTreeType;
import io.swagger.annotations.ApiOperation;
import modelservice.*;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.dom.DOMDocument;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import static com.liber.sun.utils.MyFileUtils.createControlXml;
import static com.liber.sun.utils.MyFileUtils.createDir;


/**
 * Created by sunlingzhi on 2017/10/30.
 */
@RestController
@RequestMapping("/api")
public class ApiControl {


    @Value("${web.upload-path}")
    private String ROOT;
    @Value("${port.modelContainer}")
    private String port;


    @RequestMapping(value = "/runSagaModelByDC", method = RequestMethod.POST)
    public Result<String> runSagaModeByDC(HttpServletRequest request){
        try {
            Server server = ServerFactory.createServer(Constant.SAGA_SERVER_IP, Constant.SAGA_SERVER_PORT);
            if (server.connect() == 1) {
                ServiceAccess pServiceAccess = server.getServiceAccess();
                DataConfiguration pDataconfig = pServiceAccess.createDataConfig();

                String stateId = "";
                String oid = "";
                String control = "";
                String modelFilePath = ROOT + "/dataProcess/temp/" + System.currentTimeMillis() + "/";
                createDir(modelFilePath);
                String fileInputPath = modelFilePath;
                ModelService pMservice = null;

                //非文件类型参数
                Map<String, String[]> parameter = request.getParameterMap();
                for (String key : parameter.keySet()) {
                    switch (key) {
                        case "stateId":
                            stateId = parameter.get(key)[0];
                            break;
                        case "oid":
                            oid = parameter.get(key)[0];
                            pMservice = pServiceAccess.getModelServiceByOID(oid);
                            if (pMservice == null) {
                                return ResultUtil.error(ResultEnum.GET_MODEL_SERVICE_ERROR);
                            }
                            break;
                        case "control":
                            control = parameter.get(key)[0];
                            JSONObject jobj = (JSONObject) JSON.parse(control);
                            String controlPath = fileInputPath + "control.xml";
                            boolean xmlOk = createControlXml(jobj, controlPath);
                            if (!xmlOk) return ResultUtil.error(ResultEnum.CREATE_CONTROL_FILE_ERROR);
                            Data pData = pServiceAccess.uploadDataByFile(controlPath, "control.xml");
                            if (pData != null) {
                                System.out.println(pData.getID() + " - " + pData.getSize() + " - " + pData.getGenerationDateTime() + " - " + pData.getTag());
                                pDataconfig.insertData(stateId, "Control", pData.getID(), false);
                            }
                            break;
                        default:
                            String event = key;
                            String dataInfo = parameter.get(key)[0];
                            List<DataUploadInfo> dataUploadInfos = JSON.parseArray(dataInfo,DataUploadInfo.class);
                            String requestURL = "http://"+Constant.SAGA_SERVER_IP+":"+Constant.SAGA_SERVER_PORT+"/geodata?type=url";
                            String dataId = MyHttpUtils.PostFileUrlToMC(requestURL, dataUploadInfos);
                            if(dataId.isEmpty()){
                              return ResultUtil.error(ResultEnum.MODEL_RUN_ERROR);
                            }

                            pDataconfig.insertData(stateId, event, dataId, false);
                            break;
                    }
                }
                String recordId = pMservice.invoke(pDataconfig);
                System.out.println("recordId:" + recordId);
                if (recordId != null && recordId.length() > 0) {
                    return ResultUtil.success(recordId);
                } else {
                    return ResultUtil.error(ResultEnum.MODEL_RUN_ERROR);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultUtil.error(ResultEnum.MODEL_RUN_ERROR);
    }



    @RequestMapping(value = "/runSagaModel", method = RequestMethod.POST)
    public Result<String> runSagaModel(HttpServletRequest request) {
        try {
            Server server = ServerFactory.createServer(Constant.SAGA_SERVER_IP, Constant.SAGA_SERVER_PORT);
            if (server.connect() == 1) {
                ServiceAccess pServiceAccess = server.getServiceAccess();
                DataConfiguration pDataconfig = pServiceAccess.createDataConfig();

                String stateId = "";
                String oid = "";
                String control = "";
                String modelFilePath = ROOT + "/dataProcess/temp/" + System.currentTimeMillis() + "/";
                createDir(modelFilePath);
                String fileInputPath = modelFilePath;
                ModelService pMservice = null;

                //非文件类型参数
                Map<String, String[]> parameter = request.getParameterMap();
                for (String key : parameter.keySet()) {
                    switch (key) {
                        case "stateId":
                            stateId = parameter.get(key)[0];
                            break;
                        case "oid":
                            oid = parameter.get(key)[0];
                            pMservice = pServiceAccess.getModelServiceByOID(oid);
                            if (pMservice == null) {
                                return ResultUtil.error(ResultEnum.GET_MODEL_SERVICE_ERROR);
                            }
                            break;
                        case "control":
                            control = parameter.get(key)[0];
                            JSONObject jobj = (JSONObject) JSON.parse(control);
                            String controlPath = fileInputPath + "control.xml";
                            boolean xmlOk = createControlXml(jobj, controlPath);
                            if (!xmlOk) return ResultUtil.error(ResultEnum.CREATE_CONTROL_FILE_ERROR);
                            Data pData = pServiceAccess.uploadDataByFile(controlPath, "control.xml");
                            if (pData != null) {
                                System.out.println(pData.getID() + " - " + pData.getSize() + " - " + pData.getGenerationDateTime() + " - " + pData.getTag());
                                pDataconfig.insertData(stateId, "Control", pData.getID(), false);
                            }
                            break;
                        default:
                            String event = key;
                            String pathFromReq = parameter.get(key)[0];
                            String dataPath = "";
                            // 根据 ‘,’ 分割 pathFromReq 字符串，判断输入数据是 list 还是 单个路径
                            String[] pathList = pathFromReq.split(",");
                            if (pathList.length > 1) {
                                String tmpUnzipPath = modelFilePath + "unzip/";
                                String tmpZipPath = modelFilePath + "zip/";
                                createDir(tmpZipPath);
                                createDir(tmpUnzipPath);
                                for (int j = 0; j < pathList.length; j++) {
                                    File tempFile = new File(ROOT + "/" + pathList[j]);
                                    ZipUtils.unZipFiles(tempFile, tmpUnzipPath);
                                }
                                String zipFilePath = tmpZipPath + "list.zip";
                                ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFilePath));
                                ZipUtils.zipFiles(zipOut, "", new File(tmpUnzipPath).listFiles());
                                zipOut.closeEntry();
                                zipOut.close();
                                dataPath = zipFilePath;
                            } else {
                                dataPath = ROOT + "/" + pathFromReq;
                            }
                            String fileName = dataPath.substring(dataPath.lastIndexOf("/") + 1);
                            Data pData_Local = pServiceAccess.uploadDataByFile(dataPath, fileName);
                            if (pData_Local != null) {
                                pDataconfig.insertData(stateId, event, pData_Local.getID(), false);
                            }
                            break;
                    }
                }

                //文件类型参数
                MultiValueMap<String, MultipartFile> multiFileMap = ((MultipartHttpServletRequest) request).getMultiFileMap();
                String finalStateId = stateId;
                multiFileMap.forEach((key, fileList) -> {
                    try {
                        String event = key;
                        String fileName = "";
                        String filePath = "";
                        if (fileList.size() == 1) {
                            MultipartFile file = fileList.get(0);
                            fileName = file.getOriginalFilename();
                            filePath = fileInputPath + file.getOriginalFilename();
                            File uploadFile = new File(filePath);

                            file.transferTo(uploadFile);

                        } else if (fileList.size() > 1) {
                            fileName = "list_" + System.currentTimeMillis() + ".zip";
                            String tmpUnzipPath = modelFilePath + "unzip/";
                            String tmpZipPath = modelFilePath + "zip/";
                            createDir(tmpZipPath);
                            createDir(tmpUnzipPath);
                            for (int i = 0; i < fileList.size(); i++) {
                                MultipartFile mf = fileList.get(i);
                                File file = new File(modelFilePath + mf.getOriginalFilename());
                                mf.transferTo(file);
                                ZipUtils.unZipFiles(file, tmpUnzipPath);
                            }
                            String zipFilePath = tmpZipPath + fileName;
                            ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFilePath));
                            ZipUtils.zipFiles(zipOut, "", new File(tmpUnzipPath).listFiles());
                            zipOut.closeEntry();
                            zipOut.close();
                            filePath = zipFilePath;
                        }
                        Data pData = pServiceAccess.uploadDataByFile(filePath, fileName);
                        if (pData != null) {
                            pDataconfig.insertData(finalStateId, event, pData.getID(), false);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                String recordId = pMservice.invoke(pDataconfig);
                System.out.println("recordId:" + recordId);
                if (recordId != null && recordId.length() > 0) {
                    return ResultUtil.success(recordId);
                } else {
                    return ResultUtil.error(ResultEnum.MODEL_RUN_ERROR);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultUtil.error(ResultEnum.MODEL_RUN_ERROR);
    }


    @ApiOperation(value = "获取远程文件的文本内容", notes = "")
    @RequestMapping(value = "/remoteGet", method = RequestMethod.GET)
    public Result getNews(@RequestParam("url") String url) throws Exception {
        try {
            return ResultUtil.success(MyHttpUtils.GET(url, "GBK", null));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    @ApiOperation(value = "获取模型Info", notes = "根据模型id，查询模型Info")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public Result<String> getInfo(@RequestParam("id") String id,
                                  @RequestParam("ip") String ip
    ) throws Exception {
        String url = "http://" + ip + ":" + port + "/modelser/inputdata/json/" + id;
        String result = "";
        try {
            result = MyHttpUtils.GET(url, "utf-8", null);
            return ResultUtil.success(result);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    @ApiOperation(value = "获取模型运行实例", notes = "根据运行实例id，查询模型运行实例")
    @RequestMapping(value = "/instance", method = RequestMethod.GET)
    public Result<String> getInstance(@RequestParam("id") String id,
                                      @RequestParam("ip") String ip
    ) throws Exception {
        String url = "http://" + ip + ":" + port + "/modelserrun/json/" + id;
        String result = "";
        try {
            result = MyHttpUtils.GET(url, "utf-8", null);
            return ResultUtil.success(result);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    @ApiOperation(value = "模型计算", notes = "根据模型id和Input数据的信息，启动模型运算")
    @RequestMapping(value = "/run", method = RequestMethod.GET)
    public Result<String> runModel(@RequestParam("id") String id,
                                   @RequestParam("ip") String ip,
                                   @RequestParam("inputdata") String inputdata
    ) throws Exception {
        String url = "http://" + ip + ":" + port + "/modelser/" + id + "?ac=run&inputdata=" + inputdata;
        String result = "";
        try {
            result = MyHttpUtils.GET(url, "utf-8", null);
            return ResultUtil.success(result);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    @ApiOperation(value = "上传数据到模型容器", notes = "上传数据")
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public Result<String> uploadToRemote(@RequestParam("ip") String ip,
                                         @RequestParam("myfile") MultipartFile file) throws Exception {

        String url = "http://" + ip + ":" + port + "/geodata?type=file";
        String result = "";
        try {
            result = MyHttpUtils.POSTFileToModelContainer(
                    url,
                    "utf-8",
                    MyFileUtils.getFileNameWithoutExtension(file.getOriginalFilename()),
                    MyFileUtils.multipartToFile(file)
            );
            return ResultUtil.success(result);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    @ApiOperation(value = "从模型容器下载数据", notes = "根据数据id下载数据")
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> downloadFromModelContainer(@RequestParam("ip") String ip,
                                                                          @RequestParam("id") String id,
                                                                          @RequestParam("filename") String filename
    ) throws Exception {
        String url = "http://" + ip + ":" + port + "/geodata/" + id;
        try {
            MyResponse res = MyHttpUtils.getMyResponse(url);
            HttpHeaders headers = new HttpHeaders();
            String ex = MyFileUtils.getExtension(res.getContent_Disposition());

            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Content-Disposition", "attachment;filename=" + filename + "." + ex);
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentLength(res.getContentLength())
                    .body(new InputStreamResource(res.getInputStream()));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @ApiOperation(value = "上传json数据(会在后台转化为shapfile)上传到模getData型容器", notes = "上传数据")
    @RequestMapping(value = "/json2shp", method = RequestMethod.POST)
    public Result<String> jsonUploadToModelContainer(@RequestParam("geojsonString") String geojsonString,
                                                     @RequestParam("ip") String ip,
                                                     @RequestParam("projString") String projString) throws Exception {

        String uid = UUID.randomUUID().toString();

        String folderGeojson = ROOT + "dataProcess/localGeojson/" + uid + "/";
        MyFileUtils.mkFolder(folderGeojson);
        String geojsonFile = folderGeojson + "data.geojson";
        MyFileUtils.writeStringToFile(geojsonString, new File(geojsonFile), "utf-8");
        String projFile = folderGeojson + "proj.bat";
        MyFileUtils.writeStringToFile(projString, new File(projFile), "utf-8");

        String folderShap = ROOT + "dataProcess/temp/" + uid + "/";
        MyFileUtils.mkFolder(folderShap);
        String PluginPath = ROOT + "support";
        GeoUtils.geojson2shapefile(geojsonFile, folderShap + "data.shp", projFile, PluginPath);


        //生成配置文件
        File[] files = new File(folderShap).listFiles();

        Document dom = new DOMDocument();
        Element root = dom.addElement("UDXZip");
        Element elementFileName = root.addElement("FileName");
        elementFileName.addAttribute("count", String.valueOf(files.length));
        for (File file : files) {
            Element element1 = elementFileName.addElement("Add");
            element1.addAttribute("value", file.getName());
        }


        XMLWriter xmlWriter = new XMLWriter(MyFileUtils.getFileOutputStream(new File(folderShap + "/configure.udxcfg"), false));
        xmlWriter.write(dom);
        xmlWriter.close();


        String folderOut = ROOT + "dataProcess/localShapZip/" + uid + "/";
        MyFileUtils.mkFolder(folderOut);
        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(
                folderOut + "data.zip"));
        ZipUtils.zipFiles(zipOut, "", new File(folderShap).listFiles());
        zipOut.closeEntry();
        zipOut.close();


        File fileUpload = new File(folderOut + "data.zip");

        String url = "http://" + ip + ":" + port + "/geodata?type=file";

        String result = "";
        try {
            result = MyHttpUtils.POSTFileToModelContainer(
                    url,
                    "utf-8",
                    MyFileUtils.getFileNameWithoutExtension("data.zip"),
                    fileUpload
            );
            return ResultUtil.success(result);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    @ApiOperation(value = "从模型容器获取Shapefile数据(后台会转化为geojson到前台)", notes = "获取数据")
    @RequestMapping(value = "/getData", method = RequestMethod.GET)
    public Result<Kun_geojson> getGeoJSONFromModelContainer(@RequestParam("id") String id, @RequestParam("ip") String ip) throws Exception {
        String url = "http://" + ip + ":" + port + "/geodata/" + id;
        try {
            String uid = UUID.randomUUID().toString();

            String folderZip = ROOT + "dataProcess/localShapZip/" + uid + "/";
            MyFileUtils.mkFolder(folderZip);
            String responseFileName = MyHttpUtils.getMyFile(folderZip, url);
            String PluginPath = ROOT + "support";

            String UnzipDirectory = ROOT + "dataProcess/temp/" + uid + "/";
            MyFileUtils.mkFolder(UnzipDirectory);
            File f = new File(folderZip + responseFileName);
            ZipUtils.unZipFiles(f, UnzipDirectory);

            List<String> shpFileList = MyFileUtils.getShpFileListDir(UnzipDirectory);

            if (shpFileList.size() > 1) {
                List<Kun_geojson> geojsons = new ArrayList<>();
                String folderGejson = ROOT + "dataProcess/localGeojson/" + uid + "/";
                MyFileUtils.mkFolder(folderGejson);
                MyFileUtils.zipFilesInDir(UnzipDirectory, "shp");
                for (int i = 0; i < shpFileList.size(); i++) {
                    String shpFilePath = shpFileList.get(i);
                    String shpFileNameNoExt = shpFilePath.substring(shpFilePath.lastIndexOf('\\') + 1, shpFilePath.lastIndexOf('.'));
                    String geojsonPath = folderGejson + shpFileNameNoExt + ".geojson";
                    GeoUtils.shapefile2geojson(shpFilePath, geojsonPath, PluginPath);
                    Kun_geojson kun_geojson = new Kun_geojson(MyFileUtils.getFileContentByString(new File(geojsonPath), "utf-8"),
                            MyFileUtils.getFileContentByString(new File(MyFileUtils.getProjInDirectory(UnzipDirectory, shpFileNameNoExt)), "utf-8"));
                    String dataPath = "dataProcess/temp/" + uid + "/" + shpFileNameNoExt + ".zip";
                    kun_geojson.setDataPath(dataPath);
                    geojsons.add(kun_geojson);
                }
                return ResultUtil.success(geojsons);
            } else if (shpFileList.size() == 1) {
                String shpFilePath = shpFileList.get(0);
                String folderGejson = ROOT + "dataProcess/localGeojson/" + uid + "/";
                MyFileUtils.mkFolder(folderGejson);
                String geojsonPath = folderGejson + MyFileUtils.getFileNameWithoutExtension(responseFileName) + ".geojson";
                GeoUtils.shapefile2geojson(shpFilePath, geojsonPath, PluginPath);
                Kun_geojson kun_geojson = new Kun_geojson(MyFileUtils.getFileContentByString(new File(geojsonPath), "utf-8"),
                        MyFileUtils.getFileContentByString(new File(MyFileUtils.getProjInDirectory(UnzipDirectory)), "utf-8"));
                String dataPath = "dataProcess/localShapZip/" + uid + "/" + responseFileName;
                kun_geojson.setDataPath(dataPath);
                return ResultUtil.success(kun_geojson);
            } else {
                return ResultUtil.error(ResultEnum.NO_OUTPUT_DATA);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    @ApiOperation(value = "从模型容器获取数据,返回数据的类型给前台", notes = "目前的格式包括tif,sgrd,shp,和unknown")
    @RequestMapping(value = "/getDataType", method = RequestMethod.GET)
    public Result<String> getDataType(@RequestParam(value = "id", required = true) String id, @RequestParam("ip") String ip) throws Exception {

        String url = "http://" + ip + ":" + port + "/geodata/" + id;
        try {
            String uid = UUID.randomUUID().toString();
            String zipPath = ROOT + "dataProcess/dataType/" + uid + "/";
            MyFileUtils.mkFolder(zipPath);
            String responseFileName = MyHttpUtils.getMyFile(zipPath, url);
            String s = getDataExtension(zipPath + responseFileName);
            return ResultUtil.success(s);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @ApiOperation(value = "TIF转Base64 Bmp", notes = "返回的JSON包括投影和六参数")
    @RequestMapping(value = "/getTifInformation", method = RequestMethod.POST)
    public Result<TifInformation> getTifInformation(@RequestParam("colorMapping") String colorMapping,
                                                    @RequestParam("tifFile") MultipartFile tifFile) throws Exception {

        String uid = UUID.randomUUID().toString();
        String basePath = ROOT + "dataProcess/colorMapping/" + uid + "/";
        MyFileUtils.mkFolder(basePath);
        String temPath = ROOT + "dataProcess/temp/" + uid + "/";
        MyFileUtils.mkFolder(temPath);
        String colorBandFilePath = temPath + "colorBand.bat";
        File f = new File(temPath + tifFile.getOriginalFilename());
        MyFileUtils.writeInputStreamToFile(tifFile.getInputStream(), f);
        ZipUtils.unZipFiles(f, temPath);
        String tifFilePath = MyFileUtils.getTifFileInDirectory(temPath);
        String bmpFilePath = basePath + "out_BMP.bmp";
        MyFileUtils.writeStringToFile(colorMapping, new File(colorBandFilePath), "utf-8");
        GeoUtils.colorMapping(tifFilePath, colorBandFilePath, bmpFilePath, ROOT + "support");
        String dataPath = "dataProcess/temp/" + uid + "/" + tifFile.getOriginalFilename();
        TifInformation tifInformation = getTifInformation(basePath);
        if (tifInformation == null) return ResultUtil.error(ResultEnum.NO_PROJ_ERROR);
        tifInformation.setDataPath(dataPath);
        return ResultUtil.success(tifInformation);
    }

    @ApiOperation(value = "SGRD转Base64 Bmp", notes = "返回的JSON包括投影和六参数")
    @RequestMapping(value = "/getSgrdInformation", method = RequestMethod.POST)
    public Result getSgrdInformation(@RequestParam("sgrdFile") MultipartFile sgrdFile) throws IOException, InterruptedException {
        String uid = UUID.randomUUID().toString();
        String temPath = ROOT + "dataProcess/temp/" + uid + "/";
        MyFileUtils.mkFolder(temPath);
        File f = new File(temPath + sgrdFile.getOriginalFilename());
        MyFileUtils.writeInputStreamToFile(sgrdFile.getInputStream(), f);
        ZipUtils.unZipFiles(f, temPath);

        String basePath = ROOT + "dataProcess/colorMapping/" + uid + "/";
        MyFileUtils.mkFolder(basePath);
        String bmpFilePath = basePath + "out_BMP.bmp";
        //调用saga工具将sgrd数据转换为 bmp数据
        boolean bmpIsOk = SagaUtils.sgrdToBmp(temPath, bmpFilePath);
        if (bmpIsOk) {
            String dataPath = "dataProcess/temp/" + uid + "/" + sgrdFile.getOriginalFilename();
            SgrdInformation sgrdInformation = getSgrdInformation(basePath, "out_BMP");
            if (sgrdInformation == null) return ResultUtil.error(ResultEnum.NO_PROJ_ERROR);
            sgrdInformation.setDataPath(dataPath);
            return ResultUtil.success(sgrdInformation);
        } else {
            return ResultUtil.error(ResultEnum.LOAD_DATA_FAILED);
        }
    }

    @RequestMapping(value = "/getTableFile", method = RequestMethod.POST)
    public Result getTableFile(@RequestParam("id") String id, @RequestParam("ip") String ip) throws Exception{
        String url = "http://" + ip + ":" + port + "/geodata/" + id;
        String uid = UUID.randomUUID().toString();

        String basePath = ROOT + "dataProcess/colorMapping/" + uid + "/";
        MyFileUtils.mkFolder(basePath);
        String temPath = ROOT + "dataProcess/temp/" + uid + "/";
        MyFileUtils.mkFolder(temPath);
        //将模型输出结果拷贝至 tmpPath路径下
        String responseFileName = MyHttpUtils.getMyFile(temPath, url);

        File f = new File(temPath + responseFileName);
        ZipUtils.unZipFiles(new File(temPath + responseFileName), temPath);
        String txtPath = MyFileUtils.getSpecificFile(temPath, "txt");
        if("".equals(txtPath)){
            return ResultUtil.error(ResultEnum.NO_OUTPUT_DATA);
        }
        TableInformation tableInfo = MyFileUtils.getTableInfo(txtPath);
        return ResultUtil.success(tableInfo);
    }

    @RequestMapping(value = "/getRemoteSgrdInformation", method = RequestMethod.POST)
    public Result<SgrdInformation> getRemoteSgrdInformation(@RequestParam("id") String id, @RequestParam("ip") String ip) throws Exception {
        String url = "http://" + ip + ":" + port + "/geodata/" + id;
        String uid = UUID.randomUUID().toString();

        String basePath = ROOT + "dataProcess/colorMapping/" + uid + "/";
        MyFileUtils.mkFolder(basePath);
        String temPath = ROOT + "dataProcess/temp/" + uid + "/";
        MyFileUtils.mkFolder(temPath);
        //将模型输出结果拷贝至 tmpPath路径下
        String responseFileName = MyHttpUtils.getMyFile(temPath, url);

        File f = new File(temPath + responseFileName);
        ZipUtils.unZipFiles(new File(temPath + responseFileName), temPath);
        //判断输出结果的文件中是否缺少 sgrd后缀
        MyFileUtils.addExtSgrd(temPath);

        //调用saga工具将sgrd数据转换为 bmp数据
        List<String> bmpPathList = SagaUtils.sgrdListToBmp(temPath, basePath);
        if (bmpPathList.size() > 1) {
            List<SgrdInformation> sgrdInfors = new ArrayList<>();
            MyFileUtils.zipFilesInDir(temPath, "sgrd");
            for (int i = 0; i < bmpPathList.size(); i++) {
                String bmpPath = bmpPathList.get(i);
                String bmpNameNoExt = bmpPath.substring(bmpPath.lastIndexOf("/") + 1, bmpPath.lastIndexOf('.'));
                SgrdInformation sgrdInformation = getSgrdInformation(basePath, bmpNameNoExt);
                String dataPath =  "dataProcess/temp/" + uid + "/" + bmpNameNoExt + ".zip";
                if(sgrdInformation!=null){
                    sgrdInformation.setDataPath(dataPath);
                    sgrdInfors.add(sgrdInformation);
                }
            }
            return ResultUtil.success(sgrdInfors);
        } else if (bmpPathList.size() == 1) {
            String bmpPath = bmpPathList.get(0);
            String bmpNameNoExt = bmpPath.substring(bmpPath.lastIndexOf("/") + 1, bmpPath.lastIndexOf('.'));
            String dataPath = "dataProcess/temp/" + uid + "/" + responseFileName;
            SgrdInformation sgrdInformation = getSgrdInformation(basePath, bmpNameNoExt);
            if (sgrdInformation == null) return ResultUtil.error(ResultEnum.NO_PROJ_ERROR);
            sgrdInformation.setDataPath(dataPath);
            return ResultUtil.success(sgrdInformation);
        } else {
            return ResultUtil.error(ResultEnum.LOAD_DATA_FAILED);
        }
    }


    @ApiOperation(value = "TIF转Base64 Bmp From Reomote", notes = "返回的JSON包括投影和六参数")
    @RequestMapping(value = "/getRemoteTifInformation", method = RequestMethod.POST)
    public Result<TifInformation> getRemoteTifInformation(@RequestParam("colorMapping") String colorMapping,
                                                          @RequestParam("id") String id,
                                                          @RequestParam("ip") String ip) throws Exception {

        String url = "http://" + ip + ":" + port + "/geodata/" + id;
        String uid = UUID.randomUUID().toString();
        String basePath = ROOT + "dataProcess/colorMapping/" + uid + "/";
        MyFileUtils.mkFolder(basePath);
        String temPath = ROOT + "dataProcess/temp/" + uid + "/";
        MyFileUtils.mkFolder(temPath);


        String responseFileName = MyHttpUtils.getMyFile(temPath, url);
        File f = new File(temPath + responseFileName);
        ZipUtils.unZipFiles(f, temPath);

        String tifFilePath = MyFileUtils.getTifFileInDirectory(temPath);

        String colorBandFilePath = temPath + "colorBand.bat";
        MyFileUtils.writeStringToFile(colorMapping, new File(colorBandFilePath), "utf-8");

        String bmpFilePath = basePath + "out_BMP.bmp";
        GeoUtils.colorMapping(tifFilePath, colorBandFilePath, bmpFilePath, ROOT + "support");
        String dataPath = "dataProcess/temp/" + uid + "/" + responseFileName;
        TifInformation tifInformation = getTifInformation(basePath);
        if (tifInformation == null) return ResultUtil.error(ResultEnum.NO_PROJ_ERROR);
        tifInformation.setDataPath(dataPath);
        return ResultUtil.success(tifInformation);
    }


    @ApiOperation(value = "上传shp数据到后台，返回geoJSON以在前台显示", notes = "若本身为geojson不做操作，" +
            "若为shapefile则进行转化")
    @RequestMapping(value = "/shp2json", method = RequestMethod.POST)
    public Result<String> shap2jsonInLocal(@RequestParam("shpfile") MultipartFile file) {
        String suffix = MyFileUtils.getExtension(file.getOriginalFilename());
        if (suffix.equals("geojson")) {
            try {
                return ResultUtil.success(MyIoUtils.inputStreamToString(file.getInputStream()));
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        } else if (suffix.equals("zip"))//shapefile格式
        {
            String uid = UUID.randomUUID().toString();
            String basePath = ROOT + "dataProcess/localShapZip/" + uid + "/";
            MyFileUtils.mkFolder(basePath);

            String PluginPath = ROOT + "support";
            String UnzipDirectory = ROOT + "dataProcess/temp/" + uid + "/";
            MyFileUtils.mkFolder(UnzipDirectory);

            String zipPath = basePath + file.getOriginalFilename();
            File fileZip = new File(zipPath);
            try {
                MyFileUtils.writeInputStreamToFile(file.getInputStream(), fileZip);
                ZipUtils.unZipFiles(fileZip, UnzipDirectory);
                String ShpFileName = MyFileUtils.getShpFileInDirectory(UnzipDirectory);
                String name = file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf("."));
                String geojsonPath = ROOT + "dataProcess/localGeojson/" + uid + "/";
                MyFileUtils.mkFolder(geojsonPath);
                String geojsonFile = geojsonPath + name + ".geojson";
                GeoUtils.shapefile2geojson(ShpFileName, geojsonFile, PluginPath);
                //转出来的geojson 一般都是gbk 编码的
                Kun_geojson kun_geojson = new Kun_geojson(MyFileUtils.getFileContentByString(new File(geojsonFile), "gbk"),
                        MyFileUtils.getFileContentByString(new File(MyFileUtils.getProjInDirectory(UnzipDirectory)), "gbk"));
                System.out.println(kun_geojson.getGeojson());
                String dataPath = "dataProcess/localShapZip/" + uid + "/" + file.getOriginalFilename();
                kun_geojson.setDataPath(dataPath);
                return ResultUtil.success(kun_geojson);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        } else {
            return ResultUtil.error(-1, "目前仅支持Zip包格式的Shap数据和Geojson数据");
        }
    }


    /*
        读取Zip文件中的文件，确定数据的类型sh'p
        目前支持tif,shp的判定。
     */
    public String getDataExtension(String fileName) throws IOException {
        if (MyFileUtils.getExtension(fileName).equals("zip")) {
            File file = new File(fileName);
            ZipFile zf = new ZipFile(file);
            Enumeration items = zf.entries();
            String ext = "";
            while (items.hasMoreElements()) {
                ZipEntry item = (ZipEntry) items.nextElement();
                String extension = MyFileUtils.getExtension(item.getName()).toLowerCase();
                if (extension.equals("tif")) {
                    zf.close();
                    return "tif";
                } else if (extension.equals("shp")) {
                    zf.close();
                    return "shp";
                } else if (extension.equals("sgrd")) {
                    zf.close();
                    return "sgrd";
                } else if(extension.equals("mgrd")){
                    return "sgrd";
                } else if(extension.equals("txt")){
                    return "txt";
                }
            }
            zf.close();
            return "UNKNOWN";
        } else {
            return "UNKNOWN";
        }
    }


    /**
     * 获取当前路径下的sgrd文件转换成bmp文件后的信息：
     * 1.bmp文件路径
     * 2.prj文件获取投影信息
     * 3.bpw文件获取范围信息
     *
     * @param path
     * @return
     */
    public SgrdInformation getSgrdInformation(String path, String fileNameNoExt) throws InterruptedException {
        SgrdInformation sgrd = new SgrdInformation();
        File file = new File(path);
        File[] files = file.listFiles();
        //如果文件还没生成，则
        int time = 0;
        boolean dataNotReady = true;
        while (dataNotReady && time <= 5) {
            for (File fileItem : files) {
                //获取fileItem 不带后缀的文件名：
                String fileItemName = fileItem.getName();
                String fileItemNameNoExt = fileItemName.substring(0, fileItemName.lastIndexOf('.'));
                if (fileNameNoExt.equals(fileItemNameNoExt)) {
                    dataNotReady =false;
                    String extension = MyFileUtils.getExtension(fileItem.getName());
                    if (extension.equals("bmp")) {
//                        sgrd.setFilePosition(fileItem.getAbsolutePath().substring(ROOT.length() - 1));
                        sgrd.setFilePosition(fileItem.getAbsolutePath().substring(2));
                    } else if (extension.equals("prj")) {
                        String prj = MyFileUtils.readToString(fileItem.getAbsolutePath())[0];
                        if (null == prj || prj.isEmpty()) return null;
                        sgrd.setSRS(prj);
                    } else if (extension.equals("bpw")) {
                        String[] scopes = MyFileUtils.readToString(fileItem.getAbsolutePath());
                        String scope = "";
                        if (scopes.length >= 6) {
                            scope = scopes[4] + "," + scopes[0] + "," + scopes[2] + "," + scopes[5] + "," + scopes[1] + "," + scopes[3];
                            sgrd.setGeoTransform(scope);
                        } else {
                            return null;
                        }
                    }
                }
            }
            if(dataNotReady){
                Thread.sleep(1000);
                time++;
                files = file.listFiles();
            }
        }
        return sgrd;
    }

    /*
        获取当前文件下所有文件，根据后缀bmp，xml 返回TifInformation
     */
    public TifInformation getTifInformation(String Path) throws IOException, DocumentException {
        TifInformation tifInformation = new TifInformation();
        File file = new File(Path);
        File[] files = file.listFiles();

        for (File fileItem : files) {
            String extension = MyFileUtils.getExtension(fileItem.getName());
            if (extension.equals("bmp")) {
//                tifInformation.setFilePosition(fileItem.getAbsolutePath().substring(ROOT.length() - 1));
                tifInformation.setFilePosition(fileItem.getAbsolutePath().substring(2));
            } else if (extension.equals("xml")) {
                Document doc = XmlUtils.parseFile(fileItem);
                Element rootElement = doc.getRootElement();
                Element SRSelement = rootElement.element("SRS");
                Element GeoTransformelement = rootElement.element("GeoTransform");
                if (SRSelement == null || GeoTransformelement == null) return null;
                tifInformation.setSRS(SRSelement.getText());
                tifInformation.setGeoTransform(GeoTransformelement.getText());
            } else {

            }
        }
        return tifInformation;
    }


}
