package com.example.databasebackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.databasebackend.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserTableMapper extends BaseMapper<User> {
    @Insert("insert into user(username, password, role) values(#{username}, #{password}, #{role})")
    void insertUser(String username, String password, String role);
}
