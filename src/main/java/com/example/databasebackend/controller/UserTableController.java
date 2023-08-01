package com.example.databasebackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.databasebackend.entity.User;
import com.example.databasebackend.mapper.UserTableMapper;
import com.example.databasebackend.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userTable")
public class UserTableController {
    @Autowired
    private UserTableMapper userTableMapper;

    @GetMapping("getUser")
    public Result getUser(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit,
            @RequestParam(value = "sort", defaultValue = "+id") String sort,
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String role){
        Page<User> pageParam = new Page<>(page, limit);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        if(sort.equals("+id")){
            wrapper.orderByAsc("id");
        } else if(sort.equals("-id")){
            wrapper.orderByDesc("id");
        } else if (sort.equals("+username")) {
            wrapper.orderByAsc("username");
        } else if (sort.equals("-username")) {
            wrapper.orderByDesc("username");
        } else if (sort.equals("+role")) {
            wrapper.orderByAsc("role");
        } else if (sort.equals("-role")) {
            wrapper.orderByDesc("role");
        }
        if(username!=null){
            wrapper.like("username", username);
        }
        if(role!=null){
            wrapper.eq("role", role);
        }
        if(id!=null){
            wrapper.eq("id", id);
        }

        userTableMapper.selectPage(pageParam, wrapper);
        return Result.ok(pageParam.getRecords()).total(pageParam.getTotal());
    }

    @PostMapping("deleteUser")
    public Result deleteUser(@RequestParam(value = "id") Integer id){
        userTableMapper.deleteById(id);
        return Result.ok();
    }

    @PostMapping("createUser")
    public Result createUser(@RequestBody User user){
        userTableMapper.insertUser(user.getUsername(), user.getPassword(), user.getRole());
        return Result.ok();
    }

    @PostMapping("updateUser")
    public Result updateUser(@RequestBody User user){
        UpdateWrapper<User> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", user.getId());
        userTableMapper.update(user, wrapper);
        return Result.ok();
    }
}
