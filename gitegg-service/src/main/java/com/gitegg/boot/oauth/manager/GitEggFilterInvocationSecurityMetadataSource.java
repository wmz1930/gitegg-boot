package com.gitegg.boot.oauth.manager;

import cn.hutool.core.convert.Convert;
import com.gitegg.platform.base.constant.AuthConstant;
import com.gitegg.platform.oauth2.props.AuthUrlWhiteListProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.PathMatcher;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author GitEgg
 * @date 2021/11/14
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GitEggFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource, InitializingBean {

    private final RedisTemplate redisTemplate;

    private final AuthUrlWhiteListProperties authUrlWhiteListProperties;

    /**
     * 是否开启租户模式
     */
    @Value("${tenant.enable}")
    private Boolean enable;


    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {

        PathMatcher pathMatcher = new AntPathMatcher();
        // 白名单url直接放行
        List<String> whiteUrls = authUrlWhiteListProperties.getWhiteUrls();
        List<String> staticFiles = authUrlWhiteListProperties.getStaticFiles();
        List<String> permitAllUrls = Stream.of(whiteUrls, staticFiles).flatMap(Collection::stream).collect(Collectors.toList());
        String urls = permitAllUrls.stream().filter(url -> pathMatcher.match(url, ((FilterInvocation) object).getRequest().getRequestURI())).findAny().orElse(null);

        // 白名单直接放行
        if (null != urls) {
            return Collections.EMPTY_LIST;
        }

        if (object instanceof FilterInvocation) {
            FilterInvocation filterInvocation = (FilterInvocation) object;
            String requestURI = filterInvocation.getRequest().getRequestURI();

            // 获取租户id
            String tenantId = filterInvocation.getRequest().getHeader(AuthConstant.TENANT_ID);

            String redisRoleKey = AuthConstant.TENANT_RESOURCE_ROLES_KEY;
            // 判断是否开启了租户模式，如果开启了，那么按租户分类的方式获取角色权限
            if (enable) {
                redisRoleKey += tenantId;
            } else {
                redisRoleKey = AuthConstant.RESOURCE_ROLES_KEY;
            }

            // 缓存取资源权限角色关系列表
            Map<Object, Object> resourceRolesMap = redisTemplate.opsForHash().entries(redisRoleKey);
            Iterator<Object> iterator = resourceRolesMap.keySet().iterator();

            // 请求路径匹配到的资源需要的角色权限集合authorities统计
            List<String> authorities = new ArrayList<>();
            while (iterator.hasNext()) {
                String pattern = (String) iterator.next();
                if (pathMatcher.match(pattern, requestURI)) {
                    authorities.addAll(Convert.toList(String.class, resourceRolesMap.get(pattern)));
                }
            }
            // 返回请求所需的权限
            return SecurityConfig.createList(authorities.toArray(new String[authorities.size()]));
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
