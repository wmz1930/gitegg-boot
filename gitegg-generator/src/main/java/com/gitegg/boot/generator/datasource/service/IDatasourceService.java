package com.gitegg.boot.generator.datasource.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gitegg.boot.generator.datasource.dto.CreateDatasourceDTO;
import com.gitegg.boot.generator.datasource.dto.DatasourceDTO;
import com.gitegg.boot.generator.datasource.dto.QueryDatasourceDTO;
import com.gitegg.boot.generator.datasource.dto.UpdateDatasourceDTO;
import com.gitegg.boot.generator.datasource.entity.Datasource;

import java.util.List;

/**
 * <p>
 * 数据源配置表 服务类
 * </p>
 *
 * @author GitEgg
 * @since 2021-08-18 16:39:49
 */
public interface IDatasourceService extends IService<Datasource> {

    /**
    * 分页查询数据源配置表列表
    * @param page
    * @param queryDatasourceDTO
    * @return
    */
    Page<DatasourceDTO> queryDatasourceList(Page<DatasourceDTO> page, QueryDatasourceDTO queryDatasourceDTO);

    /**
     * 查询数据源配置表列表
     * @param queryDatasourceDTO
     * @return
     */
    List<DatasourceDTO> queryDatasourceList(QueryDatasourceDTO queryDatasourceDTO);

    /**
    * 查询数据源配置表详情
    * @param queryDatasourceDTO
    * @return
    */
    DatasourceDTO queryDatasource(QueryDatasourceDTO queryDatasourceDTO);

    /**
    * 创建数据源配置表
    * @param datasource
    * @return
    */
    boolean createDatasource(CreateDatasourceDTO datasource);

    /**
    * 更新数据源配置表
    * @param datasource
    * @return
    */
    boolean updateDatasource(UpdateDatasourceDTO datasource);

    /**
    * 删除数据源配置表
    * @param datasourceId
    * @return
    */
    boolean deleteDatasource(Long datasourceId);

    /**
    * 批量删除数据源配置表
    * @param datasourceIds
    * @return
    */
    boolean batchDeleteDatasource(List<Long> datasourceIds);
}
