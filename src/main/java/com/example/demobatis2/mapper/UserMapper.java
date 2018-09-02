package com.example.demobatis2.mapper;

import com.example.demobatis2.entity.User;
import com.example.demobatis2.entity.UserCustom;
import com.example.demobatis2.util.MyMapper;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public interface UserMapper extends MyMapper<User> {

    UserCustom findByUsername(String name);

    int saveUser(User user);

    int saveUserNRole(Map<String, Object> map);
}