package com.liber.sun.controller;

import com.liber.sun.dao.Kun_SettingRepository;
import com.liber.sun.domain.Kun_Setting;
import com.liber.sun.domain.Result;
import com.liber.sun.enums.ResultEnum;
import com.liber.sun.exception.MyException;

import com.liber.sun.utils.ResultUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by sunlingzhi on 2017/11/24.
 */
@RestController
@RequestMapping("/setting")
public class Kun_SettingController {
    @Autowired
    private Kun_SettingRepository kun_settingRepository;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "queryCondition", value = "可为all,name,id三种查询方式", required = true,
                    dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "value", value = "", required = true, dataType = "String",
                    paramType = "query")
    })
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result getSettingByQuery(@RequestParam("queryCondition") String queryCondition,
                                    @RequestParam("value") String value) {
        if (queryCondition.equals("all")) {
            return ResultUtil.success(kun_settingRepository.findAll());
        } else if (queryCondition.equals("id")) {
            return ResultUtil.success(kun_settingRepository.findOne(value));
        } else if (queryCondition.equals("name")) {
            return ResultUtil.success(kun_settingRepository.findKun_SettingsByName(value));
        } else {
            throw new MyException(ResultEnum.NO_QUERY_CONDITION_ERROR);
        }
    }


    @ApiOperation(value = "创建", notes = "根据Kun_Setting对象创建,建议不设置id字段，由后台生成，避免出现id重复问题")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Result<Kun_Setting> addModelItem(Kun_Setting kun_setting) {
        Kun_Setting item = kun_settingRepository.insert(kun_setting);
        return ResultUtil.success(item);
    }

    @ApiOperation(value = "删除", notes = "根据id字段删除")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Result<String> deleteModelItemById(@RequestParam("id") String id) {
        kun_settingRepository.delete(id);
        return ResultUtil.success("id: " + id + "删除成功");
    }

    //如果使用PUT请求，需要在前端设置Body为x-www-form-urlencoded，HTTP的格式要求
    //为了方便，使用POST请求
    // 对于Post请求，无论是在query中设置还是在body里面设置，都是可以生成对应的实体类的
    @ApiOperation(value = "更新", notes = "根据ModelItem对象更新,要设置id字段，\n" +
            "如果使用PUT请求，需要在前端设置Body为x-www-form-urlencoded，HTTP的格式要求\n" +
            "为了方便，使用POST请求\n" +
            "另外对于Post请求，无论是在query中设置还是在body里面设置，都是可以生成对应的实体类的")
    @ApiImplicitParam(name = "id", value = "", required = true, dataType = "String")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Result<Kun_Setting> updateModelItem(Kun_Setting kun_setting) {
        return ResultUtil.success(kun_settingRepository.save(kun_setting));
    }


}
