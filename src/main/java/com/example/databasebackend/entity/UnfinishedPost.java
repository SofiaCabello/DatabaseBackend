package com.example.databasebackend.entity;

import lombok.Data;

@Data
public class UnfinishedPost {
    private Integer[] ID;
    private String custom;
    private String orderType;
    private String time;
    private String quantity;
}
