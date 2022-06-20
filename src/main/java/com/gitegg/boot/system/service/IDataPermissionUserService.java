package com.gitegg.boot.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gitegg.boot.system.dto.*;
import com.gitegg.boot.system.entity.DataPermissionUser;
import com.gitegg.boot.system.entity.UserInfo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author GitEgg
 * @since 2021-05-13
 */
public interface IDataPermissionUserService extends IService<DataPermissionUser> {

    /**
    * 分页查询列表
    * @param page
    * @param queryDataPermissionUserDTO
    * @return
    */
    Page<DataPermissionUserDTO> queryDataPermissionUserList(Page<DataPermissionUserDTO> page, QueryDataPermissionUserDTO queryDataPermissionUserDTO);

    /**
    * 查询详情
    * @param queryDataPermissionUserDTO
    * @return
    */
    DataPermissionUserDTO queryDataPermissionUser(QueryDataPermissionUserDTO queryDataPermissionUserDTO);

    /**
    * 创建
    * @param dataPermissionUser
    * @return
    */
    boolean createDataPermissionUser(CreateDataPermissionUserDTO dataPermissionUser);

    /**
    * 更新
    * @param dataPermissionUser
    * @return
    */
    boolean updateDataPermissionUser(UpdateDataPermissionUserDTO dataPermissionUser);

    /**
    * 删除
    * @param dataPermissionUserId
    * @return
    */
    boolean deleteDataPermissionUser(Long dataPermissionUserId);

    /**
    * 批量删除
    * @param dataPermissionUserIds
    * @return
    */
    boolean batchDeleteDataPermissionUser(List<Long> dataPermissionUserIds);

    boolean updateUserOrganizationDataPermission(UpdateDataPermissionUserDTO updateDataPermission);

    Page<UserInfo> selectOrganizationUserList(Page<UserInfo> page, QueryUserDTO user);
}
