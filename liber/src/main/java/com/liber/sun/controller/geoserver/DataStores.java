package com.liber.sun.controller.geoserver;

import com.liber.sun.domain.Result;

import com.liber.sun.utils.MyFileUtils;
import com.liber.sun.utils.MyHttpUtils;
import com.liber.sun.utils.ResultUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.URL;
import java.util.HashMap;


/**
 * Created by sunlingzhi on 2017/12/11.
 */
@RestController
@RequestMapping("/geoserver")
public class DataStores {



    @Value("${port.geoserver}")
    private String port;
    @Value("${port.geoserver.admin}")
    private String admin;
    @Value("${port.geoserver.password}")
    private String password;

    @ApiOperation(value = "List all data stores in certain workspaces", notes = "")
    @RequestMapping(value = "/workspaces/{workspaceName}/datastores", method = RequestMethod.GET)
    public Result<String> listDataStores(@PathVariable("workspaceName") String workspaceName,
                                         @RequestParam("ip") String ip) throws Exception {
        String url="http://" + ip + ":" + port + "/geoserver/rest/workspaces/" + workspaceName + "/datastores"
                + ".json";
        String responseContent= MyHttpUtils.GET(url,"utf-8",null,admin,password);
        return ResultUtil.success(responseContent);
    }


    @ApiOperation(value = "get a certain data store", notes = "")
    @RequestMapping(value = "/workspaces/{workspaceName}/datastores/{datastoreName}", method = RequestMethod.GET)
    public Result<String> listDataStore(@PathVariable("workspaceName") String workspaceName,
                                        @PathVariable("datastoreName") String datastoreName,
                                        @RequestParam("ip") String ip) throws Exception {
        String url="http://" + ip + ":" + port + "/geoserver/rest/workspaces/" + workspaceName + "/datastores/"
                + datastoreName
                + ".json";
        String responseContent=MyHttpUtils.GET(url,"utf-8",null,admin,password);
        return ResultUtil.success(responseContent);
    }

    @ApiOperation(value = "get feature type", notes = "")
    @RequestMapping(value = "/workspaces/{workspaceName}/datastores/{datastoreName}/featuretupes", method = RequestMethod.GET)
    public Result<String> getFeatureTypes(@PathVariable("workspaceName") String workspaceName,
                                        @PathVariable("datastoreName") String datastoreName,
                                        @RequestParam("ip") String ip) throws Exception {
        String url="http://" + ip + ":" + port + "/geoserver/rest/workspaces/" + workspaceName + "/datastores/"
                + datastoreName+"/feature.json";
        String responseContent=MyHttpUtils.GET(url,"utf-8",null,admin,password);
        return ResultUtil.success(responseContent);
    }

    @ApiOperation(value = "get feature type", notes = "")
    @RequestMapping(value = "/workspaces/{workspaceName}/datastores/{datastoreName}/featuretupes/{featuretypeName}", method = RequestMethod.GET)
    public Result<String> getFeatureType(@PathVariable("workspaceName") String workspaceName,
                                          @PathVariable("datastoreName") String datastoreName,
                                          @PathVariable("featuretypeName") String featuretypeName,
                                          @RequestParam("ip") String ip) throws Exception {
        String url="http://" + ip + ":" + port + "/geoserver/rest/workspaces/" + workspaceName + "/datastores/"
                + datastoreName+"/featuretypes/"+featuretypeName+".json";
        String responseContent=MyHttpUtils.GET(url,"utf-8",null,admin,password);
        return ResultUtil.success(responseContent);
    }


    @ApiOperation(value = "Delete the data store", notes = "如果store中还包含这feature的话，会返回403")
    @RequestMapping(value = "/workspaces/{workspaceName}/datastores/{datastoreName}", method = RequestMethod.DELETE)
    public Result<String> deleteDataStore(@PathVariable("workspaceName") String workspaceName,
                                          @PathVariable("datastoreName") String datastoreName,
                                          @RequestParam("ip") String ip) throws Exception {
        String url="http://" + ip + ":" + port + "/geoserver/rest/workspaces/" + workspaceName + "/datastores/" +
                datastoreName+"?recurse=true";
        String responseContent = MyHttpUtils.DELETE(url, "utf-8", null, admin, password);
        if(responseContent.equals("")){//成功
            return ResultUtil.success("delete "+datastoreName+" finish!");
        }else{
            return ResultUtil.success(responseContent);
        }
    }


    @ApiOperation(value = "Delete featureType", notes = "")
    @RequestMapping(value = "/workspaces/{workspaceName}/datastores/{datastoreName}/featuretypes/{featuretypeName}", method = RequestMethod.DELETE)
    public Result<String> deleteFeatureType(@PathVariable("workspaceName") String workspaceName,
                                          @PathVariable("datastoreName") String datastoreName,
                                            @PathVariable("featuretypeName") String featuretypeName,
                                          @RequestParam("ip") String ip) throws Exception {
        String url="http://" + ip + ":" + port + "/geoserver/rest/workspaces/" + workspaceName + "/datastores/" +
                datastoreName+"/featuretypes/"+featuretypeName+"?recurse=true";
        String responseContent = MyHttpUtils.DELETE(url, "utf-8", null, admin, password);
        if(responseContent.equals("")){//成功
            return ResultUtil.success("delete "+datastoreName+" finish!");
        }else{
            return ResultUtil.success(responseContent);
        }
    }




    @ApiOperation(value = "Creates or modifies a single data store by uploading,注意是geoserver接受的是PUT", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "method", value = "可为url,file,external三种查询方式,建议使用file,后台目前写死为file\n" +
                    "file 从本地上传文件,body里面就是file本身\n" +
                    "url 从remote source上传.body里面包含URL指向远处资源\n" +
                    "external使用服务已存在的资源，body里面是资源的绝对路径 file:/path/to/nyc.shp", required = true,
                    dataType = "String", paramType = "Path"),
    })
    @RequestMapping(value = "/workspaces/{workspaceName}/datastores/{datastore}/{method}.shp", method = RequestMethod.POST)
    public Result<String> createDataStoresByUpload(@PathVariable("workspaceName") String workspaceName,
                                                   @PathVariable("datastore") String datastore,
                                                   @PathVariable("method") String method,
                                                   @RequestParam("ip") String ip,
                                                   @RequestParam("file") MultipartFile file) throws Exception {
        String url="http://" + ip + ":" + port + "/geoserver/rest/workspaces/" + workspaceName + "/datastores/"+datastore
                +"/"+"file.shp";
        File fileTemp = MyFileUtils.multipartToFile(file);
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("connection", "keep-alive");
        headerMap.put("Content-Type", "application/zip");//也可以通过file.getContentType来获取
        String responseContet = MyHttpUtils.PUTRawFile(url, "utf-8", headerMap, fileTemp, admin, password);
        return ResultUtil.success(responseContet);
    }





}
