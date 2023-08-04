package com.example.databasebackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.databasebackend.mapper.UnfinishedMapper;
import com.example.databasebackend.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.databasebackend.entity.unfinishedorders;
import com.example.databasebackend.entity.UnfinishedPost;

@RestController
@RequestMapping("/order")
public class UnfinishedController {
    @Autowired
    private UnfinishedMapper unfinishedMapper;

    @GetMapping("getUnfinished")
    public Result getUnfinished(
            @RequestParam(value = "page",defaultValue = "1") Integer page,
            @RequestParam(value = "limit",defaultValue = "10") Integer limit,
            @RequestParam(value = "sort",defaultValue = "+id") String sort,
            @RequestParam(required = false)  Integer orderID,
            @RequestParam(required = false)  String drugName,
            @RequestParam(required = false)  String custom,
            @RequestParam(required = false)  String orderType,
            @RequestParam(required = false)  String time){
        Page<unfinishedorders> pageParam = new Page<>(page, limit);
        QueryWrapper<unfinishedorders> wrapper = new QueryWrapper<>();
        if(sort.equals("+id")){
            wrapper.orderByAsc("id");
        } else if(sort.equals("-id")){
            wrapper.orderByDesc("id");
        } else if (sort.equals("+time")) {
            wrapper.orderByAsc("time");
        } else if (sort.equals("-time")) {
            wrapper.orderByDesc("time");
        }
        if(orderID!=null) {
            wrapper.eq("ID", orderID);
        }
        if(drugName!=null) {
            wrapper.like("name", drugName);
        }
        if(custom!=null) {
            wrapper.like("custom", custom);
        }
        unfinishedMapper.selectPage(pageParam, wrapper);
        int total = (int) pageParam.getTotal();
        for(int i=0 ; i<pageParam.getRecords().size(); i++){
            for(int j=i+1; j<pageParam.getRecords().size(); j++){
                if(pageParam.getRecords().get(i).getID().equals(pageParam.getRecords().get(j).getID())){
                    //将药品名合并
                    pageParam.getRecords().get(i).setName(pageParam.getRecords().get(i).getName()+","+pageParam.getRecords().get(j).getName());
                    pageParam.getRecords().remove(j);
                    System.out.println("pageParam.getRecords():"+pageParam.getRecords());
                    j--;
                    total--;
                }
            }
        }
        return Result.ok(pageParam.getRecords()).total(total);
    }

    // 以及增删改的API...
    @PostMapping("createUnfinished")
    public Result createUnfinished(@RequestBody UnfinishedPost unfinishedPost){
        unfinishedMapper.insertUnfinished(unfinishedPost.getCustom(), unfinishedPost.getOrderType(), unfinishedPost.getTime());
        Integer[] ID = unfinishedPost.getID();
        Integer unfinishedID = unfinishedMapper.selectMaxID();
        // quantity是由逗号分隔的字符串，需要分割
        Integer[] quantity = new Integer[]{};
        if(unfinishedPost.getQuantity()!=null){
            String[] quantityString = unfinishedPost.getQuantity().split(",");
            quantity = new Integer[quantityString.length];
            for(int i=0; i<quantityString.length; i++){
                quantity[i] = Integer.parseInt(quantityString[i]);
            }
        }
        if(quantity.length!=ID.length){
            return Result.fail().message("药品数量与药品种类不符");
        }else {
            for (int i = 0; i < ID.length; i++) {
                unfinishedMapper.insertDrugUnfinished(unfinishedID, ID[i], quantity[i]);
            }
        }
        return Result.ok();
    }

    @PostMapping("finishUnfinished")
    public Result finisheUnfinished(@RequestParam Integer id){
        unfinishedMapper.moveRecordToFinished(id);
        return Result.ok();
    }

    // 还差一个修改和删除的API...
}
