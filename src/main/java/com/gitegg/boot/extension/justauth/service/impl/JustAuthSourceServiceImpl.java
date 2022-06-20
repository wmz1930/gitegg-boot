package com.gitegg.boot.extension.justauth.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitegg.platform.base.constant.AuthConstant;
import com.gitegg.platform.base.constant.GitEggConstant;
import com.gitegg.platform.base.util.BeanCopierUtils;
import com.gitegg.platform.base.util.JsonUtils;
import com.gitegg.boot.extension.justauth.dto.*;
import com.gitegg.boot.extension.justauth.entity.JustAuthSource;
import com.gitegg.boot.extension.justauth.mapper.JustAuthSourceMapper;
import com.gitegg.boot.extension.justauth.service.IJustAuthSourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;


/**
 * <p>
 * 租户第三方登录信息配置表 服务实现类
 * </p>
 *
 * @author GitEgg
 * @since 2022-05-19
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class JustAuthSourceServiceImpl extends ServiceImpl<JustAuthSourceMapper, JustAuthSource> implements IJustAuthSourceService {

    private final JustAuthSourceMapper justAuthSourceMapper;
    
    private final RedisTemplate redisTemplate;
    
    /**
     * 是否开启租户模式
     */
    @Value("${tenant.enable}")
    private Boolean enable;

    /**
    * 分页查询租户第三方登录信息配置表列表
    * @param page
    * @param queryJustAuthSourceDTO
    * @return
    */
    @Override
    public Page<JustAuthSourceDTO> queryJustAuthSourceList(Page<JustAuthSourceDTO> page, QueryJustAuthSourceDTO queryJustAuthSourceDTO) {
        Page<JustAuthSourceDTO> justAuthSourceInfoList = justAuthSourceMapper.queryJustAuthSourceList(page, queryJustAuthSourceDTO);
        return justAuthSourceInfoList;
    }

    /**
    * 查询租户第三方登录信息配置表列表
    * @param queryJustAuthSourceDTO
    * @return
    */
    @Override
    public List<JustAuthSourceDTO> queryJustAuthSourceList(QueryJustAuthSourceDTO queryJustAuthSourceDTO) {
        List<JustAuthSourceDTO> justAuthSourceInfoList = justAuthSourceMapper.queryJustAuthSourceList(queryJustAuthSourceDTO);
        return justAuthSourceInfoList;
    }

    /**
    * 查询租户第三方登录信息配置表详情
    * @param queryJustAuthSourceDTO
    * @return
    */
    @Override
    public JustAuthSourceDTO queryJustAuthSource(QueryJustAuthSourceDTO queryJustAuthSourceDTO) {
        JustAuthSourceDTO justAuthSourceDTO = justAuthSourceMapper.queryJustAuthSource(queryJustAuthSourceDTO);
        return justAuthSourceDTO;
    }

    /**
    * 创建租户第三方登录信息配置表
    * @param justAuthSource
    * @return
    */
    @Override
    public boolean createJustAuthSource(CreateJustAuthSourceDTO justAuthSource) {
        JustAuthSource justAuthSourceEntity = BeanCopierUtils.copyByClass(justAuthSource, JustAuthSource.class);
        boolean result = this.save(justAuthSourceEntity);
        if (result)
        {
            // 新增到缓存
            JustAuthSource justAuthSourceCreate = this.getById(justAuthSourceEntity.getId());
            JustAuthSourceDTO justAuthSourceDTO = BeanCopierUtils.copyByClass(justAuthSourceCreate, JustAuthSourceDTO.class);
            this.addOrUpdateJustAuthSourceCache(justAuthSourceDTO);
        }
        return result;
    }

    /**
    * 更新租户第三方登录信息配置表
    * @param justAuthSource
    * @return
    */
    @Override
    public boolean updateJustAuthSource(UpdateJustAuthSourceDTO justAuthSource) {
        JustAuthSource justAuthSourceEntity = BeanCopierUtils.copyByClass(justAuthSource, JustAuthSource.class);
        boolean result = this.updateById(justAuthSourceEntity);
        if (result)
        {
            // 新增到缓存
            JustAuthSource justAuthSourceUpdate = this.getById(justAuthSourceEntity.getId());
            JustAuthSourceDTO justAuthSourceDTO = BeanCopierUtils.copyByClass(justAuthSourceUpdate, JustAuthSourceDTO.class);
            this.addOrUpdateJustAuthSourceCache(justAuthSourceDTO);
        }
        return result;
    }

    /**
    * 删除租户第三方登录信息配置表
    * @param justAuthSourceId
    * @return
    */
    @Override
    public boolean deleteJustAuthSource(Long justAuthSourceId) {
        // 新增到缓存
        JustAuthSource justAuthSourceDelete = this.getById(justAuthSourceId);
        JustAuthSourceDTO justAuthSourceDTO = BeanCopierUtils.copyByClass(justAuthSourceDelete, JustAuthSourceDTO.class);
        this.deleteJustAuthSourceCache(justAuthSourceDTO);
        boolean result = this.removeById(justAuthSourceId);
        return result;
    }

    /**
    * 批量删除租户第三方登录信息配置表
    * @param justAuthSourceIds
    * @return
    */
    @Override
    public boolean batchDeleteJustAuthSource(List<Long> justAuthSourceIds) {
        List<JustAuthSource> justAuthSourceDeleteList = this.listByIds(justAuthSourceIds);
        if (!CollectionUtils.isEmpty(justAuthSourceDeleteList))
        {
            for(JustAuthSource justAuthSourceDelete: justAuthSourceDeleteList)
            {
                JustAuthSourceDTO justAuthSourceDTO = BeanCopierUtils.copyByClass(justAuthSourceDelete, JustAuthSourceDTO.class);
                this.deleteJustAuthSourceCache(justAuthSourceDTO);
            }
        }
        boolean result = this.removeByIds(justAuthSourceIds);
        return result;
    }
    
    /**
     * 初始化配置表列表
     * @return
     */
    @Override
    public void initJustAuthSourceList() {
        QueryJustAuthSourceDTO queryJustAuthSourceDTO = new QueryJustAuthSourceDTO();
        queryJustAuthSourceDTO.setStatus(GitEggConstant.ENABLE);
        List<JustAuthSourceDTO> justAuthSourceInfoList = justAuthSourceMapper.initJustAuthSourceList(queryJustAuthSourceDTO);
        
        // 判断是否开启了租户模式，如果开启了，那么角色权限需要按租户进行分类存储
        if (enable) {
            Map<Long, List<JustAuthSourceDTO>> authSourceListMap =
                    justAuthSourceInfoList.stream().collect(Collectors.groupingBy(JustAuthSourceDTO::getTenantId));
            authSourceListMap.forEach((key, value) -> {
                String redisKey = AuthConstant.SOCIAL_TENANT_SOURCE_KEY + key;
                redisTemplate.delete(redisKey);
                addJustAuthSource(redisKey, value);
            });
            
        } else {
            redisTemplate.delete(AuthConstant.SOCIAL_SOURCE_KEY);
            addJustAuthSource(AuthConstant.SOCIAL_SOURCE_KEY, justAuthSourceInfoList);
        }
    }
    
    private void addJustAuthSource(String key, List<JustAuthSourceDTO> sourceList) {
        Map<String, String> authConfigMap = new TreeMap<>();
        Optional.ofNullable(sourceList).orElse(new ArrayList<>()).forEach(source -> {
            try {
                authConfigMap.put(source.getSourceName(), JsonUtils.objToJson(source));
                redisTemplate.opsForHash().putAll(key, authConfigMap);
            } catch (Exception e) {
                log.error("初始化第三方登录失败：{}" , e);
            }
        });
    }
    
    private void addOrUpdateJustAuthSourceCache(JustAuthSourceDTO justAuthSource) {
        try {
            String redisKey = AuthConstant.SOCIAL_SOURCE_KEY;
            if (enable) {
                redisKey = AuthConstant.SOCIAL_TENANT_SOURCE_KEY + justAuthSource.getTenantId();
            }
            redisTemplate.opsForHash().put(redisKey, justAuthSource.getSourceName(), JsonUtils.objToJson(justAuthSource));
        } catch (Exception e) {
            log.error("修改第三方登录缓存失败：{}" , e);
        }
    }
    
    private void deleteJustAuthSourceCache(JustAuthSourceDTO justAuthSource) {
        try {
            String redisKey = AuthConstant.SOCIAL_SOURCE_KEY;
            if (enable) {
                redisKey = AuthConstant.SOCIAL_TENANT_SOURCE_KEY + justAuthSource.getTenantId();
            }
            redisTemplate.opsForHash().delete(redisKey, justAuthSource.getSourceName(), JsonUtils.objToJson(justAuthSource));
        } catch (Exception e) {
            log.error("删除第三方登录缓存失败：{}" , e);
        }
    }
}
