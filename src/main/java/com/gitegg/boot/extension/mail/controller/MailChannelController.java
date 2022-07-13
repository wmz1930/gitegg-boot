package com.gitegg.boot.extension.mail.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitegg.platform.base.constant.GitEggConstant;
import com.gitegg.platform.base.result.PageResult;
import com.gitegg.platform.base.result.Result;
import com.gitegg.platform.base.dto.CheckExistDTO;
import com.gitegg.boot.extension.mail.entity.*;
import com.gitegg.boot.extension.mail.dto.*;

import com.gitegg.boot.extension.mail.service.IMailChannelService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import com.alibaba.excel.EasyExcel;
import com.gitegg.platform.base.util.BeanCopierUtils;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;


/**
* <p>
* 邮件渠道表 前端控制器
* </p>
*
* @author GitEgg
* @since 2022-06-24
*/
@RestController
@RequestMapping("/extension/mail/channel")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "MailChannelController|邮件渠道表前端控制器")
@RefreshScope
public class MailChannelController {

    private final IMailChannelService mailChannelService;

    /**
    * 查询邮件渠道表列表
    *
    * @param queryMailChannelDTO
    * @param page
    * @return
    */
    @GetMapping("/list")
    @ApiOperation(value = "查询邮件渠道表列表")
    public PageResult<MailChannelDTO> list(QueryMailChannelDTO queryMailChannelDTO, Page<MailChannelDTO> page) {
        Page<MailChannelDTO> pageMailChannel = mailChannelService.queryMailChannelList(page, queryMailChannelDTO);
        return PageResult.data(pageMailChannel.getTotal(), pageMailChannel.getRecords());
    }

    /**
    * 查询邮件渠道表详情
    *
    * @param queryMailChannelDTO
    * @return
    */
    @GetMapping("/query")
    @ApiOperation(value = "查询邮件渠道表详情")
    public Result<?> query(QueryMailChannelDTO queryMailChannelDTO) {
        MailChannelDTO mailChannelDTO = mailChannelService.queryMailChannel(queryMailChannelDTO);
        return Result.data(mailChannelDTO);
    }

    /**
    * 添加邮件渠道表
    *
    * @param mailChannel
    * @return
    */
    @PostMapping("/create")
    @ApiOperation(value = "添加邮件渠道表")
    public Result<?> create(@RequestBody CreateMailChannelDTO mailChannel) {
        boolean result = mailChannelService.createMailChannel(mailChannel);
        return Result.result(result);
    }

    /**
    * 修改邮件渠道表
    *
    * @param mailChannel
    * @return
    */
    @PostMapping("/update")
    @ApiOperation(value = "更新邮件渠道表")
    public Result<?> update(@RequestBody UpdateMailChannelDTO mailChannel) {
        boolean result = mailChannelService.updateMailChannel(mailChannel);
        return Result.result(result);
    }

    /**
    * 删除邮件渠道表
    *
    * @param mailChannelId
    * @return
    */
    @PostMapping("/delete/{mailChannelId}")
    @ApiOperation(value = "删除邮件渠道表")
    @ApiImplicitParam(paramType = "path", name = "mailChannelId", value = "邮件渠道表ID", required = true, dataType = "Long")
    public Result<?> delete(@PathVariable("mailChannelId") Long mailChannelId) {
        if (null == mailChannelId) {
            return Result.error("ID不能为空");
        }
        boolean result = mailChannelService.deleteMailChannel(mailChannelId);
        return Result.result(result);
    }

    /**
    * 批量删除邮件渠道表
    *
    * @param mailChannelIds
    * @return
    */
    @PostMapping("/batch/delete")
    @ApiOperation(value = "批量删除邮件渠道表")
    @ApiImplicitParam(name = "mailChannelIds", value = "邮件渠道表ID列表", required = true, dataType = "List")
    public Result<?> batchDelete(@RequestBody List<Long> mailChannelIds) {
        if (CollectionUtils.isEmpty(mailChannelIds)) {
            return Result.error("邮件渠道表ID列表不能为空");
        }
        boolean result = mailChannelService.batchDeleteMailChannel(mailChannelIds);
        return Result.result(result);
    }
     /**
     * 修改邮件渠道表状态
     *
     * @param mailChannelId
     * @param channelStatus
     * @return
     */
     @PostMapping("/status/{mailChannelId}/{channelStatus}")
     @ApiOperation(value = "修改邮件渠道表状态")
     @ApiImplicitParams({
     @ApiImplicitParam(name = "mailChannelId", value = "邮件渠道表ID", required = true, dataType = "Long", paramType = "path"),
     @ApiImplicitParam(name = "channelStatus", value = "邮件渠道表状态", required = true, dataType = "Integer", paramType = "path") })
     public Result<?> updateStatus(@PathVariable("mailChannelId") Long mailChannelId,
         @PathVariable("channelStatus") Integer channelStatus) {

         if (null == mailChannelId || StringUtils.isEmpty(channelStatus)) {
           return Result.error("ID和状态不能为空");
         }
         UpdateMailChannelDTO mailChannel = new UpdateMailChannelDTO();
         mailChannel.setId(mailChannelId);
         mailChannel.setChannelStatus(channelStatus);
         boolean result = mailChannelService.updateMailChannel(mailChannel);
         return Result.result(result);
     }


    /**
    * 批量导出邮件渠道表数据
    * @param response
    * @param queryMailChannelDTO
    * @throws IOException
    */
    @GetMapping("/download")
    public void download(HttpServletResponse response, QueryMailChannelDTO queryMailChannelDTO) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("邮件渠道表数据列表", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        List<MailChannelDTO> mailChannelList = mailChannelService.queryMailChannelList(queryMailChannelDTO);
        List<MailChannelExport> mailChannelExportList = new ArrayList<>();
        for (MailChannelDTO mailChannelDTO : mailChannelList) {
           MailChannelExport mailChannelExport = BeanCopierUtils.copyByClass(mailChannelDTO, MailChannelExport.class);
           mailChannelExportList.add(mailChannelExport);
        }
        String sheetName = "邮件渠道表数据列表";
        EasyExcel.write(response.getOutputStream(), MailChannelExport.class).sheet(sheetName).doWrite(mailChannelExportList);
    }

    /**
    * 批量上传邮件渠道表数据
    * @param file
    * @return
    * @throws IOException
    */
    @PostMapping("/upload")
    public Result<?> upload(@RequestParam("uploadFile") MultipartFile file) throws IOException {
    List<MailChannelImport> mailChannelImportList =  EasyExcel.read(file.getInputStream(), MailChannelImport.class, null).sheet().doReadSync();
        if (!CollectionUtils.isEmpty(mailChannelImportList))
        {
            List<MailChannel> mailChannelList = new ArrayList<>();
            mailChannelImportList.stream().forEach(mailChannelImport-> {
               mailChannelList.add(BeanCopierUtils.copyByClass(mailChannelImport, MailChannel.class));
            });
            mailChannelService.saveBatch(mailChannelList);
        }
        return Result.success();
    }

    /**
    * 下载邮件渠道表数据导入模板
    * @param response
    * @throws IOException
    */
    @GetMapping("/download/template")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("邮件渠道表数据导入模板", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        String sheetName = "邮件渠道表数据列表";
        EasyExcel.write(response.getOutputStream(), MailChannelImport.class).sheet(sheetName).doWrite(null);
    }
 }
