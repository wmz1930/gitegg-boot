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
 * ???????????????????????????
 * token??????????????????OAuth2??????????????????????????????????????????????????????????????????????????????
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
     * ?????????????????????
     */
    @Override
    @SneakyThrows
    public void configure(ClientDetailsServiceConfigurer clients) {
        GitEggClientDetailsServiceImpl jdbcClientDetailsService = new GitEggClientDetailsServiceImpl(dataSource);
        // client-secret????????????
        jdbcClientDetailsService.setPasswordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());
        jdbcClientDetailsService.setFindClientDetailsSql(AuthConstant.FIND_CLIENT_DETAILS_SQL);
        jdbcClientDetailsService.setSelectClientDetailsSql(AuthConstant.SELECT_CLIENT_DETAILS_SQL);
        clients.withClientDetails(jdbcClientDetailsService);
    }

    /**
     * ???????????????authorization??????????????????token?????????????????????????????????(token services)
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {

        // ????????????
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> tokenEnhancers = new ArrayList<>();
        tokenEnhancers.add(tokenEnhancer());
        tokenEnhancers.add(jwtAccessTokenConverter());
        tokenEnhancerChain.setTokenEnhancers(tokenEnhancers);

        // ???????????????tokenGranter
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
                 * refresh_token????????????????????????????????????(true)??????????????????(false)????????????true
                 * 1.???????????????access_token?????????????????? refresh token?????????????????????????????????????????????????????????
                 * 2.??????????????????access_token?????????????????? refresh_token????????????????????????refresh_token?????????????????????????????????????????????
                 */
                .reuseRefreshTokens(false)
                // ????????????Oauth2???????????????????????????
//                .pathMapping("/oauth/token", "/api/oauth/token")
                //???????????????????????????
                .exceptionTranslator(gitEggOAuth2ExceptionTranslator);
    }

    /**
     * ??????????????????
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.allowFormAuthenticationForClients()
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    /**
     * ??????????????????????????????token??????
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
     * ???classpath?????????????????????????????????(??????+??????)
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
     * JWT????????????
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
        //????????????token
        tokenServices.setSupportRefreshToken(true);
        //??????????????????RefreshToken
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

