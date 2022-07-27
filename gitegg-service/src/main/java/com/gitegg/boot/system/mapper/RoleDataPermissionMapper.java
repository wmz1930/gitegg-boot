package com.gitegg.boot.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitegg.boot.system.dto.QueryRoleDataPermissionDTO;
import com.gitegg.boot.system.dto.RoleDataPermissionDTO;
import com.gitegg.boot.system.entity.RoleDataPermission;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 角色和数据权限关联表 Mapper 接口
 * </p>
 *
 * @author GitEgg
 * @since 2021-05-14
 */
public interface RoleDataPermissionMapper extends BaseMapper<RoleDataPermission> {

    /**
    * 查询角色和数据权限关联表列表
    * @param page
    * @param roleDataPermissionDTO
    * @return
    */
    Page<RoleDataPermissionDTO> queryRoleDataPermissionList(Page<RoleDataPermissionDTO> page, @Param("roleDataPermission") QueryRoleDataPermissionDTO roleDataPermissionDTO);

    /**
    * 查询角色和数据权限关联表信息
    * @param roleDataPermissionDTO
    * @return
    */
    RoleDataPermissionDTO queryRoleDataPermission(@Param("roleDataPermission") QueryRoleDataPermissionDTO roleDataPermissionDTO);
}
