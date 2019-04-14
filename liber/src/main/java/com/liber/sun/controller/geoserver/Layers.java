package com.liber.sun.controller.geoserver;

import com.liber.sun.domain.Result;

import com.liber.sun.utils.MyHttpUtils;
import com.liber.sun.utils.ResultUtil;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.net.URL;

/**
 * Created by sunlingzhi on 2017/12/21.
 */
@RestController
@RequestMapping("/geoserver")
public class Layers {


    @Value("${port.geoserver}")
    private String port;
    @Value("${port.geoserver.admin}")
    private String admin;
    @Value("${port.geoserver.password}")
    private String password;

    @ApiOperation(value = "List all layers", notes = "")
    @RequestMapping(value = "/layers", method = RequestMethod.GET)
    public Result<String> listLayers(@RequestParam("ip") String ip) throws Exception {
        String url="http://" + ip + ":" + port + "/geoserver/rest/layers.json";
        String responseContent= MyHttpUtils.GET(url,"utf-8",null,admin,password);
        return ResultUtil.success(responseContent);
    }


    @ApiOperation(value = "get a certain layer store", notes = "")
    @RequestMapping(value = "/layers/{layerName}", method = RequestMethod.GET)
    public Result<String> listLayer(@PathVariable("layerName") String layerName,
                                        @RequestParam("ip") String ip) throws Exception {
        String url="http://" + ip + ":" + port + "/geoserver/rest/layers/"+layerName+".json";
        String responseContent=MyHttpUtils.GET(url,"utf-8",null,admin,password);
        return ResultUtil.success(responseContent);
    }
}
