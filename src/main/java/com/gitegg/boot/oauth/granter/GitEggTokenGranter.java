package com.gitegg.boot.oauth.granter;

import com.anji.captcha.service.CaptchaService;
import com.gitegg.boot.extension.justauth.service.IJustAuthService;
import com.gitegg.boot.extension.sms.service.ISmsService;
import com.gitegg.boot.system.service.IUserService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 自定义token
 * @author GitEgg
 */
public class GitEggTokenGranter {

    /**
     * 自定义tokenGranter
     */
    public static TokenGranter getTokenGranter(final AuthenticationManager authenticationManager,
                                               final AuthorizationServerEndpointsConfigurer endpoints, RedisTemplate redisTemplate, IUserService userService,
                                               ISmsService smsService, IJustAuthService justAuthService, CaptchaService captchaService, UserDetailsService userDetailsService, String captchaType,
                                               String secretKey, String secretKeySalt) {
        // 默认tokenGranter集合
        List<TokenGranter> granters = new ArrayList<>(Collections.singletonList(endpoints.getTokenGranter()));
        // 增加验证码模式
        granters.add(new CaptchaTokenGranter(authenticationManager, endpoints.getTokenServices(),
                endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory(), redisTemplate, captchaService,
                captchaType));
        // 增加短信验证码模式
        granters.add(new SmsCaptchaTokenGranter(authenticationManager, endpoints.getTokenServices(),
                endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory(), redisTemplate, userService, smsService, captchaService,
                userDetailsService, captchaType));
        // 增加第三方登录模式
        granters.add(new SocialTokenGranter(authenticationManager, endpoints.getTokenServices(),
                endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory(), redisTemplate, justAuthService,
                userDetailsService, captchaType, secretKey, secretKeySalt));
        // 组合tokenGranter集合
        return new CompositeTokenGranter(granters);
    }

}
