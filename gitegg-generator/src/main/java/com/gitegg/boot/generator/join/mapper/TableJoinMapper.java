package com.gitegg.boot.generator.join.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitegg.boot.generator.join.dto.QueryTableJoinDTO;
import com.gitegg.boot.generator.join.dto.TableJoinDTO;
import com.gitegg.boot.generator.join.entity.TableJoin;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 多表查询时的联合表配置 Mapper 接口
 * </p>
 *
 * @author GitEgg
 * @since 2021-09-03 11:38:07
 */
public interface TableJoinMapper extends BaseMapper<TableJoin> {

    /**
    * 查询多表查询时的联合表配置列表
    * @param page
    * @param tableJoinDTO
    * @return
    */
    Page<TableJoinDTO> queryTableJoinList(Page<TableJoinDTO> page, @Param("tableJoin") QueryTableJoinDTO tableJoinDTO);

    /**
    * 查询多表查询时的联合表配置信息
    * @param tableJoinDTO
    * @return
    */
    TableJoinDTO queryTableJoin(@Param("tableJoin") QueryTableJoinDTO tableJoinDTO);
}
