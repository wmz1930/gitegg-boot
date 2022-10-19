package com.gitegg.boot.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitegg.boot.system.dto.CreateUserDTO;
import com.gitegg.boot.system.dto.QueryUserDTO;
import com.gitegg.boot.system.dto.UpdateDataPermissionUserDTO;
import com.gitegg.boot.system.dto.UpdateUserDTO;
import com.gitegg.boot.system.entity.User;
import com.gitegg.boot.system.entity.UserInfo;
import com.gitegg.boot.system.service.IDataPermissionUserService;
import com.gitegg.boot.system.service.IUserService;
import com.gitegg.platform.base.annotation.resubmit.ResubmitLock;
import com.gitegg.platform.base.constant.GitEggConstant;
import com.gitegg.platform.base.dto.CheckExistDTO;
import com.gitegg.platform.base.enums.ResultCodeEnum;
import com.gitegg.platform.base.result.PageResult;
import com.gitegg.platform.base.result.Result;
import com.gitegg.platform.base.util.BeanCopierUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: UserController
 * @Description: User前端控制器
 * @author gitegg
 * @date 2019年5月18日 下午4:03:58
 */
@RestController
@RequestMapping(value = "/system/user")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "UserController|用户相关的前端控制器", tags = {"用户配置"})
public class UserController {

    private final IUserService userService;

    private final IDataPermissionUserService dataPermissionUserService;

    @Value("${system.defaultPwd}")
    private String defaultPwd;

    /**
     * 查询所有用户
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "realName", value = "用户名", required = false, dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "mobile", value = "手机号", required = false, dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "email", value = "邮箱", required = false, dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "roleId", value = "角色", required = false, dataTypeClass = Integer.class, paramType = "query"),
            @ApiImplicitParam(name = "status", value = "用户状态", required = false, dataTypeClass = Integer.class, paramType = "query"),
            @ApiImplicitParam(name = "size", value = "每页条数", required = false, dataTypeClass = Integer.class, paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", required = false, dataTypeClass = Integer.class, paramType = "query") })
//    @ResubmitLock(interval = 60, argsIndex = {0}, ignoreKeys = {"email","status"})
    public PageResult<UserInfo> list(@ApiIgnore QueryUserDTO user, @ApiIgnore Page<UserInfo> page) {
        Page<UserInfo> pageUser = userService.selectUserList(page, user);
        PageResult<UserInfo> pageResult = new PageResult<>(pageUser.getTotal(), pageUser.getRecords());
        return pageResult;
    }

    /**
     * 添加用户
     */
    @PostMapping("/create")
    @ApiOperation(value = "添加用户")
    @ResubmitLock(interval = 10)
    public Result<?> create(@RequestBody @Valid CreateUserDTO user) {
        CreateUserDTO userDTO = userService.createUser(user);
        return Result.data(userDTO.getId());
    }

    /**
     * 修改用户
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新用户信息")
    public Result<?> update(@RequestBody UpdateUserDTO user) {
        boolean result = userService.updateUser(user);
        if (result) {
            return Result.success();
        } else {
            return Result.error(ResultCodeEnum.FAILED);
        }
    }

    /**
     * 删除用户
     */
    @PostMapping("/delete/{userId}")
    @ApiOperation(value = "删除用户")
    @ApiImplicitParam(paramType = "path", name = "userId", value = "用户ID", required = true, dataTypeClass = Long.class)
    public Result<?> delete(@PathVariable("userId") Long userId) {
        if (null == userId) {
            return Result.error("用户ID不能为空");
        }
        boolean result = userService.deleteUser(userId);
        if (result) {
            return Result.success();
        } else {
            return Result.error(ResultCodeEnum.FAILED);
        }
    }

    /**
     * 批量删除用户
     */
    @PostMapping("/batch/delete")
    @ApiOperation(value = "批量删除用户")
    @ApiImplicitParam(name = "userIds", value = "用户ID列表", required = true, dataTypeClass = List.class)
    public Result<?> batchDelete(@RequestBody List<Long> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return Result.error("用户ID列表不能为空");
        }
        boolean result = userService.batchDeleteUser(userIds);
        if (result) {
            return Result.success();
        } else {
            return Result.error(ResultCodeEnum.FAILED);
        }
    }

    /**
     * 修改自己密码
     */
    @PostMapping("/password/change")
    @ApiOperation(value = "用户修改密码")
    public Result<?> updatePassword(@RequestBody UpdateUserDTO userUpdate, @ApiIgnore User tempUser) {
        String newPwd = userUpdate.getNewPwd();
        String oldPwd = userUpdate.getOldPwd();
        if (StringUtils.isEmpty(newPwd) || StringUtils.isEmpty(oldPwd)) {
            return Result.error("密码不能为空");
        }
        if (tempUser == null || !BCrypt.checkpw(tempUser.getAccount() + oldPwd, tempUser.getPassword())) {
            return Result.error("原密码错误");
        }
        UpdateUserDTO user = new UpdateUserDTO();
        user.setId(tempUser.getId());
        user.setPassword(newPwd);
        boolean result = userService.updateUser(user);
        if (result) {
            return Result.success();
        } else {
            return Result.error(ResultCodeEnum.FAILED);
        }
    }

    /**
     * 重置密码
     */
    @PostMapping("password/reset/{userId}")
    @ApiOperation(value = "管理员重置用户密码")
    @ApiImplicitParam(paramType = "path", name = "userId", value = "用户ID", required = true, dataTypeClass = Long.class)
    public Result<?> resetPassword(@PathVariable("userId") Long userId) {
        if (null == userId) {
            return Result.error("用户ID不能为空");
        }
        User oldInfo = userService.getById(userId);
        UpdateUserDTO user = BeanCopierUtils.copyByClass(oldInfo, UpdateUserDTO.class);
        user.setId(userId);
        user.setPassword(defaultPwd);
        boolean result = userService.updateUser(user);
        if (result) {
            return Result.success();
        } else {
            return Result.error(ResultCodeEnum.FAILED);
        }
    }

    /**
     * 修改用户状态
     */
    @PostMapping("/status/{userId}/{status}")
    @ApiOperation(value = "管理员修改用户状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataTypeClass = Long.class, paramType = "path"),
        @ApiImplicitParam(name = "status", value = "用户状态", required = true, dataTypeClass = Integer.class, paramType = "path")})
    public Result<?> updateStatus(@PathVariable("userId") Long userId, @PathVariable("status") Integer status) {
        if (null == userId || StringUtils.isEmpty(status)) {
            return Result.error("ID和状态不能为空");
        }
        UpdateUserDTO user = new UpdateUserDTO();
        user.setId(userId);
        user.setStatus(status);
        boolean result = userService.updateUser(user);
        if (result) {
            return Result.success();
        } else {
            return Result.error(ResultCodeEnum.FAILED);
        }
    }

    /**
     * 个人修改用户信息，限制修改字段
     */
    @PostMapping("/update/info")
    @ApiOperation(value = "用户修改个人信息")
    public Result<?> updateInfo(@RequestBody UpdateUserDTO user, @ApiIgnore User tempUser) {
        UpdateUserDTO upUser = new UpdateUserDTO();
        upUser.setAvatar(user.getAvatar());
        upUser.setRealName(user.getRealName());
        upUser.setNickname(user.getNickname());
        upUser.setAreas(user.getAreas());
        upUser.setId(tempUser.getId());
        boolean result = userService.updateUser(user);
        if (result) {
            return Result.success();
        } else {
            return Result.error(ResultCodeEnum.FAILED);
        }
    }

    /**
     * 更新用户数据权限
     */
    @PostMapping("/update/organization/data/permission")
    @ApiOperation(value = "更新用户数据权限")
    public Result<?> updateUserDataPermission(@RequestBody UpdateDataPermissionUserDTO updateDataPermission) {
        boolean result = dataPermissionUserService.updateUserOrganizationDataPermission(updateDataPermission);
        if (result) {
            return Result.success();
        } else {
            return Result.error(ResultCodeEnum.FAILED);
        }
    }

    /**
     * 校验用户账号是否存在
     * 
     * @param user
     * @return
     */
    @PostMapping(value = "/check")
    @ApiOperation(value = "校验用户账号是否存在", notes = "校验用户账号是否存在")
    public Result<Boolean> checkUserExist(CheckExistDTO user) {
        String field = user.getCheckField();
        String value = user.getCheckValue();
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq(field, value);
        if(null != user && null != user.getId()) {
            userQueryWrapper.ne("id", user.getId());
        }
        int count = userService.count(userQueryWrapper);
        if (GitEggConstant.COUNT_ZERO == count){
            return Result.data(true);
        } else {
            return Result.data(false);
        }
    }

    /**
     * 查询所有用户
     */
    @GetMapping("/organization/data/permission/list")
    @ApiOperation(value = "分页查询机构权限下的用户列表")
    public PageResult<UserInfo> organizationDataUserList(@ApiIgnore QueryUserDTO user, @ApiIgnore Page<UserInfo> page) {
        if(null == user.getOrganizationId())
        {
            return new PageResult(GitEggConstant.COUNT_ZERO, new ArrayList<>());
        }
        Page<UserInfo> pageUser = dataPermissionUserService.selectOrganizationUserList(page, user);
        PageResult<UserInfo> pageResult = new PageResult<>(pageUser.getTotal(), pageUser.getRecords());
        return pageResult;
    }

    /**
     * 批量删除机构下的用户权限关系
     */
    @PostMapping("/organization/data/permission/batch/delete")
    @ApiOperation(value = "批量删除机构下的用户权限关系")
    @ApiImplicitParam(name = "dataPermissionUserIds", value = "ID列表", required = true, dataTypeClass = List.class)
    public Result<?> organizationDataUserBatchDelete(@RequestBody List<Long> dataPermissionUserIds) {
        if (CollectionUtils.isEmpty(dataPermissionUserIds)) {
            return Result.error("ID列表不能为空");
        }
        boolean result = dataPermissionUserService.batchDeleteDataPermissionUser(dataPermissionUserIds);
        return Result.result(result);
    }
}
