package com.gitegg.boot.extension.justauth.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitegg.boot.extension.justauth.dto.CreateJustAuthSocialUserDTO;
import com.gitegg.boot.extension.justauth.dto.JustAuthSocialUserDTO;
import com.gitegg.boot.extension.justauth.dto.QueryJustAuthSocialUserDTO;
import com.gitegg.boot.extension.justauth.dto.UpdateJustAuthSocialUserDTO;
import com.gitegg.boot.extension.justauth.service.IJustAuthSocialUserService;
import com.gitegg.platform.base.result.PageResult;
import com.gitegg.platform.base.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
* <p>
* 第三方用户绑定 前端控制器
* </p>
*
* @author GitEgg
* @since 2022-05-19
*/
@RestController
@RequestMapping("/extension/justauth/social/user")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "JustAuthSocialUserController|第三方用户绑定前端控制器", tags = {"第三方用户绑定"})
public class JustAuthSocialUserController {

    private final IJustAuthSocialUserService justAuthSocialUserService;

    /**
    * 查询第三方用户绑定列表
    *
    * @param queryJustAuthSocialUserDTO
    * @param page
    * @return
    */
    @GetMapping("/list")
    @ApiOperation(value = "查询第三方用户绑定列表")
    public PageResult<JustAuthSocialUserDTO> list(QueryJustAuthSocialUserDTO queryJustAuthSocialUserDTO, Page<JustAuthSocialUserDTO> page) {
        Page<JustAuthSocialUserDTO> pageJustAuthSocialUser = justAuthSocialUserService.queryJustAuthSocialUserList(page, queryJustAuthSocialUserDTO);
        return PageResult.data(pageJustAuthSocialUser.getTotal(), pageJustAuthSocialUser.getRecords());
    }

    /**
    * 查询第三方用户绑定详情
    *
    * @param queryJustAuthSocialUserDTO
    * @return
    */
    @GetMapping("/query")
    @ApiOperation(value = "查询第三方用户绑定详情")
    public Result<?> query(QueryJustAuthSocialUserDTO queryJustAuthSocialUserDTO) {
        JustAuthSocialUserDTO justAuthSocialUserDTO = justAuthSocialUserService.queryJustAuthSocialUser(queryJustAuthSocialUserDTO);
        return Result.data(justAuthSocialUserDTO);
    }

    /**
    * 添加第三方用户绑定
    *
    * @param justAuthSocialUser
    * @return
    */
    @PostMapping("/create")
    @ApiOperation(value = "添加第三方用户绑定")
    public Result<?> create(@RequestBody CreateJustAuthSocialUserDTO justAuthSocialUser) {
        justAuthSocialUserService.createJustAuthSocialUser(justAuthSocialUser);
        return Result.success();
    }

    /**
    * 修改第三方用户绑定
    *
    * @param justAuthSocialUser
    * @return
    */
    @PostMapping("/update")
    @ApiOperation(value = "更新第三方用户绑定")
    public Result<?> update(@RequestBody UpdateJustAuthSocialUserDTO justAuthSocialUser) {
        boolean result = justAuthSocialUserService.updateJustAuthSocialUser(justAuthSocialUser);
        return Result.result(result);
    }

    /**
    * 删除第三方用户绑定
    *
    * @param justAuthSocialUserId
    * @return
    */
    @PostMapping("/delete/{justAuthSocialUserId}")
    @ApiOperation(value = "删除第三方用户绑定")
    @ApiImplicitParam(paramType = "path", name = "justAuthSocialUserId", value = "第三方用户绑定ID", required = true, dataType = "Long")
    public Result<?> delete(@PathVariable("justAuthSocialUserId") Long justAuthSocialUserId) {
        if (null == justAuthSocialUserId) {
            return Result.error("ID不能为空");
        }
        boolean result = justAuthSocialUserService.deleteJustAuthSocialUser(justAuthSocialUserId);
        return Result.result(result);
    }

    /**
    * 批量删除第三方用户绑定
    *
    * @param justAuthSocialUserIds
    * @return
    */
    @PostMapping("/batch/delete")
    @ApiOperation(value = "批量删除第三方用户绑定")
    @ApiImplicitParam(name = "justAuthSocialUserIds", value = "第三方用户绑定ID列表", required = true, dataType = "List")
    public Result<?> batchDelete(@RequestBody List<Long> justAuthSocialUserIds) {
        if (CollectionUtils.isEmpty(justAuthSocialUserIds)) {
            return Result.error("第三方用户绑定ID列表不能为空");
        }
        boolean result = justAuthSocialUserService.batchDeleteJustAuthSocialUser(justAuthSocialUserIds);
        return Result.result(result);
    }


 }
