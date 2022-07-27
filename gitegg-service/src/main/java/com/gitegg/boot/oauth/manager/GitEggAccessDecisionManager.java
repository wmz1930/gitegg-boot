package com.gitegg.boot.oauth.manager;

import com.gitegg.platform.base.constant.AuthConstant;
import com.gitegg.platform.base.constant.TokenConstant;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author GitEgg
 * @date 2021/11/15
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GitEggAccessDecisionManager implements AccessDecisionManager {

    private final RedisTemplate redisTemplate;

    /**
     * 是否开启租户模式
     */
    @Value(("${tenant.enable}"))
    private Boolean enable;

    /**
     * @param authentication   用户凭证
     * @param resource         资源 URL
     * @param configAttributes 资源 URL 所须要的权限
     * @throws AccessDeniedException               资源拒绝访问
     * @throws InsufficientAuthenticationException 用户凭证不符
     */
    @Override
    public void decide(Authentication authentication, Object resource, Collection<ConfigAttribute> configAttributes)
            throws AccessDeniedException, InsufficientAuthenticationException {

        // 如果开启了租户模式，但是请求头里没有租户信息，那么拒绝访问
        String tenantId = ((FilterInvocation) resource).getRequest().getHeader(AuthConstant.TENANT_ID);
        if (enable && StringUtils.isEmpty(tenantId)) {
            throw new AccessDeniedException("已开启租户模式，请求参数未查询到租户信息");
        }

        // token为空拒绝访问
        String token = ((FilterInvocation) resource).getRequest().getHeader(AuthConstant.JWT_TOKEN_HEADER);
        if (!StringUtils.isEmpty(token)) {
            //如果token被加入到黑名单，就是执行了退出登录操作，那么拒绝访问
            String realToken = token.replace(AuthConstant.JWT_TOKEN_PREFIX, "");
            try {
                JWSObject jwsObject = JWSObject.parse(realToken);
                Payload payload = jwsObject.getPayload();
                JSONObject jsonObject = payload.toJSONObject();
                String jti = jsonObject.getAsString(TokenConstant.JTI);
                String blackListToken = (String)redisTemplate.opsForValue().get(AuthConstant.TOKEN_BLACKLIST + jti);
                if (!StringUtils.isEmpty(blackListToken)) {
                    throw new AccessDeniedException("已退出登录");
                }
            } catch (ParseException e) {
                log.error("获取token黑名单时发生错误：{}", e);
            }
        }


        log.info("[决策管理器]:开始判断请求 {} 须要的权限", ((FilterInvocation) resource).getRequestUrl());
        if (configAttributes == null || configAttributes.isEmpty()) {
            log.info("[决策管理器]:请求 {} 无需权限", ((FilterInvocation) resource).getRequestUrl());
            return;
        }

        authentication.isAuthenticated();

        log.info("[决策管理器]:请求 {} 须要的权限 - {}", ((FilterInvocation) resource).getRequestUrl(), configAttributes);
        // 判断用户所拥有的权限，是否符合对应的Url权限，用户权限是实现 UserDetailsService#loadUserByUsername 返回用户所对应的权限
        Iterator<ConfigAttribute> ite = configAttributes.iterator();
        log.info("[决策管理器]:用户 {} 拥有的权限 - {}", authentication.getName(), authentication.getAuthorities());
        while (ite.hasNext()) {
            ConfigAttribute neededAuthority = ite.next();
            String neededAuthorityStr = neededAuthority.getAttribute();
            for (GrantedAuthority existingAuthority : authentication.getAuthorities()) {
                if (neededAuthorityStr.equals(existingAuthority.getAuthority())) {
                    return;
                }
            }
        }
        log.info("[决策管理器]:用户 {} 没有访问资源 {} 的权限!", authentication.getName(), ((FilterInvocation) resource).getRequestUrl());
        throw new AccessDeniedException("权限不足!");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    /**
     * 是否支持 FilterInvocationSecurityMetadataSource 须要将这里的false改成true
     *
     * @param clazz
     * @return
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
