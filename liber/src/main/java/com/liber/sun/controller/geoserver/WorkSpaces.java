package com.liber.sun.controller.geoserver;

import com.liber.sun.domain.Result;

import com.liber.sun.utils.MyHttpUtils;
import com.liber.sun.utils.ResultUtil;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;

/**
 * Created by sunlingzhi on 2017/12/11.
 */
@RestController
@RequestMapping("/geoserver")
public class WorkSpaces {

    @Value("${port.geoserver}")
    private String port;

    @Value("${port.geoserver.admin}")
    private String admin;


    @Value("${port.geoserver.password}")
    private String password;


    //JSON格式如下
//    {
//        "workspace": { "name": "sunlinzhi" }
//    }

    /******************************************/
    @ApiOperation(value="Get a list of workspaces", notes="返回json格式")
    @RequestMapping(value = "/workspaces", method = RequestMethod.GET)
    public Result<String> listWorkSpaces(@RequestParam("ip") String ip) throws Exception {
        String url="http://" + ip + ":"+port+"/geoserver/rest/workspaces.json";
        String responseString= MyHttpUtils.GET(url,"utf-8",null,admin,password);
        return ResultUtil.success(responseString);
    }


    /******************************************/
    @ApiOperation(value="Get the detail of certain workspace", notes="")
    @RequestMapping(value = "/workspaces/{workspaceName}", method = RequestMethod.GET)
    public Result<String> listWorkSpace(@PathVariable String workspaceName,
                                        @RequestParam String ip) throws Exception {
        String url="http://" + ip + ":"+port+"/geoserver/rest/workspaces/"+workspaceName+".json";
        String responseContent=MyHttpUtils.GET(url,"utf-8",null,admin,password);
        return ResultUtil.success(responseContent);
    }


    /******************************************/
    @ApiOperation(value="Add a new workspace to server", notes="")
    @RequestMapping(value = "/workspaces", method = RequestMethod.POST)
    public Result<String> createWorkSpaces(@RequestParam("ip") String ip,@RequestParam("newWorkSpaceName") String workspaceName) throws Exception {
        String jsonString="{" +
                "\"workspace\":{" +
                "\"name\":\""+workspaceName+"\""+
                "}"+
                "}";
        HashMap<String,String> headerMap=new HashMap<>();
        headerMap.put("connection", "keep-alive");
        headerMap.put("Content-type", "application/json");
        String returnContent=MyHttpUtils.POSTRawString("http://" + ip + ":"+port+"/geoserver/rest/workspaces","utf-8",
                headerMap,jsonString,admin,password );
        if(!returnContent.equals("")){
            return ResultUtil.success(returnContent);
        }else{//为空的时候成功
            return ResultUtil.success("Add workSpace "+workspaceName+" success");
        }
    }


    /******************************************/
    @ApiOperation(value="delete workspace", notes="recurse=true,会强制删除workSpace，不管里面是否包含内容！")
    @RequestMapping(value = "/workspaces/{workspaceName}", method = RequestMethod.DELETE)
    public Result<String> deleteWorkSpace(@PathVariable String workspaceName,
                                          @RequestParam String ip) throws Exception {
        String url="http://" + ip + ":"+port+"/geoserver/rest/workspaces/"+workspaceName+"?recurse=true";
        String responseContent=MyHttpUtils.DELETE(url,"utf-8",null,admin,password);
        if(!responseContent.equals("")){
            return ResultUtil.success(responseContent);
        }else{//为空的时候成功
            return ResultUtil.success("Delete workSpace "+workspaceName+" success");
        }
    }



}
