package com.gitegg.boot.system.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitegg.boot.system.dto.*;
import com.gitegg.boot.system.entity.DataPermissionRole;
import com.gitegg.boot.system.entity.RoleDataPermission;
import com.gitegg.boot.system.service.IDataPermissionRoleService;
import com.gitegg.boot.system.service.IRoleDataPermissionService;
import com.gitegg.platform.base.constant.GitEggConstant;
import com.gitegg.platform.base.dto.CheckExistDTO;
import com.gitegg.platform.base.result.PageResult;
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
* 数据权限配置表 前端控制器
* </p>
*
* @author GitEgg
* @since 2021-05-13
*/
@RestController
@RequestMapping("/system/data/permission/role")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "DataPermissionRoleController|数据权限配置表前端控制器", tags = {"数据权限配置"})
public class DataPermissionController {

    private final IDataPermissionRoleService dataPermissionRoleService;

    private final IRoleDataPermissionService roleDataPermissionService;

    /**
    * 查询数据权限配置表列表
    */
    @GetMapping("/list")
    @ApiOperation(value = "查询数据权限配置表列表")
    public PageResult<DataPermissionRoleDTO> list(QueryDataPermissionRoleDTO queryDataPermissionRoleDTO, Page<DataPermissionRoleDTO> page) {
        Page<DataPermissionRoleDTO> pageDataPermissionRole = dataPermissionRoleService.queryDataPermissionRoleList(page, queryDataPermissionRoleDTO);
        return PageResult.data(pageDataPermissionRole.getTotal(), pageDataPermissionRole.getRecords());
    }

    /**
    * 查询数据权限配置表详情
    */
    @GetMapping("/query")
    @ApiOperation(value = "查询数据权限配置表详情")
    public Result<?> query(QueryDataPermissionRoleDTO queryDataPermissionRoleDTO) {
        DataPermissionRoleDTO dataPermissionRoleDTO = dataPermissionRoleService.queryDataPermissionRole(queryDataPermissionRoleDTO);
        return Result.data(dataPermissionRoleDTO);
    }

    /**
    * 添加数据权限配置表
    */
    @PostMapping("/create")
    @ApiOperation(value = "添加数据权限配置表")
    public Result<?> create(@RequestBody CreateDataPermissionRoleDTO dataPermissionRole) {
        boolean result = dataPermissionRoleService.createDataPermissionRole(dataPermissionRole);
        return Result.result(result);
    }

    /**
    * 修改数据权限配置表
    */
    @PostMapping("/update")
    @ApiOperation(value = "更新数据权限配置表")
    public Result<?> update(@RequestBody UpdateDataPermissionRoleDTO dataPermissionRole) {
        boolean result = dataPermissionRoleService.updateDataPermissionRole(dataPermissionRole);
        return Result.result(result);
    }

    /**
    * 删除数据权限配置表
    */
    @PostMapping("/delete/{dataPermissionRoleId}")
    @ApiOperation(value = "删除数据权限配置表")
    @ApiImplicitParam(paramType = "path", name = "dataPermissionRoleId", value = "数据权限配置表ID", required = true, dataTypeClass = Long.class)
    public Result<?> delete(@PathVariable("dataPermissionRoleId") Long dataPermissionRoleId) {
        if (null == dataPermissionRoleId) {
            return Result.error("ID不能为空");
        }
        boolean result = dataPermissionRoleService.deleteDataPermissionRole(dataPermissionRoleId);
        return Result.result(result);
    }

    /**
    * 批量删除数据权限配置表
    */
    @PostMapping("/batch/delete")
    @ApiOperation(value = "批量删除数据权限配置表")
    @ApiImplicitParam(name = "dataPermissionRoleIds", value = "数据权限配置表ID列表", required = true, dataTypeClass = List.class)
    public Result<?> batchDelete(@RequestBody List<Long> dataPermissionRoleIds) {
        if (CollectionUtils.isEmpty(dataPermissionRoleIds)) {
            return Result.error("数据权限配置表ID列表不能为空");
        }
        boolean result = dataPermissionRoleService.batchDeleteDataPermissionRole(dataPermissionRoleIds);
        return Result.result(result);
    }

    /**
    * 修改数据权限配置表状态
    */
    @PostMapping("/status/{dpId}/{dpStatus}")
    @ApiOperation(value = "修改数据权限配置表状态")
    @ApiImplicitParams({
    @ApiImplicitParam(name = "dpId", value = "数据权限配置表ID", required = true, dataTypeClass = Long.class, paramType = "path"),
    @ApiImplicitParam(name = "dpStatus", value = "数据权限配置表状态", required = true, dataTypeClass = Integer.class, paramType = "path") })
    public Result<?> updateStatus(@PathVariable("dpId") Long dpId,
            @PathVariable("dpStatus") Integer dpStatus) {
        if (null == dpId || StringUtils.isEmpty(dpStatus)) {
            return Result.error("ID和状态不能为空");
        }
        UpdateDataPermissionRoleDTO dataPermissionRole = new UpdateDataPermissionRoleDTO();
        dataPermissionRole.setId(dpId);
        dataPermissionRole.setStatus(dpStatus);
        boolean result = dataPermissionRoleService.updateDataPermissionRole(dataPermissionRole);
        return Result.result(result);
    }

    /**
    * 校验数据权限配置表是否存在
    *
    * @param dataPermissionRole
    * @return
    */
    @PostMapping(value = "/check")
    @ApiOperation(value = "校验数据权限配置表是否存在", notes = "校验数据权限配置表是否存在")
    public Result<Boolean> checkDataPermissionRoleExist(CheckExistDTO dataPermissionRole) {
        String field = dataPermissionRole.getCheckField();
        String value = dataPermissionRole.getCheckValue();
        QueryWrapper<DataPermissionRole> dataPermissionRoleQueryWrapper = new QueryWrapper<>();
        dataPermissionRoleQueryWrapper.eq(field, value);
        if(null != dataPermissionRole.getId()) {
            dataPermissionRoleQueryWrapper.ne("id", dataPermissionRole.getId());
        }
        int count = dataPermissionRoleService.count(dataPermissionRoleQueryWrapper);
        if (GitEggConstant.COUNT_ZERO == count){
            return Result.data(true);
        } else{
            return Result.data(false);
        }
    }

    /**
     * 获取拥有某个数据权限的所有角色列表
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/get/roles/{id}")
    @ApiOperation(value = "获取拥有某个数据权限的所有角色列表")
    @ApiImplicitParam(paramType = "path", name = "id", value = "数据权限ID", required = true, dataTypeClass = Long.class)
    public Result<List<RoleDataPermission>> queryPermissionRoles(@PathVariable("id") Long id) {
        LambdaQueryWrapper<RoleDataPermission> ew = new LambdaQueryWrapper<>();
        ew.eq(RoleDataPermission::getDataPermissionId, id);
        List<RoleDataPermission> list = roleDataPermissionService.list(ew);
        return Result.data(list);
    }

    /**
     * 批量修改数据权限的角色列表
     *
     * @param updateRoleDataPermissionDTO
     * @return
     */
    @PostMapping(value = "/batch/role/update")
    @ApiOperation(value = "修改角色的权限资源")
    public Result<?> updateRoleResource(@RequestBody UpdateRoleDataPermissionDTO updateRoleDataPermissionDTO) {
        boolean result = roleDataPermissionService.updateDataPermissionRoleList(updateRoleDataPermissionDTO);
        return Result.result(result);
    }
 }
