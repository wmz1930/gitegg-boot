package com.gitegg.boot.generator.dict.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 代码生成数据字典
 * </p>
 *
 * @author GitEgg
 * @since 2018-10-28
 */
@Data
@ApiModel(value = "CreateGeneratorDictDTO对象", description = "代码生成数据字典")
public class CreateGeneratorDictDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "租户")
    private Long tenantId;

    @ApiModelProperty(value = "字典类型")
    private Long parentId;

    @ApiModelProperty(value = "所有上级id的集合")
    private String ancestors;

    @ApiModelProperty(value = "字典名称")
    private String dictName;

    @ApiModelProperty(value = "字典值")
    private String dictCode;

    @ApiModelProperty(value = "排序")
    private Integer dictOrder;

    @ApiModelProperty(value = "1有效，0禁用")
    private Integer dictStatus;

    @ApiModelProperty(value = "描述信息")
    private String comments;

}
