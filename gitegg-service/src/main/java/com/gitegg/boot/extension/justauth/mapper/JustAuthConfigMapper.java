package com.gitegg.boot.extension.justauth.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitegg.boot.extension.justauth.dto.JustAuthConfigDTO;
import com.gitegg.boot.extension.justauth.dto.QueryJustAuthConfigDTO;
import com.gitegg.boot.extension.justauth.entity.JustAuthConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 租户第三方登录功能配置表 Mapper 接口
 * </p>
 *
 * @author GitEgg
 * @since 2022-05-16
 */
public interface JustAuthConfigMapper extends BaseMapper<JustAuthConfig> {

    /**
    * 分页查询租户第三方登录功能配置表列表
    * @param page
    * @param justAuthConfigDTO
    * @return
    */
    Page<JustAuthConfigDTO> queryJustAuthConfigList(Page<JustAuthConfigDTO> page, @Param("justAuthConfig") QueryJustAuthConfigDTO justAuthConfigDTO);

    /**
    * 查询租户第三方登录功能配置表列表
    * @param justAuthConfigDTO
    * @return
    */
    List<JustAuthConfigDTO> queryJustAuthConfigList(@Param("justAuthConfig") QueryJustAuthConfigDTO justAuthConfigDTO);

    /**
    * 查询租户第三方登录功能配置表信息
    * @param justAuthConfigDTO
    * @return
    */
    JustAuthConfigDTO queryJustAuthConfig(@Param("justAuthConfig") QueryJustAuthConfigDTO justAuthConfigDTO);
    
    /**
     * 查询租户第三方登录功能配置表列表
     * @param justAuthConfigDTO
     * @return
     */
    @InterceptorIgnore(tenantLine = "true")
    List<JustAuthConfigDTO> initJustAuthConfigList(@Param("justAuthConfig") QueryJustAuthConfigDTO justAuthConfigDTO);
}
