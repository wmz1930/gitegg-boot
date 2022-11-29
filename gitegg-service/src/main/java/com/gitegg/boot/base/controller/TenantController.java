package com.gitegg.boot.base.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitegg.boot.base.dto.CreateTenantDTO;
import com.gitegg.boot.base.dto.QueryTenantDTO;
import com.gitegg.boot.base.dto.TenantDTO;
import com.gitegg.boot.base.dto.UpdateTenantDTO;
import com.gitegg.boot.base.entity.Tenant;
import com.gitegg.boot.base.service.ITenantService;
import com.gitegg.platform.base.constant.GitEggConstant;
import com.gitegg.platform.base.dto.CheckExistDTO;
import com.gitegg.platform.base.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 租户信息表 前端控制器
 * </p>
 *
 * @author GitEgg
 * @since 2020-12-18
 */
@RestController
@RequestMapping("/extension/base/tenant")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "TenantController|租户信息表前端控制器", tags = {"租户信息配置"})
public class TenantController {

    private final ITenantService tenantService;

    /**
     * 查询租户信息表列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询租户信息表列表")
    public Result<Page<TenantDTO>> list(QueryTenantDTO queryTenantDTO, Page<TenantDTO> page) {
        Page<TenantDTO> pageTenant = tenantService.queryTenantList(page, queryTenantDTO);
        return Result.data(pageTenant);
    }

    /**
     * 查询租户信息表详情
     */
    @GetMapping("/query")
    @ApiOperation(value = "查询租户信息表详情")
    public Result<?> query(QueryTenantDTO queryTenantDTO) {
        TenantDTO tenantDTO = tenantService.queryTenant(queryTenantDTO);
        return Result.data(tenantDTO);
    }

    /**
     * 添加租户信息表
     */
    @PostMapping("/create")
    @ApiOperation(value = "添加租户信息表")
    public Result<?> create(@RequestBody CreateTenantDTO tenant) {
        boolean result = tenantService.createTenant(tenant);
        return Result.result(result);
    }

    /**
     * 修改租户信息表
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新租户信息表")
    public Result<?> update(@RequestBody UpdateTenantDTO tenant) {
        boolean result = tenantService.updateTenant(tenant);
        return Result.result(result);
    }

    /**
     * 删除租户信息表
     */
    @PostMapping("/delete/{tenantId}")
    @ApiOperation(value = "删除租户信息表")
    @ApiImplicitParam(paramType = "path", name = "tenantId", value = "租户信息表ID", required = true, dataTypeClass = Long.class)
    public Result<?> delete(@PathVariable("tenantId") Long tenantId) {
        if (null == tenantId) {
            return Result.error("ID不能为空");
        }
        boolean result = tenantService.deleteTenant(tenantId);
        return Result.result(result);
    }

    /**
     * 批量删除租户信息表
     */
    @PostMapping("/batch/delete")
    @ApiOperation(value = "批量删除租户信息表")
    @ApiImplicitParam(name = "tenantIds", value = "租户信息表ID列表", required = true, dataTypeClass = List.class)
    public Result<?> batchDelete(@RequestBody List<Long> tenantIds) {
        if (CollectionUtils.isEmpty(tenantIds)) {
            return Result.error("租户信息表ID列表不能为空");
        }
        boolean result = tenantService.batchDeleteTenant(tenantIds);
        return Result.result(result);
    }

    /**
     * 修改租户信息表状态
     */
    @PostMapping("/status/{tenantId}/{tenantStatus}")
    @ApiOperation(value = "修改租户信息表状态")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "tenantId", value = "租户信息表ID", required = true, dataTypeClass = Long.class, paramType = "path"),
        @ApiImplicitParam(name = "tenantStatus", value = "租户信息表状态", required = true, dataTypeClass = Integer.class,
            paramType = "path")})
    public Result<?> updateStatus(@PathVariable("tenantId") Long tenantId,
        @PathVariable("tenantStatus") Integer tenantStatus) {
        if (null == tenantId || StringUtils.isEmpty(tenantStatus)) {
            return Result.error("ID和状态不能为空");
        }
        UpdateTenantDTO tenant = new UpdateTenantDTO();
        tenant.setId(tenantId);
        tenant.setTenantStatus(tenantStatus);
        boolean result = tenantService.updateTenant(tenant);
        return Result.result(result);
    }

    /**
     * 校验租户信息表是否存在
     *
     * @param tenant
     * @return
     */
    @PostMapping(value = "/check")
    @ApiOperation(value = "校验租户信息表是否存在", notes = "校验租户信息表是否存在")
    public Result<Boolean> checkTenantExist(CheckExistDTO tenant) {
        String field = tenant.getCheckField();
        String value = tenant.getCheckValue();
        QueryWrapper<Tenant> tenantQueryWrapper = new QueryWrapper<>();
        tenantQueryWrapper.eq(field, value);
        if (null != tenant.getId()) {
            tenantQueryWrapper.ne("id", tenant.getId());
        }
        int count = tenantService.count(tenantQueryWrapper);
        if (GitEggConstant.COUNT_ZERO == count) {
            return Result.data(true);
        } else {
            return Result.data(false);
        }
    }
}
