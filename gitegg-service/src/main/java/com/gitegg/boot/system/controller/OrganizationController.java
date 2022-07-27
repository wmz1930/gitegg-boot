package com.gitegg.boot.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gitegg.boot.system.dto.CreateOrganizationDTO;
import com.gitegg.boot.system.dto.UpdateOrganizationDTO;
import com.gitegg.boot.system.entity.Organization;
import com.gitegg.boot.system.service.IOrganizationService;
import com.gitegg.platform.base.constant.GitEggConstant;
import com.gitegg.platform.base.dto.CheckExistDTO;
import com.gitegg.platform.base.enums.ResultCodeEnum;
import com.gitegg.platform.base.result.Result;
import com.gitegg.platform.base.util.BeanCopierUtils;
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
 * 组织表 前端控制器
 * </p>
 *
 * @author gitegg
 * @since 2019-05-19
 */
@RestController
@RequestMapping(value = "/system/organization")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "OrganizationController|组织机构相关的前端控制器", tags = {"组织机构配置"})
public class OrganizationController {

    private final IOrganizationService organizationService;

    /**
     * 查询组织树
     * 
     * @param parentId
     * @return
     */
    @GetMapping(value = "/tree")
    @ApiOperation(value = "查询组织机构树", notes = "树状展示组织机构信息")
    @ApiImplicitParam(paramType = "query", name = "parentId", value = "父级ID", required = false, dataTypeClass = Integer.class)
    public Result<List<Organization>> queryOrganizationTree(Long parentId) {
        List<Organization> treeList = organizationService.queryOrganizationByParentId(parentId);
        return Result.data(treeList);
    }

    /**
     * 添加组织
     */
    @PostMapping("/create")
    @ApiOperation(value = "添加组织机构")
    public Result<Organization> create(@RequestBody CreateOrganizationDTO org) {
        Organization orgEntity = BeanCopierUtils.copyByClass(org, Organization.class);
        if (null != org && !CollectionUtils.isEmpty(org.getAreas())) {
            orgEntity.setProvince(org.getAreas().get(GitEggConstant.Address.PROVINCE));
            orgEntity.setCity(org.getAreas().get(GitEggConstant.Address.CITY));
            orgEntity.setArea(org.getAreas().get(GitEggConstant.Address.AREA));
        }
        boolean result = organizationService.createOrganization(orgEntity);
        if (result) {
            return Result.data(orgEntity);
        } else {
            return Result.error(ResultCodeEnum.FAILED);
        }
    }

    /**
     * 修改组织
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新组织机构")
    public Result<Organization> update(@RequestBody UpdateOrganizationDTO org) {
        Organization orgEntity = BeanCopierUtils.copyByClass(org, Organization.class);
        if (null != org && !CollectionUtils.isEmpty(org.getAreas())) {
            orgEntity.setProvince(org.getAreas().get(GitEggConstant.Address.PROVINCE));
            orgEntity.setCity(org.getAreas().get(GitEggConstant.Address.CITY));
            orgEntity.setArea(org.getAreas().get(GitEggConstant.Address.AREA));
        }
        boolean result = organizationService.updateOrganization(orgEntity);
        if (result) {
            return Result.data(orgEntity);
        } else {
            return Result.error(ResultCodeEnum.FAILED);
        }
    }

    /**
     * 修改修改组织机构状态
     */
    @PostMapping("/status/{organizationId}/{organizationStatus}")
    @ApiOperation(value = "修改角色状态")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "organizationId", value = "组织机构ID", required = true, dataTypeClass = Long.class,
            paramType = "path"),
        @ApiImplicitParam(name = "organizationStatus", value = "组织机构状态", required = true, dataTypeClass = Integer.class,
            paramType = "path")})
    public Result<?> updateStatus(@PathVariable("organizationId") Long organizationId,
        @PathVariable("organizationStatus") Integer organizationStatus) {
        if (null == organizationId || StringUtils.isEmpty(organizationStatus)) {
            return Result.error("ID和状态不能为空");
        }
        Organization organization = new Organization();
        organization.setId(organizationId);
        organization.setOrganizationStatus(organizationStatus);
        boolean result = organizationService.updateOrganization(organization);
        if (result) {
            return Result.success();
        } else {
            return Result.error(ResultCodeEnum.FAILED);
        }
    }

    /**
     * 删除组织
     */
    @PostMapping("/delete/{organizationId}")
    @ApiOperation(value = "删除组织机构")
    @ApiImplicitParam(paramType = "path", name = "organizationId", value = "组织机构ID", required = true, dataTypeClass = Long.class)
    public Result<?> delete(@PathVariable("organizationId") Long organizationId) {
        boolean result = organizationService.deleteOrganization(organizationId);
        if (result) {
            return Result.success();
        } else {
            return Result.error(ResultCodeEnum.FAILED);
        }
    }

    @PostMapping(value = "/check")
    @ApiOperation(value = "校验组织是否存在", notes = "校验组织是否存在")
    public Result<Boolean> checkOrganization(CheckExistDTO organization) {
        String field = organization.getCheckField();
        String value = organization.getCheckValue();
        QueryWrapper<Organization> organizationQueryWrapper = new QueryWrapper<>();
        organizationQueryWrapper.eq(field, value);
        if(null != organization.getId()) {
            organizationQueryWrapper.ne("id", organization.getId());
        }
        int count = organizationService.count(organizationQueryWrapper);
        if (GitEggConstant.COUNT_ZERO == count){
            return Result.data(true);
        } else{
            return Result.data(false);
        }
    }
}
