package com.gitegg.boot.generator.validate.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitegg.boot.generator.validate.dto.QueryValidateDTO;
import com.gitegg.boot.generator.validate.dto.ValidateDTO;
import com.gitegg.boot.generator.validate.entity.Validate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 字段校验规则配置表 Mapper 接口
 * </p>
 *
 * @author GitEgg
 * @since 2021-10-15
 */
public interface ValidateMapper extends BaseMapper<Validate> {

    /**
    * 分页查询字段校验规则配置表列表
    * @param page
    * @param validateDTO
    * @return
    */
    Page<ValidateDTO> queryValidateList(Page<ValidateDTO> page, @Param("validate") QueryValidateDTO validateDTO);

    /**
    * 查询字段校验规则配置表列表
    * @param validateDTO
    * @return
    */
    List<ValidateDTO> queryValidateList(@Param("validate") QueryValidateDTO validateDTO);

    /**
    * 查询字段校验规则配置表信息
    * @param validateDTO
    * @return
    */
    ValidateDTO queryValidate(@Param("validate") QueryValidateDTO validateDTO);
}
