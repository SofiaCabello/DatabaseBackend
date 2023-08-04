package com.example.databasebackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.databasebackend.entity.Drug;
import com.example.databasebackend.mapper.DrugMapper;
import com.example.databasebackend.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/drug")
@CrossOrigin(origins = "*")
public class DrugController {
    @Autowired
    private DrugMapper drugMapper;

    @GetMapping("getDrug")
    public Result getDrug(
            @RequestParam(value="page", defaultValue="1") Integer page,
            @RequestParam(value="limit", defaultValue="10") Integer limit,
            @RequestParam(value="sort", defaultValue="+id") String sort,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String manufacturer
            ){
        Page<Drug> pageParam = new Page<>(page, limit);
        QueryWrapper<Drug> wrapper = new QueryWrapper<>();
        if(sort.equals("+id")){
            wrapper.orderByAsc("id");
        } else if(sort.equals("-id")){
            wrapper.orderByDesc("id");
        } else if (sort.equals("+stock")) {
            wrapper.orderByAsc("stock");
        } else if (sort.equals("-stock")) {
            wrapper.orderByDesc("stock");
        } else if (sort.equals("+priceIn")) {
            wrapper.orderByAsc("price_in");
        } else if (sort.equals("-priceIn")) {
            wrapper.orderByDesc("price_in");
        } else if (sort.equals("+priceOut")) {
            wrapper.orderByAsc("price_out");
        } else if (sort.equals("-priceOut")) {
            wrapper.orderByDesc("price_out");
        }
        if(name!=null){
            wrapper.like("name", name);
        }
        if(type!=null){
            wrapper.eq("type", type);
        }
        if(manufacturer!=null){
            wrapper.like("manufacturer", manufacturer);
        }
        if(id!=null){
            wrapper.eq("id", id);
        }
        drugMapper.selectPage(pageParam, wrapper);
        List<Drug> list = pageParam.getRecords();
        System.out.println(list);
        return Result.ok(list).total(pageParam.getTotal());
    }

    @PostMapping("updateDrug")
    public Result updateDrug(@RequestBody Drug drug){
        UpdateWrapper<Drug> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", drug.getID());
        drugMapper.update(drug, wrapper);
        return Result.ok();
    }

    @PostMapping("deleteDrug")
    public Result deleteDrug(@RequestParam(value = "id") Integer id){
        QueryWrapper<Drug> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        drugMapper.delete(wrapper);
        return Result.ok();
    }

    @PostMapping("createDrug")
    public Result createDrug(@RequestBody Drug drug){
        drugMapper.insertDrug(drug.getName(), drug.getType(), drug.getManufacturer(), drug.getDescription(), drug.getStock(), drug.getPriceIn(), drug.getPriceOut());
        return Result.ok();
    }

    @GetMapping("getDrugList")
    public Result getDrugList(){
        List<Map<Integer,String>> queryResult = drugMapper.getAllDrug();
        return Result.ok(queryResult);
    }
}
