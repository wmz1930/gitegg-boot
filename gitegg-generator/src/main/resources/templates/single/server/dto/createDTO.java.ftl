<#assign dtoPackage="${package.Entity}"/>
package ${dtoPackage?replace("entity","dto")};

<#list table.importPackages as pkg>
    <#if !pkg?starts_with("com.baomidou.mybatisplus.annotation.") && !pkg?starts_with("java.io.Serializable")>
import ${pkg};
    </#if>
</#list>

<#list fields as field>
    <#if field.controlType == "DTAE_TIME_PICKER" || field.controlType == "DTAE_PICKER" || field.controlType == "TIME_PICKER">
        <#assign hasJsonFormat= true/>
    </#if>
</#list>
<#if hasJsonFormat?? && hasJsonFormat == true>
import com.fasterxml.jackson.annotation.JsonFormat;
</#if>
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Min;
import javax.validation.constraints.Max;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.Pattern;

<#if swagger>
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
</#if>
<#if entityLombokModel>
import lombok.Data;
import lombok.EqualsAndHashCode;
    <#if chainModel>
import lombok.experimental.Accessors;
    </#if>
</#if>

/**
 * <p>
 * ${table.comment!}
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if entityLombokModel>
@Data
    <#if superEntityClass??>
@EqualsAndHashCode(callSuper = true)
    <#else>
@EqualsAndHashCode(callSuper = false)
    </#if>
    <#if chainModel>
@Accessors(chain = true)
    </#if>
</#if>
<#if swagger>
@ApiModel(value="${entity}对象", description="${table.comment!}")
</#if>
<#if superEntityClass??>
public class Create${entity}DTO extends ${superEntityClass}<#if activeRecord><${entity}></#if> {
<#elseif activeRecord>
public class Create${entity}DTO extends Model<${entity}> {
<#else>
public class Create${entity}DTO implements Serializable {
</#if>

<#if entitySerialVersionUID>
    private static final long serialVersionUID = 1L;
</#if>
<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list fields as field>
    <#if field?? && field.formAdd == true && (config.tableType != " join" || (config.tableType == "join" && field.joinId == 0))>

    <#if field.comment!?length gt 0>
        <#if swagger>
    @ApiModelProperty(value = "${field.comment}")
        <#else>
    /**
     * ${field.comment}
     */
        </#if>
    </#if>
    <#if field.required == true && field.entityType?? && field.entityType == 'String'>
    @NotBlank(message="${field.comment}不能为空")
    </#if>
    <#if field.required == true && field.entityType?? && field.entityType != 'String'>
    @NotNull(message="${field.comment}不能为空")
    </#if>
    <#if field.min?? && field.entityType?? && field.entityType != 'String'>
    @Min(${field.min}L)
    </#if>
    <#if field.max?? && field.entityType?? && field.entityType != 'String'>
    @Max(${field.max}L)
    </#if>
    <#if field.minLength?? && field.maxLength?? && field.entityType?? && field.entityType == 'String'>
    @Length(min=${field.minLength},max=${field.maxLength})
    </#if>
    <#if field.validateValue??>
    @Pattern(regexp="${field.validateValue}",message="${field.comment}格式错误")
    </#if>
    <#if field.validateRegular??>
    @Pattern(regexp="${field.validateRegular}",message="${field.comment}格式错误")
    </#if>
    <#if field.controlType == "DTAE_TIME_PICKER">
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    <#elseif field.controlType == "DTAE_PICKER">
    @JsonFormat(pattern = "yyyy-MM-dd")
    <#elseif field.controlType == "TIME_PICKER">
    @JsonFormat(pattern = "HH:mm:ss")
    </#if>
    private ${field.entityType} ${field.entityName};
    </#if>
</#list>
<#------------  END 字段循环遍历  ---------->
}
