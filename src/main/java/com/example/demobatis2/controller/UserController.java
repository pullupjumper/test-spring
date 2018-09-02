package com.example.demobatis2.controller;

import com.example.demobatis2.entity.User;
import com.example.demobatis2.entity.UserCustom;
import com.example.demobatis2.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping(value = "/users/{id}")
    public User findById(@PathVariable Integer id) {
        return userService.findById(id);
    }

    @PostMapping(value = "/users")
    public void saveUser(@RequestBody UserCustom userCustom){
        userService.saveUser(userCustom);
    }
}
