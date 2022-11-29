package com.gitegg.boot.base.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 数据字典
 * </p>
 *
 * @author GitEgg
 * @since 2018-10-28
 */
@Data
@ApiModel(value = "DictInfo对象", description = "数据字典")
public class QueryDictBusinessDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

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

    @ApiModelProperty(value = "是否是叶子节点(查询时，如果此值为 1，则表示只查询子节点)")
    private Integer isLeaf;

    @ApiModelProperty(value = "字典列表")
    private List<DictDTO> children = new ArrayList<>();

}
