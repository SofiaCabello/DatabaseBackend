package com.example.databasebackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.databasebackend.entity.finishedorders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FinishedMapper extends BaseMapper<finishedorders> {
}
