package com.gitegg.boot.extension.justauth.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gitegg.boot.extension.justauth.dto.CreateJustAuthConfigDTO;
import com.gitegg.boot.extension.justauth.dto.JustAuthConfigDTO;
import com.gitegg.boot.extension.justauth.dto.QueryJustAuthConfigDTO;
import com.gitegg.boot.extension.justauth.dto.UpdateJustAuthConfigDTO;
import com.gitegg.boot.extension.justauth.entity.JustAuthConfig;

import java.util.List;

/**
 * <p>
 * 租户第三方登录功能配置表 服务类
 * </p>
 *
 * @author GitEgg
 * @since 2022-05-16
 */
public interface IJustAuthConfigService extends IService<JustAuthConfig> {

    /**
    * 分页查询租户第三方登录功能配置表列表
    * @param page
    * @param queryJustAuthConfigDTO
    * @return
    */
    Page<JustAuthConfigDTO> queryJustAuthConfigList(Page<JustAuthConfigDTO> page, QueryJustAuthConfigDTO queryJustAuthConfigDTO);

    /**
    * 查询租户第三方登录功能配置表列表
    * @param queryJustAuthConfigDTO
    * @return
    */
    List<JustAuthConfigDTO> queryJustAuthConfigList(QueryJustAuthConfigDTO queryJustAuthConfigDTO);

    /**
    * 查询租户第三方登录功能配置表详情
    * @param queryJustAuthConfigDTO
    * @return
    */
    JustAuthConfigDTO queryJustAuthConfig(QueryJustAuthConfigDTO queryJustAuthConfigDTO);

    /**
    * 创建租户第三方登录功能配置表
    * @param justAuthConfig
    * @return
    */
    boolean createJustAuthConfig(CreateJustAuthConfigDTO justAuthConfig);

    /**
    * 更新租户第三方登录功能配置表
    * @param justAuthConfig
    * @return
    */
    boolean updateJustAuthConfig(UpdateJustAuthConfigDTO justAuthConfig);

    /**
    * 删除租户第三方登录功能配置表
    * @param justAuthConfigId
    * @return
    */
    boolean deleteJustAuthConfig(Long justAuthConfigId);

    /**
    * 批量删除租户第三方登录功能配置表
    * @param justAuthConfigIds
    * @return
    */
    boolean batchDeleteJustAuthConfig(List<Long> justAuthConfigIds);
    
    /**
     * 初始化配置表列表
     * @return
     */
    void initJustAuthConfigList();
}
