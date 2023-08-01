package com.example.databasebackend.mapper;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.databasebackend.entity.Drug;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DrugMapper extends BaseMapper<Drug> {
    @Insert("insert into drug(name, type, manufacturer, description, stock, price) values(#{name}, #{type}, #{manufacturer}, #{description}, #{stock}, #{price})")
    int insertDrug(String name, String type, String manufacturer, String description, Integer stock, Double price);
}
