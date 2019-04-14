package com.liber.sun.controller.geoserver;

import com.liber.sun.domain.Result;

import com.liber.sun.utils.MyFileUtils;
import com.liber.sun.utils.MyHttpUtils;
import com.liber.sun.utils.ResultUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;

/**
 * Created by sunlingzhi on 2017/12/18.
 */
@RestController
@RequestMapping("/geoserver")
public class Coverages {

    @Value("${port.geoserver}")
    private String port;
    @Value("${port.geoserver.admin}")
    private String admin;
    @Value("${port.geoserver.password}")
    private String password;


    @ApiOperation(value = "Controls all coverage stores in a given workspace", notes = "")
    @RequestMapping(value = "/workspaces/{workspaceName}/coveragestores", method = RequestMethod.GET)
    public Result<String> listCoverageStores(@PathVariable("workspaceName") String workspaceName,
                                             @RequestParam("ip") String ip) throws Exception {
        String url = "http://" + ip + ":" + port + "/geoserver/rest/workspaces/" + workspaceName + "/coveragestores.json";
        String responseContent = MyHttpUtils.GET(url, "utf-8", null, admin, password);
        return ResultUtil.success(responseContent);
    }

    @ApiOperation(value = "Controls a particular coverage store in a given workspace.", notes = "")
    @RequestMapping(value = "/workspaces/{workspaceName}/coveragestores/{store}", method = RequestMethod.GET)
    public Result<String> listCoverageStore(@PathVariable("workspaceName") String workspaceName,
                                            @PathVariable("coveragestoresName") String coveragestoresName,
                                            @RequestParam("ip") String ip) throws Exception {
        String url = "http://" + ip + ":" + port + "/geoserver/rest/workspaces/" + workspaceName + "/coveragestores/"
                + coveragestoresName
                + ".json";
        String responseContent = MyHttpUtils.GET(url, "utf-8", null, admin, password);
        return ResultUtil.success(responseContent);
    }


    @ApiOperation(value = "Delete the coverage in coverageStore", notes = "recurse=true,会强制删除")
    @RequestMapping(value = "/workspaces/{workspaceName}/coveragestores/{store}/coveragers/{coverage}", method = RequestMethod.DELETE)
    public Result<String> deleteCoverageStore(@PathVariable("workspaceName") String workspaceName,
                                              @PathVariable("coveragestoresName") String coveragestoresName,
                                              @PathVariable("coverage") String coverage,
                                              @RequestParam("ip") String ip) throws Exception {
        String url = "http://" + ip + ":" + port + "/geoserver/rest/workspaces/" + workspaceName + "/coveragestores/" +
                coveragestoresName + "/coverages/" + coverage + "?recurse=true";
        String responseContent = MyHttpUtils.DELETE(url, "utf-8", null, admin, password);
        if (responseContent.equals("")) {//成功
            return ResultUtil.success("delete " + coverage + " finish!");
        } else {
            return ResultUtil.success(responseContent);
        }
    }

    @ApiOperation(value = "Delete the coverageStore", notes = "recurse=true,会强制删除")
    @RequestMapping(value = "/workspaces/{workspaceName}/coveragestores/{store}", method = RequestMethod.DELETE)
    public Result<String> deleteCoverageStore(@PathVariable("workspaceName") String workspaceName,
                                              @PathVariable("coveragestoresName") String coveragestoresName,
                                              @RequestParam("ip") String ip) throws Exception {
        String url = "http://" + ip + ":" + port + "/geoserver/rest/workspaces/" + workspaceName + "/coveragestores/" +
                coveragestoresName + "?recurse=true";
        String responseContent = MyHttpUtils.DELETE(url, "utf-8", null, admin, password);
        if (responseContent.equals("")) {//成功
            return ResultUtil.success("delete " + coveragestoresName + " finish!");
        } else {
            return ResultUtil.success(responseContent);
        }
    }


    @ApiOperation(value = "Creates or modifies a single data store by uploading", notes = "注意这里后台要使用Put请求")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "format", value = "可为geotiff,worldinage,imagemosaic三种方式,推荐使用geotiff,目前后台写死geotiff\n" +
                    "geotiff GeoTIFF\n" +
                    "worldinage Georeferenced image (JPEG, PNG, TIFF)\n" +
                    "imagemosaic Image mosaic", required = true,
                    dataType = "String", paramType = "Path"),
    })
    @RequestMapping(value = "/workspaces/{workspaceName}/coveragestores/{store}/file.{format}", method = RequestMethod.POST)
    public Result<String> createDataStoresByUpload(@PathVariable("workspaceName") String workspaceName,
                                                   @PathVariable("coveragestoresName") String coveragestoresName,
                                                   @PathVariable("format") String format,
                                                   @RequestParam("ip") String ip,
                                                   @RequestParam("file") MultipartFile file) throws Exception {
        String url = "http://" + ip + ":" + port + "/geoserver/rest/workspaces/" + workspaceName + "/coveragestores/" + coveragestoresName
                + "/file.geotiff" ;
        File fileTemp = MyFileUtils.multipartToFile(file);
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("connection", "keep-alive");
        headerMap.put("Content-Type", "application/zip");
        String responseContet = MyHttpUtils.PUTRawFile(url, "utf-8", headerMap, fileTemp, admin, password);
        return ResultUtil.success(responseContet);
    }


}
