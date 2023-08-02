package com.example.databasebackend.entity;

import lombok.Data;

@Data
public class UserLogin {
    private String username;
    private String password;
    private String captcha;
}
