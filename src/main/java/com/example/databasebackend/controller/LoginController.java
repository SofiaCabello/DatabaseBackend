package com.example.databasebackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.databasebackend.entity.User;
import com.example.databasebackend.mapper.UserMapper;
import com.example.databasebackend.utils.JwtUtil;
import com.example.databasebackend.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class LoginController {
    @Autowired
    private UserMapper userMapper;

    @PostMapping("login")
    public Result login(@RequestBody User user){
        System.out.println(">>>>>>"+user+"<<<<<<<");
        String password = userMapper.getPassword(user);
        if(password==null || !password.equals(user.getPassword())){
            return Result.fail().message("用户名或密码错误");
        }else {
            String username = user.getUsername();
            String token = JwtUtil.generateToken(username);
            Map<String, Object> map = new HashMap<>();
            map.put("token", token);
            return Result.ok(map);
        }
    }

    @GetMapping("info")
    public Result info(String token){
        Map<String, Object> map=new HashMap<>();
        String username = JwtUtil.getUsername(token);
        String role = userMapper.getRole(username);
        Integer id = userMapper.getId(username);
        String[] roles = {role};
        map.put("roles",roles);
        map.put("name",username);
        map.put("id",id);
        map.put("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        return Result.ok(map);
    }

    @PostMapping("logout")
    public Result logout(){
        return Result.ok();
    }
}
