package ${package.Entity};

<#list fields as field>
    <#if field?? && field.importFlag == true && field.entityType == "LocalDateTime">
        <#assign hasDateTime=true />
    </#if>
</#list>
import com.alibaba.excel.annotation.ExcelProperty;
<#if hasDateTime?? && hasDateTime == true>
import com.alibaba.excel.annotation.format.DateTimeFormat;
</#if>
import ${mainPackagePath}.entity.${mainEntityName}Import;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
<#list table.importPackages as pkg>
    <#if !pkg?starts_with("com.baomidou.mybatisplus.annotation.") && !pkg?starts_with("com.gitegg.platform.mybatis.entity.BaseEntity")>
import ${pkg};
    </#if>
</#list>
<#if hasDateTime?? && hasDateTime == true>
import com.gitegg.platform.boot.excel.LocalDateTimeConverter;
</#if>
<#if swagger>
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
</#if>
<#if entityLombokModel>
import lombok.Data;
    <#if chainModel>
import lombok.experimental.Accessors;
    </#if>
</#if>
<#if hasDateTime?? && hasDateTime == true>

import java.time.LocalDateTime;
</#if>

/**
* <p>
* ${table.comment!}
* </p>
*
* @author ${author}
* @since ${date}
*/
@HeadRowHeight(20)
@ContentRowHeight(15)
<#if entityLombokModel>
@Data
    <#if chainModel>
@Accessors(chain = true)
    </#if>
</#if>
<#if swagger>
@ApiModel(value="${entity}对象", description="${table.comment!}数据导入模板")
</#if>
public class ${entity}Import extends ${mainEntityName}Import {

<#if entitySerialVersionUID>
    private static final long serialVersionUID = 1L;
</#if>
<#-- ----------  BEGIN 字段循环遍历  ---------->
<#assign i=0/>
<#list fields as field>
<#if field?? && field.importFlag == true>

<#if field?? && field.comment!?length gt 0>
            <#if swagger>
    @ApiModelProperty(value = "${field.comment}")
            <#else>
    /**
    * ${field.comment}
    */
            </#if>
</#if>
    @ExcelProperty(value = "${field.comment}" ,index = ${i}<#if field?? && field.entityType?? && field.entityType == "LocalDateTime">, converter = LocalDateTimeConverter.class</#if>)
    @ColumnWidth(20)
<#if field?? && field.entityType?? && field.entityType == "LocalDateTime">
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
</#if>
    private ${field.entityType} ${field.entityName};
        <#assign i=i+1/>
    </#if>
</#list>
<#------------  END 字段循环遍历  ---------->
}
