package com.gitegg.boot.extension.sms.contorller;


import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitegg.boot.extension.sms.entity.SmsTemplateExport;
import com.gitegg.boot.extension.sms.entity.SmsTemplateImport;
import com.gitegg.platform.base.constant.GitEggConstant;
import com.gitegg.platform.base.dto.CheckExistDTO;
import com.gitegg.platform.base.result.PageResult;
import com.gitegg.platform.base.result.Result;
import com.gitegg.boot.extension.sms.dto.CreateSmsTemplateDTO;
import com.gitegg.boot.extension.sms.dto.QuerySmsTemplateDTO;
import com.gitegg.boot.extension.sms.dto.SmsTemplateDTO;
import com.gitegg.boot.extension.sms.dto.UpdateSmsTemplateDTO;
import com.gitegg.boot.extension.sms.entity.SmsTemplate;
import com.gitegg.boot.extension.sms.service.ISmsTemplateService;
import com.gitegg.platform.base.util.BeanCopierUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


/**
* <p>
* 短信配置表 前端控制器
* </p>
*
* @author GitEgg
* @since 2022-05-24
*/
@RestController
@RequestMapping("/extension/sms/template")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "SmsTemplateController|短信配置表前端控制器")
@RefreshScope
public class SmsTemplateController {

    private final ISmsTemplateService smsTemplateService;

    /**
    * 查询短信配置表列表
    *
    * @param querySmsTemplateDTO
    * @param page
    * @return
    */
    @GetMapping("/list")
    @ApiOperation(value = "查询短信配置表列表")
    public PageResult<SmsTemplateDTO> list(QuerySmsTemplateDTO querySmsTemplateDTO, Page<SmsTemplateDTO> page) {
        Page<SmsTemplateDTO> pageSmsTemplate = smsTemplateService.querySmsTemplateList(page, querySmsTemplateDTO);
        return PageResult.data(pageSmsTemplate.getTotal(), pageSmsTemplate.getRecords());
    }

    /**
    * 查询短信配置表详情
    *
    * @param querySmsTemplateDTO
    * @return
    */
    @GetMapping("/query")
    @ApiOperation(value = "查询短信配置表详情")
    public Result<?> query(QuerySmsTemplateDTO querySmsTemplateDTO) {
        SmsTemplateDTO smsTemplateDTO = smsTemplateService.querySmsTemplate(querySmsTemplateDTO);
        return Result.data(smsTemplateDTO);
    }

    /**
    * 添加短信配置表
    *
    * @param smsTemplate
    * @return
    */
    @PostMapping("/create")
    @ApiOperation(value = "添加短信配置表")
    public Result<?> create(@RequestBody CreateSmsTemplateDTO smsTemplate) {
        boolean result = smsTemplateService.createSmsTemplate(smsTemplate);
        return Result.result(result);
    }

    /**
    * 修改短信配置表
    *
    * @param smsTemplate
    * @return
    */
    @PostMapping("/update")
    @ApiOperation(value = "更新短信配置表")
    public Result<?> update(@RequestBody UpdateSmsTemplateDTO smsTemplate) {
        boolean result = smsTemplateService.updateSmsTemplate(smsTemplate);
        return Result.result(result);
    }

    /**
    * 删除短信配置表
    *
    * @param smsTemplateId
    * @return
    */
    @PostMapping("/delete/{smsTemplateId}")
    @ApiOperation(value = "删除短信配置表")
    @ApiImplicitParam(paramType = "path", name = "smsTemplateId", value = "短信配置表ID", required = true, dataTypeClass = Long.class)
    public Result<?> delete(@PathVariable("smsTemplateId") Long smsTemplateId) {
        if (null == smsTemplateId) {
            return Result.error("ID不能为空");
        }
        boolean result = smsTemplateService.deleteSmsTemplate(smsTemplateId);
        return Result.result(result);
    }

    /**
    * 批量删除短信配置表
    *
    * @param smsTemplateIds
    * @return
    */
    @PostMapping("/batch/delete")
    @ApiOperation(value = "批量删除短信配置表")
    @ApiImplicitParam(name = "smsTemplateIds", value = "短信配置表ID列表", required = true, dataTypeClass = List.class)
    public Result<?> batchDelete(@RequestBody List<Long> smsTemplateIds) {
        if (CollectionUtils.isEmpty(smsTemplateIds)) {
            return Result.error("短信配置表ID列表不能为空");
        }
        boolean result = smsTemplateService.batchDeleteSmsTemplate(smsTemplateIds);
        return Result.result(result);
    }
     /**
     * 修改短信配置表状态
     *
     * @param smsTemplateId
     * @param templateStatus
     * @return
     */
     @PostMapping("/status/{smsTemplateId}/{templateStatus}")
     @ApiOperation(value = "修改短信配置表状态")
     @ApiImplicitParams({
     @ApiImplicitParam(name = "smsTemplateId", value = "短信配置表ID", required = true, dataType = "Long", paramType = "path"),
     @ApiImplicitParam(name = "templateStatus", value = "短信配置表状态", required = true, dataType = "Integer", paramType = "path") })
     public Result<?> updateStatus(@PathVariable("smsTemplateId") Long smsTemplateId,
         @PathVariable("templateStatus") Integer templateStatus) {

         if (null == smsTemplateId || StringUtils.isEmpty(templateStatus)) {
           return Result.error("ID和状态不能为空");
         }
         UpdateSmsTemplateDTO smsTemplate = new UpdateSmsTemplateDTO();
         smsTemplate.setId(smsTemplateId);
         smsTemplate.setTemplateStatus(templateStatus);
         boolean result = smsTemplateService.updateSmsTemplate(smsTemplate);
         return Result.result(result);
     }

    /**
    * 校验短信配置表是否存在
    *
    * @param smsTemplate
    * @return
    */
    @PostMapping(value = "/check")
    @ApiOperation(value = "校验短信配置表是否存在", notes = "校验短信配置表是否存在")
    public Result<Boolean> checkSmsTemplateExist(CheckExistDTO smsTemplate) {
        String field = smsTemplate.getCheckField();
        String value = smsTemplate.getCheckValue();
        QueryWrapper<SmsTemplate> smsTemplateQueryWrapper = new QueryWrapper<>();
        smsTemplateQueryWrapper.eq(field, value);
        if(null != smsTemplate.getId()) {
            smsTemplateQueryWrapper.ne("id", smsTemplate.getId());
        }
        int count = smsTemplateService.count(smsTemplateQueryWrapper);
        if (GitEggConstant.COUNT_ZERO == count){
            return Result.data(true);
        } else{
            return Result.data(false);
        }
    }

    /**
    * 批量导出短信配置表数据
    * @param response
    * @param querySmsTemplateDTO
    * @throws IOException
    */
    @GetMapping("/download")
    public void download(HttpServletResponse response, QuerySmsTemplateDTO querySmsTemplateDTO) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("短信配置表数据列表", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        List<SmsTemplateDTO> smsTemplateList = smsTemplateService.querySmsTemplateList(querySmsTemplateDTO);
        List<SmsTemplateExport> smsTemplateExportList = new ArrayList<>();
        for (SmsTemplateDTO smsTemplateDTO : smsTemplateList) {
           SmsTemplateExport smsTemplateExport = BeanCopierUtils.copyByClass(smsTemplateDTO, SmsTemplateExport.class);
           smsTemplateExportList.add(smsTemplateExport);
        }
        String sheetName = "短信配置表数据列表";
        EasyExcel.write(response.getOutputStream(), SmsTemplateExport.class).sheet(sheetName).doWrite(smsTemplateExportList);
    }

    /**
    * 批量上传短信配置表数据
    * @param file
    * @return
    * @throws IOException
    */
    @PostMapping("/upload")
    public Result<?> upload(@RequestParam("uploadFile") MultipartFile file) throws IOException {
    List<SmsTemplateImport> smsTemplateImportList =  EasyExcel.read(file.getInputStream(), SmsTemplateImport.class, null).sheet().doReadSync();
        if (!CollectionUtils.isEmpty(smsTemplateImportList))
        {
            List<SmsTemplate> smsTemplateList = new ArrayList<>();
            smsTemplateImportList.stream().forEach(smsTemplateImport-> {
               smsTemplateList.add(BeanCopierUtils.copyByClass(smsTemplateImport, SmsTemplate.class));
            });
            smsTemplateService.saveBatch(smsTemplateList);
        }
        return Result.success();
    }

    /**
    * 下载短信配置表数据导入模板
    * @param response
    * @throws IOException
    */
    @GetMapping("/download/template")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("短信配置表数据导入模板", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        String sheetName = "短信配置表数据列表";
        EasyExcel.write(response.getOutputStream(), SmsTemplateImport.class).sheet(sheetName).doWrite(null);
    }
 }
