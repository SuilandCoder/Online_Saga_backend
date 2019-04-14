package com.liber.sun.service;

import com.liber.sun.dao.ToolRecordRepository;
import com.liber.sun.domain.ToolRecord;
import com.liber.sun.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by SongJie on 2019/3/26 22:47
 */
@Service
public class ToolRecordService {
    @Autowired
    ToolRecordRepository toolRecordRepository;

    //添加
    public ToolRecord addToolRecord(ToolRecord toolRecord){
        return toolRecordRepository.insert(toolRecord);
    }

    //更新
    public ToolRecord updateToolRecord(ToolRecord toolRecord){
        return toolRecordRepository.save(toolRecord);
    }

    //删除
    public void deleteToolRecord(String id) {
        toolRecordRepository.delete(id);
    }

    //查询
    public List<ToolRecord> getToolRecordList() {
        return toolRecordRepository.findAll();
    }

    public ToolRecord getToolRecordById(String id)
    {
        return  toolRecordRepository.findOne(id);
    }

    public ToolRecord getToolRecordByRecordId(String recordId){
        return toolRecordRepository.findByRecordId(recordId);
    }

    public List<ToolRecord> getToolRecordByUserId(String userId){
        return toolRecordRepository.findByUserId(userId);
    }

}
