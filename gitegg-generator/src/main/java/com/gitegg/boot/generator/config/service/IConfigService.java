package com.gitegg.boot.generator.config.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gitegg.boot.generator.config.dto.ConfigDTO;
import com.gitegg.boot.generator.config.dto.CreateConfigDTO;
import com.gitegg.boot.generator.config.dto.QueryConfigDTO;
import com.gitegg.boot.generator.config.dto.UpdateConfigDTO;
import com.gitegg.boot.generator.config.entity.Config;

import java.util.List;

/**
 * <p>
 * 代码生成配置表 服务类
 * </p>
 *
 * @author GitEgg
 * @since 2021-09-02 18:09:28
 */
public interface IConfigService extends IService<Config> {

    /**
    * 分页查询代码生成配置表列表
    * @param page
    * @param queryConfigDTO
    * @return
    */
    Page<ConfigDTO> queryConfigList(Page<ConfigDTO> page, QueryConfigDTO queryConfigDTO);

    /**
    * 查询代码生成配置表详情
    * @param queryConfigDTO
    * @return
    */
    ConfigDTO queryConfig(QueryConfigDTO queryConfigDTO);

    /**
    * 创建代码生成配置表
    * @param config
    * @return
    */
    boolean createConfig(CreateConfigDTO config);

    /**
    * 更新代码生成配置表
    * @param config
    * @return
    */
    boolean updateConfig(UpdateConfigDTO config);

    /**
    * 删除代码生成配置表
    * @param configId
    * @return
    */
    boolean deleteConfig(Long configId);

    /**
    * 批量删除代码生成配置表
    * @param configIds
    * @return
    */
    boolean batchDeleteConfig(List<Long> configIds);

    /**
     * 复制代码生成配置表
     * @param queryConfigDTO
     * @return
     */
    boolean copyConfig(QueryConfigDTO queryConfigDTO);
}
