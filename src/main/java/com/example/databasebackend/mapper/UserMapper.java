package com.example.databasebackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.databasebackend.entity.User;
import lombok.ToString;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT password FROM user WHERE username = #{username}")
    String getPassword(User user);

    @Select("SELECT role FROM user WHERE username = #{username}")
    String getRole(String username);

    @Select("SELECT id FROM user WHERE username = #{username}")
    Integer getId(String username);
}
