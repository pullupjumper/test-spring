package com.example.demobatis2.service;

import com.example.demobatis2.entity.User;
import com.example.demobatis2.entity.UserCustom;
import com.example.demobatis2.mapper.UserMapper;
import com.example.demobatis2.util.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl extends BaseService<User> {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User findById(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Transactional
    public int saveUser(UserCustom userCustom) {
        User user = new User();
        user.setUsername(userCustom.getUsername());
        user.setPassword(passwordEncoder.encode(userCustom.getPassword()));
        int count = userMapper.saveUser(user);
        Map<String, Object> map = new HashMap<>();
        map.put("userId", user.getId());
        map.put("roles", userCustom.getRoles());
        userMapper.saveUserNRole(map);
        return count;
    }

}
