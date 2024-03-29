#菜单
INSERT INTO `t_sys_resource` (
      `parent_id`,
      `tenant_id`,
      `resource_name`,
      `resource_key`,
      `resource_type`,
      `resource_icon`,
      `resource_path`,
      `resource_url`,
      `resource_level`,
      `resource_show`,
      `resource_cache`,
      `resource_page_name`,
      `resource_status`,
      `comments`,
      `create_time`,
      `creator`,
      `update_time`,
      `operator`,
      `del_flag`
  )
VALUES
  (
      0,
      0,
      '${config.moduleName!}',
      '<#if config.serviceName?? && config.serviceName != "">${config.serviceName?replace("-",":")}:</#if><#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath?replace("/","","f")?replace("/",":")}<#else>/${table.entityPath}</#if>:table',
      '2',
      'jiaoseguanli',
      '<#if config.moduleCode?? && config.moduleCode != "">${config.moduleCode}/</#if>table',
      '<#if config.serviceName?? && config.serviceName != "">${config.serviceName?replace("-","/")}/</#if><#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath?replace("/","","f")}<#else>/${table.entityPath}</#if>/${entity}Table',
      2,
      <#if vueTablePath?? && vueTablePath != "">1<#else>0</#if>,
      1,
      '${table.entityPath}Table',
      '1',
      '${config.moduleName!}',
      NOW(),
      1,
      NOW(),
      1,
      '0'
  );

<#if tableShowType?? && tableShowType == "tree_table">
#查询树列表
INSERT INTO `t_sys_resource` (
      `parent_id`,
      `tenant_id`,
      `resource_name`,
      `resource_key`,
      `resource_type`,
      `resource_icon`,
      `resource_path`,
      `resource_url`,
      `resource_level`,
      `resource_show`,
      `resource_cache`,
      `resource_page_name`,
      `resource_status`,
      `comments`,
      `create_time`,
      `creator`,
      `update_time`,
      `operator`,
      `del_flag`
  )
VALUES
  (
      ${maxId},
      0,
      '获取${config.moduleName!}树',
      '<#if config.serviceName?? && config.serviceName != "">${config.serviceName?replace("-",":")}:</#if>:<#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath?replace("/","","f")?replace("/",":")}<#else>/${table.entityPath}</#if>:tree',
      '4',
      'xitongrizhi',
      'tree',
      '<#if config.serviceName?? && config.serviceName != "">/${config.serviceName}</#if><#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath}<#else>/${table.entityPath}</#if>/tree',
      2,
      1,
      1,
      NULL,
      '1',
      '获取${config.moduleName!}树数据',
      NOW(),
      1,
      NOW(),
      1,
      '0'
  );
</#if>
#查询数据列表
INSERT INTO `t_sys_resource` (
      `parent_id`,
      `tenant_id`,
      `resource_name`,
      `resource_key`,
      `resource_type`,
      `resource_icon`,
      `resource_path`,
      `resource_url`,
      `resource_level`,
      `resource_show`,
      `resource_cache`,
      `resource_page_name`,
      `resource_status`,
      `comments`,
      `create_time`,
      `creator`,
      `update_time`,
      `operator`,
      `del_flag`
  )
VALUES
  (
      ${maxId},
      0,
      '获取${config.moduleName!}列表',
      '<#if config.serviceName?? && config.serviceName != "">${config.serviceName?replace("-",":")}:</#if>:<#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath?replace("/","","f")?replace("/",":")}<#else>/${table.entityPath}</#if>:list',
      '4',
      'xitongrizhi',
      'list',
      '<#if config.serviceName?? && config.serviceName != "">/${config.serviceName}</#if><#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath}<#else>/${table.entityPath}</#if>/list',
      2,
      1,
      1,
      NULL,
      '1',
      '获取${config.moduleName!}列表数据',
      NOW(),
      1,
      NOW(),
      1,
      '0'
  );

#添加
INSERT INTO `t_sys_resource` (
      `parent_id`,
      `tenant_id`,
      `resource_name`,
      `resource_key`,
      `resource_type`,
      `resource_icon`,
      `resource_path`,
      `resource_url`,
      `resource_level`,
      `resource_show`,
      `resource_cache`,
      `resource_page_name`,
      `resource_status`,
      `comments`,
      `create_time`,
      `creator`,
      `update_time`,
      `operator`,
      `del_flag`
  )
VALUES
  (
      ${maxId},
      0,
      '添加
',
      '<#if config.serviceName?? && config.serviceName != "">${config.serviceName?replace("-",":")}:</#if>:<#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath?replace("/","","f")?replace("/",":")}<#else>/${table.entityPath}</#if>:create',
      '4',
      'xitongrizhi',
      'create',
      '<#if config.serviceName?? && config.serviceName != "">/${config.serviceName}</#if><#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath}<#else>/${table.entityPath}</#if>/create',
      2,
      1,
      1,
      NULL,
      '1',
      '添加${config.moduleName!}',
      NOW(),
      1,
      NOW(),
      1,
      '0'
  );

#更新
INSERT INTO `t_sys_resource` (
      `parent_id`,
      `tenant_id`,
      `resource_name`,
      `resource_key`,
      `resource_type`,
      `resource_icon`,
      `resource_path`,
      `resource_url`,
      `resource_level`,
      `resource_show`,
      `resource_cache`,
      `resource_page_name`,
      `resource_status`,
      `comments`,
      `create_time`,
      `creator`,
      `update_time`,
      `operator`,
      `del_flag`
  )
VALUES
  (
      ${maxId},
      0,
      '更新${config.moduleName!}',
      '<#if config.serviceName?? && config.serviceName != "">${config.serviceName?replace("-",":")}:</#if>:<#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath?replace("/","","f")?replace("/",":")}<#else>/${table.entityPath}</#if>:update',
      '4',
      'xitongrizhi',
      'update',
      '<#if config.serviceName?? && config.serviceName != "">/${config.serviceName}</#if><#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath}<#else>/${table.entityPath}</#if>/update',
      2,
      1,
      1,
      NULL,
      '1',
      '更新${config.moduleName!}',
      NOW(),
      1,
      NOW(),
      1,
      '0'
  );

#删除
INSERT INTO `t_sys_resource` (
      `parent_id`,
      `tenant_id`,
      `resource_name`,
      `resource_key`,
      `resource_type`,
      `resource_icon`,
      `resource_path`,
      `resource_url`,
      `resource_level`,
      `resource_show`,
      `resource_cache`,
      `resource_page_name`,
      `resource_status`,
      `comments`,
      `create_time`,
      `creator`,
      `update_time`,
      `operator`,
      `del_flag`
  )
VALUES
  (
      ${maxId},
      0,
      '删除${config.moduleName!}',
      '<#if config.serviceName?? && config.serviceName != "">${config.serviceName?replace("-",":")}:</#if>:<#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath?replace("/","","f")?replace("/",":")}<#else>/${table.entityPath}</#if>:delete',
      '4',
      'xitongrizhi',
      'delete',
      '<#if config.serviceName?? && config.serviceName != "">/${config.serviceName}</#if><#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath}<#else>/${table.entityPath}</#if>/delete/{${table.entityPath}Id}',
      2,
      1,
      1,
      NULL,
      '1',
      '删除${config.moduleName!}',
      NOW(),
      1,
      NOW(),
      1,
      '0'
  );

#批量删除
INSERT INTO `t_sys_resource` (
      `parent_id`,
      `tenant_id`,
      `resource_name`,
      `resource_key`,
      `resource_type`,
      `resource_icon`,
      `resource_path`,
      `resource_url`,
      `resource_level`,
      `resource_show`,
      `resource_cache`,
      `resource_page_name`,
      `resource_status`,
      `comments`,
      `create_time`,
      `creator`,
      `update_time`,
      `operator`,
      `del_flag`
  )
  VALUES
  (
      ${maxId},
      0,
      '批量删除${config.moduleName!}',
      '<#if config.serviceName?? && config.serviceName != "">${config.serviceName?replace("-",":")}:</#if>:<#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath?replace("/","","f")?replace("/",":")}<#else>/${table.entityPath}</#if>:batch:delete',
      '4',
      'xitongrizhi',
      'batch/delete',
      '<#if config.serviceName?? && config.serviceName != "">/${config.serviceName}</#if><#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath}<#else>/${table.entityPath}</#if>/batch/delete',
      2,
      1,
      1,
      NULL,
      '1',
      '批量删除${config.moduleName!}',
      NOW(),
      1,
      NOW(),
      1,
      '0'
  );

<#list table.fields as field>
<#if field.annotationColumnName?contains("status")>
#更新状态
INSERT INTO `t_sys_resource` (
   `parent_id`,
   `tenant_id`,
   `resource_name`,
   `resource_key`,
   `resource_type`,
   `resource_icon`,
   `resource_path`,
   `resource_url`,
   `resource_level`,
   `resource_show`,
   `resource_cache`,
   `resource_page_name`,
   `resource_status`,
   `comments`,
   `create_time`,
   `creator`,
   `update_time`,
   `operator`,
   `del_flag`
)
VALUES
(
   ${maxId},
   0,
   '${config.moduleName!}状态修改',
   '<#if config.serviceName?? && config.serviceName != "">${config.serviceName?replace("-",":")}:</#if>:<#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath?replace("/","","f")?replace("/",":")}<#else>/${table.entityPath}</#if>:status',
   '4',
   'xitongrizhi',
   'status',
   '<#if config.serviceName?? && config.serviceName != "">/${config.serviceName}</#if><#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath}<#else>/${table.entityPath}</#if>/status/{${table.entityPath}Id}/{${table.entityPath}Status}',
   2,
   1,
   1,
   NULL,
   '1',
   '批量删除${config.moduleName!}',
   NOW(),
   1,
   NOW(),
   1,
   '0'
);
</#if>
</#list>

#校验是否存在
INSERT INTO `t_sys_resource` (
   `parent_id`,
   `tenant_id`,
   `resource_name`,
   `resource_key`,
   `resource_type`,
   `resource_icon`,
   `resource_path`,
   `resource_url`,
   `resource_level`,
   `resource_show`,
   `resource_cache`,
   `resource_page_name`,
   `resource_status`,
   `comments`,
   `create_time`,
   `creator`,
   `update_time`,
   `operator`,
   `del_flag`
)
VALUES
(
   ${maxId},
   0,
   '${config.moduleName!}字段校验是否存在',
   '<#if config.serviceName?? && config.serviceName != "">${config.serviceName?replace("-",":")}:</#if>:<#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath?replace("/","","f")?replace("/",":")}<#else>/${table.entityPath}</#if>:check',
   '4',
   'xitongrizhi',
   'check',
   '<#if config.serviceName?? && config.serviceName != "">/${config.serviceName}</#if><#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath}<#else>/${table.entityPath}</#if>/check',
   2,
   1,
   1,
   NULL,
   '1',
   '字段校验是否存在${config.moduleName!}',
   NOW(),
   1,
   NOW(),
   1,
   '0'
);
<#if config.exportFlag == true>
#数据导出
INSERT INTO `t_sys_resource` (
   `parent_id`,
   `tenant_id`,
   `resource_name`,
   `resource_key`,
   `resource_type`,
   `resource_icon`,
   `resource_path`,
   `resource_url`,
   `resource_level`,
   `resource_show`,
   `resource_cache`,
   `resource_page_name`,
   `resource_status`,
   `comments`,
   `create_time`,
   `creator`,
   `update_time`,
   `operator`,
   `del_flag`
)
VALUES
(
   ${maxId},
   0,
   '${config.moduleName!}数据导出',
   '<#if config.serviceName?? && config.serviceName != "">${config.serviceName?replace("-",":")}:</#if>:<#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath?replace("/","","f")?replace("/",":")}<#else>/${table.entityPath}</#if>:export',
   '4',
   'xitongrizhi',
   'export',
   '<#if config.serviceName?? && config.serviceName != "">/${config.serviceName}</#if><#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath}<#else>/${table.entityPath}</#if>/export',
   2,
   1,
   1,
   NULL,
   '1',
   '数据导出${config.moduleName!}',
   NOW(),
   1,
   NOW(),
   1,
   '0'
);
</#if>

<#if config.importFlag == true>
#下载数据导入模板
INSERT INTO `t_sys_resource` (
   `parent_id`,
   `tenant_id`,
   `resource_name`,
   `resource_key`,
   `resource_type`,
   `resource_icon`,
   `resource_path`,
   `resource_url`,
   `resource_level`,
   `resource_show`,
   `resource_cache`,
   `resource_page_name`,
   `resource_status`,
   `comments`,
   `create_time`,
   `creator`,
   `update_time`,
   `operator`,
   `del_flag`
)
VALUES
(
   ${maxId},
   0,
   '${config.moduleName!}数据导入模板下载',
   '<#if config.serviceName?? && config.serviceName != "">${config.serviceName?replace("-",":")}:</#if>:<#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath?replace("/","","f")?replace("/",":")}<#else>/${table.entityPath}</#if>:download:template',
   '4',
   'xitongrizhi',
   'download/template',
   '<#if config.serviceName?? && config.serviceName != "">/${config.serviceName}</#if><#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath}<#else>/${table.entityPath}</#if>/download/template',
   2,
   1,
   1,
   NULL,
   '1',
   '数据导入模板下载${config.moduleName!}',
   NOW(),
   1,
   NOW(),
   1,
   '0'
);

#数据导入
INSERT INTO `t_sys_resource` (
   `parent_id`,
   `tenant_id`,
   `resource_name`,
   `resource_key`,
   `resource_type`,
   `resource_icon`,
   `resource_path`,
   `resource_url`,
   `resource_level`,
   `resource_show`,
   `resource_cache`,
   `resource_page_name`,
   `resource_status`,
   `comments`,
   `create_time`,
   `creator`,
   `update_time`,
   `operator`,
   `del_flag`
)
VALUES
(
   ${maxId},
   0,
   '${config.moduleName!}数据导入',
   '<#if config.serviceName?? && config.serviceName != "">${config.serviceName?replace("-",":")}:</#if>:<#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath?replace("/","","f")?replace("/",":")}<#else>/${table.entityPath}</#if>:import',
   '4',
   'xitongrizhi',
   'import',
   '<#if config.serviceName?? && config.serviceName != "">/${config.serviceName}</#if><#if config.controllerPath?? && config.controllerPath != "">${config.controllerPath}<#else>/${table.entityPath}</#if>/import',
   2,
   1,
   1,
   NULL,
   '1',
   '数据导入${config.moduleName!}',
   NOW(),
   1,
   NOW(),
   1,
   '0'
);
</#if>