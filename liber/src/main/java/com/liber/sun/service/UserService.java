package com.liber.sun.service;

import com.liber.sun.dao.ModelItemRepository;
import com.liber.sun.dao.UserRepository;
import com.liber.sun.domain.ModelItem;
import com.liber.sun.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by SongJie on 2019/3/8 22:42
 */
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    //添加
    public User addUser(User user){
        return userRepository.insert(user);
    }

    //更新
    public User updateUser(User user){
        return userRepository.save(user);
    }

    //删除
    public void deleteUser(String id) {
        userRepository.delete(id);
    }

    //查询
    public List<User> getUserList() {
        return userRepository.findAll();
    }

    public User getUserById(String id)
    {
        return  userRepository.findOne(id);
    }

    public User getUserByUserId(String userId){
        return userRepository.findByUserId(userId);
    }

}
