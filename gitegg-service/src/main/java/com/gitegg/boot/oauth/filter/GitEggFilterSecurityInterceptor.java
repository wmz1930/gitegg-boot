package com.gitegg.boot.oauth.filter;

import com.gitegg.boot.oauth.manager.GitEggAccessDecisionManager;
import com.gitegg.boot.oauth.manager.GitEggFilterInvocationSecurityMetadataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.FilterInvocation;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author GitEgg
 * @date 2021/11/15
 */
@Slf4j
public class GitEggFilterSecurityInterceptor extends AbstractSecurityInterceptor implements Filter {

    private GitEggFilterInvocationSecurityMetadataSource securityMetadataSource;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        if (log.isInfoEnabled()) {
            log.info("GitEggFilterSecurityInterceptor init");
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (log.isInfoEnabled()) {
            log.info("GitEggFilterSecurityInterceptor doFilter");
        }
        FilterInvocation filterInvocation = new FilterInvocation(request, response, chain);
        invoke(filterInvocation);
    }

    public void invoke(FilterInvocation filterInvocation) throws IOException, ServletException {
        // filterInvocation里面有一个被拦截的url
        // 里面调用 GitEggAccessDecisionManager 的 getAttributes(Object object) 这个方法获取 filterInvocation 对应的全部权限
        // 再调用 GitEggAccessDecisionManager 的 decide方法来校验用户的权限是否足够
        InterceptorStatusToken interceptorStatusToken = super.beforeInvocation(filterInvocation);
        try {
            // 执行下一个拦截器
            filterInvocation.getChain().doFilter(filterInvocation.getRequest(), filterInvocation.getResponse());
        } finally {
            super.afterInvocation(interceptorStatusToken, null);
        }
    }

    @Override
    public void destroy() {

    }

    @Override
    public Class<?> getSecureObjectClass() {
        return FilterInvocation.class;
    }

    /**
     * 资源源数据定义，设置为自定义的 SecureResourceFilterInvocationDefinitionSource
     *
     * @return
     */
    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource() {
        return securityMetadataSource;
    }

    public void setGitEggAccessDecisionManager(GitEggAccessDecisionManager accessDecisionManager) {
        super.setAccessDecisionManager(accessDecisionManager);
    }

    @Override
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    public void setSecurityMetadataSource(GitEggFilterInvocationSecurityMetadataSource securityMetadataSource) {
        this.securityMetadataSource = securityMetadataSource;
    }

}
