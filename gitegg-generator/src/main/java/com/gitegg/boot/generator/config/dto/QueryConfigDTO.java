package com.gitegg.boot.generator.config.dto;

import com.gitegg.platform.mybatis.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 代码生成配置表
 * </p>
 *
 * @author GitEgg
 * @since 2021-09-02 18:09:28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="Config对象", description="代码生成配置表")
public class QueryConfigDTO extends BaseEntity {


    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "数据源")
    private Long datasourceId;

    @ApiModelProperty(value = "模块名称")
    private String moduleName;

    @ApiModelProperty(value = "模块代码")
    private String moduleCode;

    @ApiModelProperty(value = "服务名称")
    private String serviceName;

    @ApiModelProperty(value = "表名")
    private String tableName;

    @ApiModelProperty(value = "表别名")
    private String tableAlias;

    @ApiModelProperty(value = "表前缀")
    private String tablePrefix;

    @ApiModelProperty(value = "父级包名")
    private String parentPackage;

    @ApiModelProperty(value = "controller请求路径")
    private String controllerPath;

    @ApiModelProperty(value = "表单类型 model弹出框  drawer抽屉  tab新页面")
    private String formType;

    @ApiModelProperty(value = "表类型 single单表 join_query多表查询 main_sub主表子表")
    private String tableType;

    @ApiModelProperty(value = "主表子表时是否使用继承 1使用 0不使用")
    private Boolean extendsFlag;

    @ApiModelProperty(value = "展示类型 table数据表格 tree_table 树表格 3 left_tree_table左树右表  tree数据树")
    private String tableShowType;

    @ApiModelProperty(value = "表单字段排列 1一列一行  2 两列一行")
    private String formItemCol;

    @ApiModelProperty(value = "左树类型 organization机构树 resource资源权限树 ")
    private String leftTreeType;

    @ApiModelProperty(value = "前端代码路径")
    private String frontCodePath;

    @ApiModelProperty(value = "后端代码路径")
    private String serviceCodePath;

    @ApiModelProperty(value = "页面代码目录")
    private String frontCodeDir;

    @ApiModelProperty(value = "是否支持导入 1支持 0不支持")
    private Boolean importFlag;

    @ApiModelProperty(value = "是否支持导出 1支持 0不支持")
    private Boolean exportFlag;

    @ApiModelProperty(value = "配置复制类型 Table只复制table配置  TableField全部复制")
    private String copyType;

    @ApiModelProperty(value = "开始时间")
    private String startDateTime;

    @ApiModelProperty(value = "结束时间")
    private String endDateTime;

    @ApiModelProperty(value = "查询复用： 查询复用：分页查询和单条记录查询公用同一个sql语句")
    private Boolean queryReuse;

    @ApiModelProperty(value = "状态处理")
    private Boolean statusHandling;

    @ApiModelProperty(value = "代码生成类型")
    private String codeType;

}
