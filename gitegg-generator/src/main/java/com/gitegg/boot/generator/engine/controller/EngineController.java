package com.gitegg.boot.generator.engine.controller;


import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.gitegg.boot.generator.config.dto.QueryConfigDTO;
import com.gitegg.boot.generator.engine.dto.TableDTO;
import com.gitegg.boot.generator.engine.service.IEngineService;
import com.gitegg.platform.base.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


/**
* <p>
* 字段属性表 前端控制器
* </p>
*
* @author GitEgg
* @since 2021-09-03 11:55:34
*/
@RestController
@RequestMapping("/code/generator/engine")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "EngineController|代码生成引擎前端控制器", tags = {"代码生成接口"})
public class EngineController {

    private final IEngineService engineService;

    /**
    * 查询数据源下所有的表
    */
    @GetMapping("/table/list")
    @ApiOperation(value = "查询数据源下所有的表")
    public Result<?> listTable(QueryConfigDTO queryConfigDTO) {
        List<TableDTO> tableDTOS = engineService.queryTableList(queryConfigDTO);
        return Result.data(tableDTOS);
    }

    /**
     * 查询数据源下所有的表字段
     */
    @GetMapping("/table/field/list")
    @ApiOperation(value = "查询表字段信息")
    public Result<?> listField(String datasourceId, String tableName) {
        List<String> tableNames = new ArrayList<>();
        tableNames.add(tableName);
        List<TableInfo> tableInfo = engineService.queryTableFields(datasourceId, tableNames);
        return Result.data(tableInfo);
    }

    /**
     * 执行代码生成操作
     */
    @GetMapping("/process/generate/code")
    @ApiOperation(value = "查询表字段信息")
    public Result<?> processGenerateCode(QueryConfigDTO queryConfigDTO) {
        engineService.processGenerateCode(queryConfigDTO);
        return Result.success();
    }

 }
