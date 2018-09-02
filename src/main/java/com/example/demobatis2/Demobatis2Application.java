package com.example.demobatis2;

import com.example.demobatis2.util.MyMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
//@EnableTransactionManagement
@MapperScan(basePackages = "com.example.demobatis2.mapper", markerInterface = MyMapper.class)
public class Demobatis2Application {

    public static void main(String[] args) {
        SpringApplication.run(Demobatis2Application.class, args);
    }


}
