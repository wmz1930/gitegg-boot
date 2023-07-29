package com.gitegg.boot.extension.wx.pay.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitegg.platform.base.constant.GitEggConstant;
import com.gitegg.platform.base.result.Result;
import com.gitegg.platform.base.dto.CheckExistDTO;
import com.gitegg.platform.base.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import com.gitegg.boot.extension.wx.pay.entity.*;
import com.gitegg.boot.extension.wx.pay.dto.*;

import com.gitegg.boot.extension.wx.pay.service.IPayService;

import javax.validation.Valid;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import com.gitegg.boot.extension.wx.pay.bo.*;
import com.alibaba.excel.EasyExcel;
import com.gitegg.platform.base.util.BeanCopierUtils;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;


/**
* <p>
* 微信支付 前端控制器
* </p>
*
* @author GitEgg
* @since 2023-07-29
*/
@Slf4j
@RestController
@RequestMapping("/extension/wx/pay")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "PayController|微信支付前端控制器", tags = {"微信支付"})
public class PayController {

    private final IPayService payService;

    /**
    * 查询微信支付列表
    *
    * @param queryPayDTO
    * @param page
    * @return
    */
    @GetMapping("/list")
    @ApiOperation(value = "查询微信支付列表")
    public Result<Page<PayDTO>> list(QueryPayDTO queryPayDTO, Page<PayDTO> page) {
        Page<PayDTO> pagePay = payService.queryPayList(page, queryPayDTO);
        return Result.data(pagePay);
    }

    /**
    * 查询微信支付详情
    *
    * @param queryPayDTO
    * @return
    */
    @GetMapping("/query")
    @ApiOperation(value = "查询微信支付详情")
    public Result<?> query(QueryPayDTO queryPayDTO) {
        PayDTO payDTO = payService.queryPay(queryPayDTO);
        return Result.data(payDTO);
    }

    /**
    * 添加微信支付
    *
    * @param pay
    * @return
    */
    @PostMapping("/create")
    @ApiOperation(value = "添加微信支付")
    public Result<?> create(@RequestBody @Valid CreatePayDTO pay) {
        boolean result = payService.createPay(pay);
        return Result.result(result);
    }

    /**
    * 修改微信支付
    *
    * @param pay
    * @return
    */
    @PostMapping("/update")
    @ApiOperation(value = "更新微信支付")
    public Result<?> update(@RequestBody @Valid UpdatePayDTO pay) {
        boolean result = payService.updatePay(pay);
        return Result.result(result);
    }

    /**
    * 删除微信支付
    *
    * @param payId
    * @return
    */
    @PostMapping("/delete/{payId}")
    @ApiOperation(value = "删除微信支付")
    @ApiImplicitParam(paramType = "path", name = "payId", value = "微信支付ID", required = true, dataTypeClass = Long.class)
    public Result<?> delete(@PathVariable("payId") Long payId) {
        if (null == payId) {
            return Result.error("ID不能为空");
        }
        boolean result = payService.deletePay(payId);
        return Result.result(result);
    }

    /**
    * 批量删除微信支付
    *
    * @param payIds
    * @return
    */
    @PostMapping("/batch/delete")
    @ApiOperation(value = "批量删除微信支付")
    @ApiImplicitParam(name = "payIds", value = "微信支付ID列表", required = true, dataTypeClass = List.class)
    public Result<?> batchDelete(@RequestBody List<Long> payIds) {
        if (CollectionUtils.isEmpty(payIds)) {
            return Result.error("微信支付ID列表不能为空");
        }
        boolean result = payService.batchDeletePay(payIds);
        return Result.result(result);
    }
     /**
     * 修改微信支付状态
     *
     * @param payId
     * @param status
     * @return
     */
     @PostMapping("/status/{payId}/{status}")
     @ApiOperation(value = "修改微信支付状态")
     @ApiImplicitParams({
     @ApiImplicitParam(name = "payId", value = "微信支付ID", required = true, dataType = "Long", paramType = "path"),
     @ApiImplicitParam(name = "status", value = "微信支付状态", required = true, dataType = "String", paramType = "path") })
     public Result<?> updateStatus(@PathVariable("payId") Long payId,
         @PathVariable("status") String status) {

         if (null == payId || StringUtils.isEmpty(status)) {
           return Result.error("ID和状态不能为空");
         }
         UpdatePayDTO pay = new UpdatePayDTO();
         pay.setId(payId);
         pay.setStatus(status);
         boolean result = payService.updatePay(pay);
         return Result.result(result);
     }

    /**
    * 校验微信支付是否存在
    *
    * @param pay
    * @return
    */
    @PostMapping(value = "/check")
    @ApiOperation(value = "校验微信支付是否存在", notes = "校验微信支付是否存在")
    public Result<Boolean> checkPayExist(@RequestBody @Valid CheckExistDTO pay) {
        String field = pay.getCheckField();
        String value = pay.getCheckValue();
        QueryWrapper<Pay> payQueryWrapper = new QueryWrapper<>();
        payQueryWrapper.eq(field, value);
        if(null != pay.getId()) {
            payQueryWrapper.ne("id", pay.getId());
        }
        int count = payService.count(payQueryWrapper);
        if (GitEggConstant.COUNT_ZERO == count){
            return Result.data(true);
        } else{
            return Result.data(false);
        }
    }

    /**
     * 批量导出微信支付数据
     * @param response
     * @param queryPayDTO
     */
    @PostMapping("/export")
    @ApiOperation("导出数据")
    public void exportPayList(HttpServletResponse response, @RequestBody QueryPayDTO queryPayDTO) {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = null;
        try {
            fileName = URLEncoder.encode("用户数据列表", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            List<PayExportBO> payExportList = payService.exportPayList(queryPayDTO);
            String sheetName = "用户数据列表";
            EasyExcel.write(response.getOutputStream(), PayExportBO.class).sheet(sheetName).doWrite(payExportList);
        } catch (Exception e) {
            log.error("批量导出用户数据时发生错误:{}", e);
            throw new BusinessException("批量导出失败:" + e);
        }

    }

    /**
     * 批量上传微信支付数据
     * @param file
     * @return
     */
    @PostMapping("/import")
    @ApiOperation("批量上传微信支付数据")
    public Result<?> importPayList(@RequestParam("uploadFile") MultipartFile file) {
        boolean importSuccess = payService.importPayList(file);
        return Result.result(importSuccess);
    }

    /**
    * 下载微信支付数据导入模板
    * @param response
    * @throws IOException
    */
    @GetMapping("/download/template")
    @ApiOperation("导出上传模板")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = null;
        try {
            fileName = URLEncoder.encode("微信支付数据导入模板", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            String sheetName = "微信支付数据列表";
            EasyExcel.write(response.getOutputStream(), PayImportBO.class).sheet(sheetName).doWrite(new ArrayList<>());
        } catch (Exception e) {
            log.error("下载微信支付批量模板时发生错误:{}", e);
            throw new BusinessException("下载批量模板失败:" + e);
        }
    }
 }
