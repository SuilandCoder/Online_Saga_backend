package com.liber.sun.dao;

import com.liber.sun.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by SongJie on 2019/3/8 15:32
 */
public interface UserRepository extends MongoRepository<User, String> {
    User findByUserId(String userId);
}
