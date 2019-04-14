package com.liber.sun.dao;

import com.liber.sun.domain.ModelItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by sunlingzhi on 2017/11/1.
 */
public interface ModelItemRepository extends MongoRepository<ModelItem, String> {
    public ModelItem findByModelId(String modelId);
    public ModelItem findByValue(String value);
    public List<ModelItem> findByParentId(String parentId);
}
