package com.gitegg.boot.extension.justauth.controller;


import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitegg.platform.base.result.PageResult;
import com.gitegg.platform.base.result.Result;
import com.gitegg.platform.base.util.BeanCopierUtils;
import com.gitegg.boot.extension.justauth.dto.*;
import com.gitegg.boot.extension.justauth.entity.*;
import com.gitegg.boot.extension.justauth.service.IJustAuthSocialService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


/**
* <p>
* 租户第三方登录功能配置表 前端控制器
* </p>
*
* @author GitEgg
* @since 2022-05-23
*/
@RestController
@RequestMapping("/extension/justauth/social")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "JustAuthSocialController|租户第三方登录功能配置表前端控制器")
@RefreshScope
public class JustAuthSocialController {

    private final IJustAuthSocialService justAuthSocialService;

    /**
    * 查询租户第三方登录功能配置表列表
    *
    * @param queryJustAuthSocialDTO
    * @param page
    * @return
    */
    @GetMapping("/list")
    @ApiOperation(value = "查询租户第三方登录功能配置表列表")
    public PageResult<JustAuthSocialDTO> list(QueryJustAuthSocialDTO queryJustAuthSocialDTO, Page<JustAuthSocialDTO> page) {
        Page<JustAuthSocialDTO> pageJustAuthSocial = justAuthSocialService.queryJustAuthSocialList(page, queryJustAuthSocialDTO);
        return PageResult.data(pageJustAuthSocial.getTotal(), pageJustAuthSocial.getRecords());
    }

    /**
    * 查询租户第三方登录功能配置表详情
    *
    * @param queryJustAuthSocialDTO
    * @return
    */
    @GetMapping("/query")
    @ApiOperation(value = "查询租户第三方登录功能配置表详情")
    public Result<?> query(QueryJustAuthSocialDTO queryJustAuthSocialDTO) {
        JustAuthSocialDTO justAuthSocialDTO = justAuthSocialService.queryJustAuthSocial(queryJustAuthSocialDTO);
        return Result.data(justAuthSocialDTO);
    }

    /**
    * 添加租户第三方登录功能配置表
    *
    * @param justAuthSocial
    * @return
    */
    @PostMapping("/create")
    @ApiOperation(value = "添加租户第三方登录功能配置表")
    public Result<?> create(@RequestBody CreateJustAuthSocialDTO justAuthSocial) {
        justAuthSocialService.createJustAuthSocial(justAuthSocial);
        return Result.success();
    }

    /**
    * 修改租户第三方登录功能配置表
    *
    * @param justAuthSocial
    * @return
    */
    @PostMapping("/update")
    @ApiOperation(value = "更新租户第三方登录功能配置表")
    public Result<?> update(@RequestBody UpdateJustAuthSocialDTO justAuthSocial) {
        boolean result = justAuthSocialService.updateJustAuthSocial(justAuthSocial);
        return Result.result(result);
    }

    /**
    * 删除租户第三方登录功能配置表
    *
    * @param justAuthSocialId
    * @return
    */
    @PostMapping("/delete/{justAuthSocialId}")
    @ApiOperation(value = "删除租户第三方登录功能配置表")
    @ApiImplicitParam(paramType = "path", name = "justAuthSocialId", value = "租户第三方登录功能配置表ID", required = true, dataType = "Long")
    public Result<?> delete(@PathVariable("justAuthSocialId") Long justAuthSocialId) {
        if (null == justAuthSocialId) {
            return Result.error("ID不能为空");
        }
        boolean result = justAuthSocialService.deleteJustAuthSocial(justAuthSocialId);
        return Result.result(result);
    }

    /**
    * 批量删除租户第三方登录功能配置表
    *
    * @param justAuthSocialIds
    * @return
    */
    @PostMapping("/batch/delete")
    @ApiOperation(value = "批量删除租户第三方登录功能配置表")
    @ApiImplicitParam(name = "justAuthSocialIds", value = "租户第三方登录功能配置表ID列表", required = true, dataType = "List")
    public Result<?> batchDelete(@RequestBody List<Long> justAuthSocialIds) {
        if (CollectionUtils.isEmpty(justAuthSocialIds)) {
            return Result.error("租户第三方登录功能配置表ID列表不能为空");
        }
        boolean result = justAuthSocialService.batchDeleteJustAuthSocial(justAuthSocialIds);
        return Result.result(result);
    }


    /**
    * 批量导出租户第三方登录功能配置表数据
    * @param response
    * @param queryJustAuthSocialDTO
    * @throws IOException
    */
    @GetMapping("/download")
    public void download(HttpServletResponse response, QueryJustAuthSocialDTO queryJustAuthSocialDTO) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("租户第三方登录功能配置表数据列表", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        List<JustAuthSocialDTO> justAuthSocialList = justAuthSocialService.queryJustAuthSocialList(queryJustAuthSocialDTO);
        List<JustAuthSocialExport> justAuthSocialExportList = new ArrayList<>();
        for (JustAuthSocialDTO justAuthSocialDTO : justAuthSocialList) {
           JustAuthSocialExport justAuthSocialExport = BeanCopierUtils.copyByClass(justAuthSocialDTO, JustAuthSocialExport.class);
           justAuthSocialExportList.add(justAuthSocialExport);
        }
        String sheetName = "租户第三方登录功能配置表数据列表";
        EasyExcel.write(response.getOutputStream(), JustAuthSocialExport.class).sheet(sheetName).doWrite(justAuthSocialExportList);
    }
 }
