package com.example.databasebackend.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@TableName("drug")
public class Drug {
    private Integer ID;
    private String name;
    private String type;
    private String manufacturer;
    private String description;
    private Integer stock;
    private Double price;
}
