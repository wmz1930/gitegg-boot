package com.gitegg.boot.system.dto;

import com.gitegg.platform.mybatis.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 数据权限配置表
 * </p>
 *
 * @author GitEgg
 * @since 2021-05-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="DataPermissionRole对象", description="数据权限配置表")
public class CreateDataPermissionRoleDTO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "功能权限id")
    private Long resourceId;

    @ApiModelProperty(value = "数据权限名称")
    private String dataName;

    @ApiModelProperty(value = "数据权限对应的mapper方法全路径")
    private String dataMapperFunction;

    @ApiModelProperty(value = "需要做数据权限主表")
    private String dataTableName;

    @ApiModelProperty(value = "需要做数据权限表的别名")
    private String dataTableAlias;

    @ApiModelProperty(value = "数据权限需要排除的字段")
    private String dataColumnExclude;

    @ApiModelProperty(value = "数据权限需要保留的字段")
    private String dataColumnInclude;

    @ApiModelProperty(value = "数据权限表,默认t_sys_organization")
    private String innerTableName;

    @ApiModelProperty(value = "数据权限表的别名,默认organization")
    private String innerTableAlias;

    @ApiModelProperty(value = "数据权限类型:1只能查看本人 2只能查看本部门 3只能查看本部门及子部门 4可以查看所有数据")
    private String dataPermissionType;

    @ApiModelProperty(value = "自定义数据权限类型")
    private String customExpression;

    @ApiModelProperty(value = "状态 0禁用，1 启用,")
    private Integer status;

    @ApiModelProperty(value = "备注")
    private String comments;


}
