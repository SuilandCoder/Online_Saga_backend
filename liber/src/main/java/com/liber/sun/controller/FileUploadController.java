package com.liber.sun.controller;

import com.liber.sun.domain.Result;
import com.liber.sun.utils.MyFileUtils;
import com.liber.sun.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by sunlingzhi on 2017/10/22.
 */
@RestController //默认返回json格式
@RequestMapping("/upload")
public class FileUploadController {
    @Value("${web.upload-path}")
    private String ROOT;

    /**
     * 单文件上传
     */
    @RequestMapping(value = "/single", method = RequestMethod.POST)
    public Result<String> uploadSingle(@RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                InputStream is = file.getInputStream();
                MyFileUtils.writeInputStreamToFile(is, new File(ROOT + "fileupload/" + file.getOriginalFilename()));
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
            return ResultUtil.success(file.getOriginalFilename());
        } else {
            return ResultUtil.error(-1, "文件为空");
        }
    }

    /**
     * 多文件上传 主要是使用了MultipartHttpServletRequest和MultipartFile
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/multi", method = RequestMethod.POST)
    public Result<String> uploadMulti(HttpServletRequest request) {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        MultipartFile file = null;
        for (int i = 0; i < files.size(); i++) {
            file = files.get(i);
            if (!file.isEmpty()) {
                try {
                    InputStream is = file.getInputStream();
                    MyFileUtils.writeInputStreamToFile(is, new File(ROOT + "fileupload/" + file.getOriginalFilename()));
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            } else {
                return ResultUtil.error(-1, "文件" + i + "为空");
            }
        }
        return ResultUtil.success("上传成功");

    }




}
