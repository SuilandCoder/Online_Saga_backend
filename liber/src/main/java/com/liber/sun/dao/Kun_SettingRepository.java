package com.liber.sun.dao;

import com.liber.sun.domain.Kun_Setting;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by sunlingzhi on 2017/11/24.
 */
public interface Kun_SettingRepository extends MongoRepository<Kun_Setting,String> {

    public List<Kun_Setting> findKun_SettingsByName(String name);
}
