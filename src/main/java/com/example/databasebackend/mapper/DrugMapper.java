package com.example.databasebackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.databasebackend.entity.Drug;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;


@Mapper
public interface DrugMapper extends BaseMapper<Drug> {
    @Insert("insert into drug(name, type, manufacturer, description, stock, priceIn, priceOut) values(#{name}, #{type}, #{manufacturer}, #{description}, #{stock}, #{priceIn}, #{priceOut})")
    int insertDrug(String name, String type, String manufacturer, String description, Integer stock, Double priceIn, Double priceOut);

    @Select("SELECT id,name FROM drug")
    List<Map<Integer, String>> getAllDrug();
}
