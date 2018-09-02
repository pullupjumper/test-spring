package com.example.demobatis2;

import com.example.demobatis2.entity.User;
import com.example.demobatis2.mapper.UserMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Demobatis2ApplicationTests {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Test
    public void contextLoads() {
    }

//    @Transactional
    @Test
    public void testService() throws Exception {
        User user = new User();
        user.setUsername("user2");
        user.setPassword(passwordEncoder.encode("user2"));
        userMapper.saveUser(user);

        Map<String, Object> map = new HashMap<>();
        map.put("userId", user.getId());
        map.put("roleIds", Arrays.asList(2));
        int count = userMapper.saveUserNRole(map);
        System.out.println(count);
        Assert.assertEquals(count, 1);
    }

}
