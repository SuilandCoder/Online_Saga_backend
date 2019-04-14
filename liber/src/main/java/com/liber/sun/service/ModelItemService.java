package com.liber.sun.service;

import com.liber.sun.dao.ModelItemRepository;
import com.liber.sun.domain.ModelItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by sunlingzhi on 2017/11/1.
 */

//使用接口，实现dao层
@Service
public class ModelItemService {

    @Autowired
    private ModelItemRepository modelItemRepository;

    //添加
    public ModelItem addModelItem(ModelItem modelItem){
        return modelItemRepository.insert(modelItem);
    }

    //更新
    public ModelItem updateModelItem(ModelItem modelItem){
        return modelItemRepository.save(modelItem);
    }

    //删除
    public void deleteModelItem(String id) {
        modelItemRepository.delete(id);
    }


   //查询
    public List<ModelItem> getModelItemList() {
        return modelItemRepository.findAll();
    }

    public ModelItem getModelItemByModelId(String modelId) {
        return modelItemRepository.findByModelId(modelId);
    }

    public ModelItem getModelItemByValue(String value){
        return  modelItemRepository.findByValue(value);
    }

    public ModelItem getModelItemById(String id)
    {
        return  modelItemRepository.findOne(id);
    }



    public List<ModelItem> getModelItemListByParentId(String parentId) {
        return modelItemRepository.findByParentId(parentId);
    }


}
