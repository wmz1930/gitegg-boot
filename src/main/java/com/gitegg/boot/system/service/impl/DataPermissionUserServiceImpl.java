package com.gitegg.boot.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitegg.boot.system.dto.*;
import com.gitegg.boot.system.entity.DataPermissionUser;
import com.gitegg.boot.system.entity.UserInfo;
import com.gitegg.boot.system.mapper.DataPermissionUserMapper;
import com.gitegg.boot.system.service.IDataPermissionUserService;
import com.gitegg.platform.base.util.BeanCopierUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author GitEgg
 * @since 2021-05-13
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DataPermissionUserServiceImpl extends ServiceImpl<DataPermissionUserMapper, DataPermissionUser> implements IDataPermissionUserService {

    private final DataPermissionUserMapper dataPermissionUserMapper;

    /**
    * 分页查询列表
    * @param page
    * @param queryDataPermissionUserDTO
    * @return
    */
    @Override
    public Page<DataPermissionUserDTO> queryDataPermissionUserList(Page<DataPermissionUserDTO> page, QueryDataPermissionUserDTO queryDataPermissionUserDTO) {
        Page<DataPermissionUserDTO> dataPermissionUserInfoList = dataPermissionUserMapper.queryDataPermissionUserList(page, queryDataPermissionUserDTO);
        return dataPermissionUserInfoList;
    }

    /**
    * 查询详情
    * @param queryDataPermissionUserDTO
    * @return
    */
    @Override
    public DataPermissionUserDTO queryDataPermissionUser(QueryDataPermissionUserDTO queryDataPermissionUserDTO) {
        DataPermissionUserDTO dataPermissionUserDTO = dataPermissionUserMapper.queryDataPermissionUser(queryDataPermissionUserDTO);
        return dataPermissionUserDTO;
    }

    /**
    * 创建
    * @param dataPermissionUser
    * @return
    */
    @Override
    public boolean createDataPermissionUser(CreateDataPermissionUserDTO dataPermissionUser) {
        DataPermissionUser dataPermissionUserEntity = BeanCopierUtils.copyByClass(dataPermissionUser, DataPermissionUser.class);
        boolean result = this.save(dataPermissionUserEntity);
        return result;
    }

    /**
    * 更新
    * @param dataPermissionUser
    * @return
    */
    @Override
    public boolean updateDataPermissionUser(UpdateDataPermissionUserDTO dataPermissionUser) {
        DataPermissionUser dataPermissionUserEntity = BeanCopierUtils.copyByClass(dataPermissionUser, DataPermissionUser.class);
        boolean result = this.updateById(dataPermissionUserEntity);
        return result;
    }

    /**
    * 删除
    * @param dataPermissionUserId
    * @return
    */
    @Override
    public boolean deleteDataPermissionUser(Long dataPermissionUserId) {
        boolean result = this.removeById(dataPermissionUserId);
        return result;
    }

    /**
    * 批量删除
    * @param dataPermissionUserIds
    * @return
    */
    @Override
    public boolean batchDeleteDataPermissionUser(List<Long> dataPermissionUserIds) {
        boolean result = this.removeByIds(dataPermissionUserIds);
        return result;
    }

    /**
     * 更新用户的机构权限
     * @param updateDataPermission
     * @return
     */
    @Override
    public boolean updateUserOrganizationDataPermission(UpdateDataPermissionUserDTO updateDataPermission){
        boolean result = false;
        Long userId = updateDataPermission.getUserId();

        List<Long> removeDataPermissions = updateDataPermission.getRemoveDataPermissions();
        if (!CollectionUtils.isEmpty(removeDataPermissions) && null != userId)
        {
            LambdaQueryWrapper<DataPermissionUser> removeWrapper = new LambdaQueryWrapper<>();
            removeWrapper.eq(DataPermissionUser::getUserId, userId).in(DataPermissionUser::getOrganizationId, removeDataPermissions);
            result = this.remove(removeWrapper);
        }

        List<Long> addDataPermissions = updateDataPermission.getAddDataPermissions();
        if (!CollectionUtils.isEmpty(addDataPermissions) && null != userId)
        {
            List<DataPermissionUser> dataPermissionList = new ArrayList<>();
            for (Long dataId: addDataPermissions)
            {
                DataPermissionUser dataPermission = new DataPermissionUser();
                dataPermission.setOrganizationId(dataId);
                dataPermission.setUserId(userId);
                dataPermissionList.add(dataPermission);
            }
            result = this.saveBatch(dataPermissionList);
        }
        return result;
    }

    @Override
    public Page<UserInfo> selectOrganizationUserList(Page<UserInfo> page, QueryUserDTO user) {
        Page<UserInfo> pageUserInfo = dataPermissionUserMapper.selectOrganizationUserList(page, user);
        return pageUserInfo;
    }
}
