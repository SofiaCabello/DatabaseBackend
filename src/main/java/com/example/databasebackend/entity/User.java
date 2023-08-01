package com.example.databasebackend.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@TableName("user")
public class User{
    private Integer id;
    private String username;
    private String password;
    private String role;
}
