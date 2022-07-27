package com.gitegg.boot.generator.config.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitegg.boot.generator.config.dto.ConfigDTO;
import com.gitegg.boot.generator.config.dto.QueryConfigDTO;
import com.gitegg.boot.generator.config.entity.Config;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 代码生成配置表 Mapper 接口
 * </p>
 *
 * @author GitEgg
 * @since 2021-09-02 18:09:28
 */
public interface ConfigMapper extends BaseMapper<Config> {

    /**
    * 查询代码生成配置表列表
    * @param page
    * @param configDTO
    * @return
    */
    Page<ConfigDTO> queryConfigList(Page<ConfigDTO> page, @Param("config") QueryConfigDTO configDTO);

    /**
    * 查询代码生成配置表信息
    * @param configDTO
    * @return
    */
    ConfigDTO queryConfig(@Param("config") QueryConfigDTO configDTO);
}
