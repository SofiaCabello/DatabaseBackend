package com.example.databasebackend.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class finishedorders {
    private Integer ID;
    private String name;
    private String custom;
    private String orderType;
    private String time;
    private Double profit;
}
