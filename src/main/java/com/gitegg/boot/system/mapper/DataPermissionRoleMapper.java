package com.gitegg.boot.system.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitegg.boot.system.dto.DataPermissionRoleDTO;
import com.gitegg.boot.system.dto.QueryDataPermissionRoleDTO;
import com.gitegg.boot.system.entity.DataPermissionRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 数据权限配置表 Mapper 接口
 * </p>
 *
 * @author GitEgg
 * @since 2021-05-13
 */
public interface DataPermissionRoleMapper extends BaseMapper<DataPermissionRole> {

    /**
    * 查询数据权限配置表列表
    * @param page
    * @param dataPermissionRoleDTO
    * @return
    */
    Page<DataPermissionRoleDTO> queryDataPermissionRoleList(Page<DataPermissionRoleDTO> page, @Param("dataPermissionRole") QueryDataPermissionRoleDTO dataPermissionRoleDTO);

    /**
    * 查询数据权限配置表信息
    * @param dataPermissionRoleDTO
    * @return
    */
    DataPermissionRoleDTO queryDataPermissionRole(@Param("dataPermissionRole") QueryDataPermissionRoleDTO dataPermissionRoleDTO);

    /**
     * 初始化数据权限缓存, 添加InterceptorIgnore注解，初始化时忽略租户拦截
     * @return
     */
    @InterceptorIgnore(tenantLine = "true")
    List<DataPermissionRoleDTO> queryDataPermissionRoleListAll();
}
