package com.example.databasebackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import com.example.databasebackend.entity.unfinishedorders;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UnfinishedMapper extends BaseMapper<unfinishedorders> {

    @Insert("INSERT INTO unfinished(custom,order_type,time) VALUES(#{custom},#{order_type},#{time})")
    int insertUnfinished(String custom, String order_type, String time);

    @Insert("INSERT INTO drugUnfinished(order_ID, drug_ID, quantity) VALUES(#{order_ID},#{drug_ID},#{quantity})")
    int insertDrugUnfinished(Integer order_ID, Integer drug_ID, Integer quantity);

    @Select("SELECT MAX(ID) FROM unfinished")
    Integer selectMaxID();

    @Select("{CALL MoveRecordToFinished(#{id})}")
    void moveRecordToFinished(Integer id);
}
