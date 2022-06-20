package com.gitegg.boot.oauth.granter;

import com.anji.captcha.service.CaptchaService;
import com.gitegg.boot.extension.sms.service.ISmsService;
import com.gitegg.boot.oauth.util.CaptchaUtils;
import com.gitegg.boot.system.service.IUserService;
import com.gitegg.platform.base.constant.TokenConstant;
import com.gitegg.platform.base.enums.ResultCodeEnum;
import com.gitegg.platform.base.result.Result;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.UserDeniedAuthorizationException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 短信验证码模式
 * @author GitEgg
 */
public class SmsCaptchaTokenGranter extends AbstractTokenGranter {

    private static final String GRANT_TYPE = "sms_captcha";

    private final AuthenticationManager authenticationManager;

    private UserDetailsService userDetailsService;

    private IUserService userService;

    private ISmsService smsService;

    private RedisTemplate redisTemplate;

    private CaptchaService captchaService;

    private String captchaType;

    public SmsCaptchaTokenGranter(AuthenticationManager authenticationManager,
                                  AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService,
                                  OAuth2RequestFactory requestFactory, RedisTemplate redisTemplate, IUserService userService,
                                  ISmsService smsService, CaptchaService captchaService,
                                  UserDetailsService userDetailsService, String captchaType) {
        this(authenticationManager, tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
        this.redisTemplate = redisTemplate;
        this.captchaService = captchaService;
        this.captchaType = captchaType;
        this.userService = userService;
        this.smsService = smsService;
        this.userDetailsService = userDetailsService;
    }

    protected SmsCaptchaTokenGranter(AuthenticationManager authenticationManager,
                                  AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService,
                                  OAuth2RequestFactory requestFactory, String grantType) {
        super(tokenServices, clientDetailsService, requestFactory, grantType);
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {

        Map<String, String> parameters = new LinkedHashMap<>(tokenRequest.getRequestParameters());
        boolean checkCaptchaResult = CaptchaUtils.checkCaptcha(parameters, redisTemplate, captchaService);
        if (!checkCaptchaResult)
        {
            throw new UserDeniedAuthorizationException(ResultCodeEnum.INVALID_CAPTCHA.getMsg());
        }

        String phoneNumber = parameters.get(TokenConstant.PHONE_NUMBER);
        String smsCode = parameters.get(TokenConstant.SMS_CODE);
        String code = parameters.get(TokenConstant.CODE);
        // Protect from downstream leaks of password
        parameters.remove(TokenConstant.CODE);

        Result<Boolean> checkResult = smsService.checkSmsVerificationCode(smsCode, phoneNumber, code);

        if (null == checkResult || !checkResult.getData()) {
            throw new InvalidGrantException(("Could not authenticate user: " + phoneNumber));
        }

        UserDetails userDetails = this.userDetailsService.loadUserByUsername(phoneNumber);

        Authentication userAuth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        ((AbstractAuthenticationToken)userAuth).setDetails(parameters);

        OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
        return new OAuth2Authentication(storedOAuth2Request, userAuth);
    }

}

