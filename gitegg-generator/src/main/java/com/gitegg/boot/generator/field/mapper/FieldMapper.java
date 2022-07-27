package com.gitegg.boot.generator.field.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitegg.boot.generator.field.dto.FieldDTO;
import com.gitegg.boot.generator.field.dto.QueryFieldDTO;
import com.gitegg.boot.generator.field.entity.Field;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 字段属性配置表 Mapper 接口
 * </p>
 *
 * @author GitEgg
 * @since 2021-09-03 11:55:34
 */
public interface FieldMapper extends BaseMapper<Field> {

    /**
    * 查询字段属性配置表列表
    * @param page
    * @param fieldDTO
    * @return
    */
    Page<FieldDTO> queryFieldList(Page<FieldDTO> page, @Param("field") QueryFieldDTO fieldDTO);

    /**
     * 查询所有字段属性配置表列表
     * @param fieldDTO
     * @return
     */
    List<FieldDTO> queryFieldList(@Param("field") QueryFieldDTO fieldDTO);

    /**
    * 查询字段属性配置表信息
    * @param fieldDTO
    * @return
    */
    FieldDTO queryField(@Param("field") QueryFieldDTO fieldDTO);
}
