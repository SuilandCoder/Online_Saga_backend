package com.liber.sun.dao;

import com.liber.sun.domain.ToolRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by SongJie on 2019/3/26 22:45
 */
public interface ToolRecordRepository extends MongoRepository<ToolRecord, String> {
    List<ToolRecord> findByUserId(String userId);
    ToolRecord findByRecordId(String recordId);
}
