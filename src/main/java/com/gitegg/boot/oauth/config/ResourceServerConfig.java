package com.gitegg.boot.oauth.config;

import cn.hutool.core.util.ArrayUtil;
import com.gitegg.boot.oauth.filter.AuthGlobalFilter;
import com.gitegg.boot.oauth.filter.GitEggFilterSecurityInterceptor;
import com.gitegg.boot.oauth.handler.LoginFailHandler;
import com.gitegg.boot.oauth.handler.LoginSuccessHandler;
import com.gitegg.boot.oauth.manager.GitEggAccessDecisionManager;
import com.gitegg.boot.oauth.manager.GitEggFilterInvocationSecurityMetadataSource;
import com.gitegg.platform.oauth2.props.AuthUrlWhiteListProperties;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

/**
 * 资源服务器
 * @author GitEgg
 */
@Configuration
@AllArgsConstructor
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private final AuthUrlWhiteListProperties authUrlWhiteListProperties;

    private final LoginSuccessHandler loginSuccessHandler;

    private final LoginFailHandler loginFailHandler;

    private final AuthGlobalFilter authGlobalFilter;

    private final AuthenticationManager manager;

    private final GitEggAccessDecisionManager accessDecisionManager;

    private final GitEggFilterInvocationSecurityMetadataSource securityMetadataSource;

    /**
     *
     * @param http
     * access(String) 如果给定的SpEL表达式计算结果为true，就允许访问
     * anonymous() 允许匿名用户访问
     * authenticated() 允许认证的用户进行访问
     * denyAll() 无条件拒绝所有访问
     * fullyAuthenticated() 如果用户是完整认证的话（不是通过Remember-me功能认证的），就允许访问
     * hasAuthority(String) 如果用户具备给定权限的话就允许访问
     * hasAnyAuthority(String…)如果用户具备给定权限中的某一个的话，就允许访问
     * hasRole(String) 如果用户具备给定角色(用户组)的话,就允许访问/
     * hasAnyRole(String…) 如果用户具有给定角色(用户组)中的一个的话,允许访问.
     * hasIpAddress(String 如果请求来自给定ip地址的话,就允许访问.
     * not() 对其他访问结果求反.
     * permitAll() 无条件允许访问
     * rememberMe() 如果用户是通过Remember-me功能认证的，就允许访问
     *
     */
    @Override
    @SneakyThrows
    public void configure(HttpSecurity http) {

		http.headers().frameOptions().disable();
		http.formLogin()
			.successHandler(loginSuccessHandler)
			.failureHandler(loginFailHandler)
			.and().addFilterBefore(authGlobalFilter, UsernamePasswordAuthenticationFilter.class)
			.addFilterAfter(createRBACAuthenticationFilter(), FilterSecurityInterceptor.class)
		    .cors()
			.and()
			.authorizeRequests()
			.requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
			.antMatchers(HttpMethod.OPTIONS).permitAll()
			.antMatchers(ArrayUtil.toArray(authUrlWhiteListProperties.getStaticFiles(), String.class)).permitAll()
			.antMatchers(ArrayUtil.toArray(authUrlWhiteListProperties.getWhiteUrls(), String.class)).permitAll()
			.anyRequest().authenticated()
			.and().csrf().disable();
	}

    /**
     * RBAC权限控制
     * 过滤器优先度在 FilterSecurityInterceptor 以后
     * spring-security 的默认过滤器列表见
     * https://docs.spring.io/spring-security/site/docs/5.0.0.M1/reference/htmlsingle/#ns-custom-filters
     *
     * @return
     */
    private GitEggFilterSecurityInterceptor createRBACAuthenticationFilter() {
        GitEggFilterSecurityInterceptor interceptor = new GitEggFilterSecurityInterceptor();
        interceptor.setAuthenticationManager(manager);
        interceptor.setGitEggAccessDecisionManager(accessDecisionManager);
        interceptor.setSecurityMetadataSource(securityMetadataSource);
        return interceptor;
    }

}
