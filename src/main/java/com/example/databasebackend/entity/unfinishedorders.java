package com.example.databasebackend.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class unfinishedorders {
    private Integer ID;
    private String name;
    private String custom;
    private String orderType;
    private String time;
}
