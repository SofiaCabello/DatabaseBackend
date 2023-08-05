package com.example.databasebackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import com.example.databasebackend.entity.unfinishedorders;

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

    @Update("UPDATE unfinished SET custom=#{custom},order_type=#{order_type},time=#{time} WHERE ID=#{id}")
    void updateUnfinished(Integer id, String custom, String order_type, String time);

    @Update("UPDATE drugUnfinished SET quantity=#{quantity} WHERE order_ID=#{order_ID} AND drug_ID=#{drug_ID}")
    void updateDrugUnfinished(Integer order_ID, Integer drug_ID, Integer quantity);

    @Delete("DELETE FROM unfinished WHERE ID=#{id}")
    void deleteUnfinished(Integer id);

}
