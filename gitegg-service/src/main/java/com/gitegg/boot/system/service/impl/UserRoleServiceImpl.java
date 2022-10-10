package com.gitegg.boot.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitegg.boot.system.entity.Role;
import com.gitegg.boot.system.entity.UserRole;
import com.gitegg.boot.system.mapper.UserRoleMapper;
import com.gitegg.boot.system.service.IRoleService;
import com.gitegg.boot.system.service.IUserRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: RoleServiceImpl
 * @Description: 角色相关操作接口实现类
 * @author gitegg
 * @date 2018年5月18日 下午3:22:21
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

    private IRoleService roleService;

    @Autowired
    public void setRoleService(@Lazy IRoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    public UserRole selectByUserId(Long userId) {
        LambdaQueryWrapper<UserRole> ew = new LambdaQueryWrapper<>();
        ew.eq(UserRole::getUserId, userId);
        return this.getOne(ew);
    }

    @Override
    public List<Role> queryRolesByUserId(Long userId) {
        LambdaQueryWrapper<UserRole> ew = new LambdaQueryWrapper<>();
        ew.eq(UserRole::getUserId, userId);
        List<UserRole> userRoleList = this.list(ew);
        if (!CollectionUtils.isEmpty(userRoleList)) {
            List<Long> roleIds = new ArrayList<>();
            for (UserRole userRole : userRoleList) {
                roleIds.add(userRole.getRoleId());
            }
            List<Role> roleList = roleService.listByIds(roleIds);
            return roleList;
        }
        return null;
    }
}
