package com.liber.sun.controller;


import com.liber.sun.domain.Result;

import com.liber.sun.domain.TifInformation;
import com.liber.sun.utils.MyFileUtils;
import com.liber.sun.utils.ResultUtil;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.InputStream;


/**
 * Created by sunlingzhi on 2017/12/18.
 */
@RestController //默认返回json格式
@RequestMapping("/clean")
public class CleanController {

    @Value("${web.upload-path}")
    private String ROOT;


    @ApiOperation(value = "clean数据存储", notes = "/clean/all" )
    @RequestMapping(value = "/{collection}", method = RequestMethod.GET)
    public Result<String> clean(@PathVariable(value = "collection") String collection) throws Exception {
            if(collection.equals("all"))
            {
                MyFileUtils.deleteFiles(ROOT + "dataProcess/temp", true);
                MyFileUtils.deleteFiles(ROOT + "dataProcess/localGeojson", true);
                MyFileUtils.deleteFiles(ROOT + "dataProcess/localShapZip", true);
                MyFileUtils.deleteFiles(ROOT + "dataProcess/dataType", true);
                MyFileUtils.deleteFiles(ROOT + "dataProcess/colorMapping", true);
                return ResultUtil.success("Clean success!");
            }else{
                return ResultUtil.success("Path must be /clean/all");
            }
    }
}
