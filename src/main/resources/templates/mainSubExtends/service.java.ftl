package ${package.Service};

import java.util.List;

import ${package.Entity}.${entity};
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ${superServiceClassPackage};

<#assign dtoPackage="${package.Entity}"/>
import ${dtoPackage?replace("entity","dto")}.${entity}DTO;
import ${dtoPackage?replace("entity","dto")}.Create${entity}DTO;
import ${dtoPackage?replace("entity","dto")}.Update${entity}DTO;
import ${dtoPackage?replace("entity","dto")}.Query${entity}DTO;

/**
 * <p>
 * ${table.comment!} 服务类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if kotlin>
interface ${table.serviceName} : ${superServiceClass}<${entity}>
<#else>
public interface ${table.serviceName} extends ${superServiceClass}<${entity}> {

    /**
    * 分页查询${table.comment!}列表
    * @param page
    * @param query${entity}DTO
    * @return
    */
    Page<${entity}DTO> query${entity}List(Page<${entity}DTO> page, Query${entity}DTO query${entity}DTO);

    /**
    * 查询${table.comment!}列表
    * @param query${entity}DTO
    * @return
    */
    List<${entity}DTO> query${entity}List(Query${entity}DTO query${entity}DTO);

    /**
    * 查询${table.comment!}详情
    * @param query${entity}DTO
    * @return
    */
    ${entity}DTO query${entity}(Query${entity}DTO query${entity}DTO);

    /**
    * 创建${table.comment!}
    * @param ${table.entityPath}
    * @return
    */
    boolean create${entity}(Create${entity}DTO ${table.entityPath});

    /**
    * 批量创建${table.comment!}
    * @param ${table.entityPath}s
    * @return
    */
    public void create${entity}Batch(List<Create${entity}DTO> ${table.entityPath}s);

    /**
    * 更新${table.comment!}
    * @param ${table.entityPath}
    * @return
    */
    boolean update${entity}(Update${entity}DTO ${table.entityPath});

    /**
    * 删除${table.comment!}
    * @param ${table.entityPath}Id
    * @return
    */
    boolean delete${entity}(Long ${table.entityPath}Id);

    /**
    * 批量删除${table.comment!}
    * @param ${table.entityPath}Ids
    * @return
    */
    boolean batchDelete${entity}(List<Long> ${table.entityPath}Ids);

    /**
    * 联合主表分页查询${table.comment!}列表
    * @param page
    * @param query${entity}DTO
    * @return
    */
    Page<${entity}DTO> query${mainEntityName}${entity}List(Page<${entity}DTO> page, Query${entity}DTO query${entity}DTO);

    /**
    * 联合主表查询${table.comment!}列表
    * @param query${entity}DTO
    * @return
    */
    List<${entity}DTO> query${mainEntityName}${entity}List(Query${entity}DTO query${entity}DTO);

    /**
    * 联合主表查询${table.comment!}详情
    * @param query${entity}DTO
    * @return
    */
    ${entity}DTO query${mainEntityName}${entity}(Query${entity}DTO query${entity}DTO);
}
</#if>
