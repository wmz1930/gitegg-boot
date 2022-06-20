package com.gitegg.boot.generator.join.dto;

import com.gitegg.platform.mybatis.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 多表查询时的联合表配置
 * </p>
 *
 * @author GitEgg
 * @since 2021-09-03 11:38:07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="TableJoin对象", description="多表查询时的联合表配置")
public class QueryTableJoinDTO extends BaseEntity {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "代码生成主键")
    private Long generationId;

    @ApiModelProperty(value = "数据源和主表一致")
    private Long datasourceId;

    @ApiModelProperty(value = "模块名称")
    private String moduleName;

    @ApiModelProperty(value = "模块代码")
    private String moduleCode;

    @ApiModelProperty(value = "controller请求路径")
    private String controllerPath;

    @ApiModelProperty(value = "表名")
    private String joinTableName;

    @ApiModelProperty(value = "表别名")
    private String joinTableAlias;

    @ApiModelProperty(value = "表前缀")
    private String joinTablePrefix;

    @ApiModelProperty(value = "left左连接 right右连接 inner等值连接 union联合查询")
    private String joinTableType;

    @ApiModelProperty(value = "自定义查询字段")
    private String joinTableSelect;

    @ApiModelProperty(value = "和主表关联的id")
    private String associationId;

    @ApiModelProperty(value = "自定义on条件")
    private String joinTableOn;

    @ApiModelProperty(value = "开始时间")
    private String startDateTime;

    @ApiModelProperty(value = "结束时间")
    private String endDateTime;

    @ApiModelProperty(value = "显示排序")
    private Integer tableSort;

}
