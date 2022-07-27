package com.gitegg.boot.oauth.config;

import com.anji.captcha.service.CaptchaService;
import com.gitegg.boot.extension.justauth.service.IJustAuthService;
import com.gitegg.boot.extension.sms.service.ISmsService;
import com.gitegg.boot.oauth.exception.GitEggOAuth2ExceptionTranslator;
import com.gitegg.boot.oauth.granter.GitEggTokenGranter;
import com.gitegg.boot.oauth.service.GitEggClientDetailsServiceImpl;
import com.gitegg.boot.oauth.service.GitEggUserDetails;
import com.gitegg.boot.oauth.token.GitEggTokenServices;
import com.gitegg.boot.system.service.IUserService;
import com.gitegg.platform.base.constant.AuthConstant;
import com.gitegg.platform.base.constant.TokenConstant;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

import javax.sql.DataSource;
import java.security.KeyPair;
import java.util.*;

/**
 * 授权、认证服务配置
 * token过期时间是在OAuth2提供的数据库表里面配置，每种客户端配置不同的过期时间
 * @author GitEgg
 */
@Configuration
@EnableAuthorizationServer
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private final DataSource dataSource;

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final IUserService userService;

    private final ISmsService smsService;

    private final IJustAuthService justAuthService;

    private final RedisTemplate redisTemplate;

    private final CaptchaService captchaService;

    private final GitEggOAuth2ExceptionTranslator gitEggOAuth2ExceptionTranslator;

    @Value("${captcha.type}")
    private String captchaType;

    @Value("${system.secret-key}")
    private String secretKey;

    @Value("${system.secret-key-salt}")
    private String secretKeySalt;

    @Value("${system.keyPair.keyLocation}")
    private String keyLocation;

    @Value("${system.keyPair.keyPassword}")
    private String keyPassword;

    @Value("${system.keyPair.alias}")
    private String alias;

    /**
     * 客户端信息配置
     */
    @Override
    @SneakyThrows
    public void configure(ClientDetailsServiceConfigurer clients) {
        GitEggClientDetailsServiceImpl jdbcClientDetailsService = new GitEggClientDetailsServiceImpl(dataSource);
        // client-secret加密存储
        jdbcClientDetailsService.setPasswordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());
        jdbcClientDetailsService.setFindClientDetailsSql(AuthConstant.FIND_CLIENT_DETAILS_SQL);
        jdbcClientDetailsService.setSelectClientDetailsSql(AuthConstant.SELECT_CLIENT_DETAILS_SQL);
        clients.withClientDetails(jdbcClientDetailsService);
    }

    /**
     * 配置授权（authorization）以及令牌（token）的访问端点和令牌服务(token services)
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {

        // 增强令牌
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> tokenEnhancers = new ArrayList<>();
        tokenEnhancers.add(tokenEnhancer());
        tokenEnhancers.add(jwtAccessTokenConverter());
        tokenEnhancerChain.setTokenEnhancers(tokenEnhancers);

        // 获取自定义tokenGranter
        TokenGranter tokenGranter = GitEggTokenGranter.getTokenGranter(authenticationManager, endpoints, redisTemplate,
                userService, smsService, justAuthService, captchaService, userDetailsService, captchaType, secretKey, secretKeySalt);

        endpoints.authenticationManager(authenticationManager)
                .accessTokenConverter(jwtAccessTokenConverter())
                .tokenEnhancer(tokenEnhancerChain)
                .userDetailsService(userDetailsService)
                .tokenGranter(tokenGranter)
                .tokenServices(createDefaultTokenServices(endpoints))
                /**
                 *
                 * refresh_token有两种使用方式：重复使用(true)、非重复使用(false)，默认为true
                 * 1.重复使用：access_token过期刷新时， refresh token过期时间未改变，仍以初次生成的时间为准
                 * 2.非重复使用：access_token过期刷新时， refresh_token过期时间延续，在refresh_token有效期内刷新而无需失效再次登录
                 */
                .reuseRefreshTokens(false)
                // 可以修改Oauth2默认认证接口的地址
//                .pathMapping("/oauth/token", "/api/oauth/token")
                //自定义异常返回消息
                .exceptionTranslator(gitEggOAuth2ExceptionTranslator);
    }

    /**
     * 允许表单认证
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.allowFormAuthenticationForClients()
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    /**
     * 使用非对称加密算法对token签名
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(keyPair());
        return converter;
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }


    /**
     * 从classpath下的密钥库中获取密钥对(公钥+私钥)
     */
    @Bean
    public KeyPair keyPair() {
        KeyStoreKeyFactory factory = new KeyStoreKeyFactory(
                new ClassPathResource(keyLocation), keyPassword.toCharArray());
        KeyPair keyPair = factory.getKeyPair(
                alias, keyPassword.toCharArray());
        return keyPair;
    }

    /**
     * JWT内容增强
     */
    @Bean
    public TokenEnhancer tokenEnhancer() {
        return (accessToken, authentication) -> {
            Map<String, Object> map = new HashMap<>(2);
            GitEggUserDetails user = (GitEggUserDetails) authentication.getUserAuthentication().getPrincipal();
            map.put(TokenConstant.TENANT_ID, user.getTenantId());
            map.put(TokenConstant.OAUTH_ID, user.getOauthId());
            map.put(TokenConstant.USER_ID, user.getId());
            map.put(TokenConstant.ORGANIZATION_ID, user.getOrganizationId());
            map.put(TokenConstant.ORGANIZATION_NAME, user.getOrganizationName());
            map.put(TokenConstant.ORGANIZATION_IDS, user.getOrganizationIds());
            map.put(TokenConstant.ORGANIZATION_NAMES, user.getOrganizationNames());
            map.put(TokenConstant.ROLE_ID, user.getRoleId());
            map.put(TokenConstant.ROLE_NAME, user.getRoleName());
            map.put(TokenConstant.ROLE_IDS, user.getRoleIds());
            map.put(TokenConstant.ROLE_NAMES, user.getRoleNames());
            map.put(TokenConstant.ACCOUNT, user.getAccount());
            map.put(TokenConstant.REAL_NAME, user.getRealName());
            map.put(TokenConstant.NICK_NAME, user.getNickname());
            map.put(TokenConstant.ROLE_ID_LIST, user.getRoleIdList());
            map.put(TokenConstant.ROLE_KEY_LIST, user.getRoleKeyList());
            map.put(TokenConstant.ORGANIZATION_ID_LIST, user.getOrganizationIdList());
            map.put(TokenConstant.AVATAR, user.getAvatar());
            map.put(TokenConstant.DATA_PERMISSION_TYPE_LIST, user.getDataPermissionTypeList());
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(map);
            return accessToken;
        };
    }

    @Primary
    @Bean
    public DefaultTokenServices createDefaultTokenServices(AuthorizationServerEndpointsConfigurer endpoints) {
        GitEggTokenServices tokenServices = new GitEggTokenServices(redisTemplate);
        tokenServices.setTokenStore(tokenStore());
        //支持刷新token
        tokenServices.setSupportRefreshToken(true);
        //是否重复使用RefreshToken
        tokenServices.setReuseRefreshToken(false);
        tokenServices.setClientDetailsService(endpoints.getClientDetailsService());
        tokenServices.setTokenEnhancer(endpoints.getTokenEnhancer());
        addUserDetailsService(tokenServices, this.userDetailsService);
        return tokenServices;
    }

    private void addUserDetailsService(DefaultTokenServices tokenServices, UserDetailsService userDetailsService) {
        if (userDetailsService != null) {
            PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
            provider.setPreAuthenticatedUserDetailsService(new UserDetailsByNameServiceWrapper<>(
                    userDetailsService));
            tokenServices
                    .setAuthenticationManager(new ProviderManager(Arrays.asList(provider)));
        }
    }
}

