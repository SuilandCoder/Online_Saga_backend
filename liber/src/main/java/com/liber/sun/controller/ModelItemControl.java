package com.liber.sun.controller;

import com.liber.sun.domain.ModelItem;
import com.liber.sun.domain.Result;
import com.liber.sun.enums.ResultEnum;
import com.liber.sun.exception.MyException;
import com.liber.sun.service.ModelItemService;
import com.liber.sun.utils.ResultUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by sunlingzhi on 2017/11/1.
 */
@RestController //默认返回json格式
@RequestMapping("/modelItem")
public class ModelItemControl {
    @Autowired
    private ModelItemService modelItemService;


    @ApiImplicitParams({
            @ApiImplicitParam(name = "queryCondition", value = "可为all,parentId,value,id,modelId五种查询方式", required = true,
                    dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "value", value = "", required = true, dataType = "String",
                    paramType = "query")
    })
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result getModelItemListByQuery(@RequestParam("queryCondition") String queryCondition,
                                          @RequestParam("value") String value) {
        if (queryCondition.equals("all")) {
            return ResultUtil.success(modelItemService.getModelItemList());
        } else if (queryCondition.equals("parentId")) {
            return ResultUtil.success(modelItemService.getModelItemListByParentId(value));
        } else if (queryCondition.equals("value")) {
            return ResultUtil.success(modelItemService.getModelItemByValue(value));
        } else if (queryCondition.equals("id")) {
            return ResultUtil.success(modelItemService.getModelItemById(value));
        } else if (queryCondition.equals("modelId")) {
            return ResultUtil.success(modelItemService.getModelItemByModelId(value));
        } else {
            throw new MyException(ResultEnum.NO_QUERY_CONDITION_ERROR);
        }
    }

    @ApiOperation(value = "创建", notes = "根据ModelItem对象创建,不设置id字段")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Result<ModelItem> addModelItem(ModelItem modelItem) {
        ModelItem item = modelItemService.addModelItem(modelItem);
        return ResultUtil.success(item);
    }


    //如果使用PUT请求，需要在前端设置Body为x-www-form-urlencoded，HTTP的格式要求
    //为了方便，使用POST请求
    // 对于Post请求，无论是在query中设置还是在body里面设置，都是可以生成对应的实体类的
    @ApiOperation(value = "更新", notes = "根据ModelItem对象更新,要设置id字段，\n" +
            "如果使用PUT请求，需要在前端设置Body为x-www-form-urlencoded，HTTP的格式要求\n" +
            "为了方便，使用POST请求\n" +
            " 对于Post请求，无论是在query中设置还是在body里面设置，都是可以生成对应的实体类的")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Result<ModelItem> updateModelItem(ModelItem modelItem) {
        ModelItem item = modelItemService.updateModelItem(modelItem);
        return ResultUtil.success(item);
    }

    @ApiOperation(value = "删除", notes = "根据id字段删除，其子节点的parentId修改为NO_PARENT！")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Result<String> deleteModelItemById(@RequestParam("id") String id) {
        List<ModelItem> children = modelItemService.getModelItemListByParentId(id);
        children.forEach(i -> {
            i.setParentId("NO_PARENT");
            modelItemService.updateModelItem(i);
        });
        modelItemService.deleteModelItem(id);
        return ResultUtil.success("id: " + id + "删除成功");
    }
}
