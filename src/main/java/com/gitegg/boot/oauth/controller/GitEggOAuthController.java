package com.gitegg.boot.oauth.controller;

import cn.hutool.core.bean.BeanUtil;
import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.gitegg.boot.extension.sms.service.ISmsService;
import com.gitegg.boot.oauth.util.CaptchaUtils;
import com.gitegg.boot.oauth.util.JwtUtils;
import com.gitegg.boot.system.entity.User;
import com.gitegg.boot.system.service.IUserService;
import com.gitegg.platform.base.annotation.auth.CurrentUser;
import com.gitegg.platform.base.constant.AuthConstant;
import com.gitegg.platform.base.constant.GitEggConstant;
import com.gitegg.platform.base.constant.TokenConstant;
import com.gitegg.platform.base.domain.GitEggUser;
import com.gitegg.platform.base.exception.BusinessException;
import com.gitegg.platform.base.result.Result;
import com.gitegg.platform.captcha.constant.CaptchaConstant;
import com.gitegg.platform.captcha.domain.ImageCaptcha;
import com.gitegg.platform.oauth2.domain.Oauth2Token;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.wf.captcha.SpecCaptcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.security.KeyPair;
import java.security.Principal;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author GitEgg
 */
@Api(tags = "OAuth2认证中心")
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("/oauth")
public class GitEggOAuthController {

    private final TokenEndpoint tokenEndpoint;

    private final RedisTemplate redisTemplate;

    private final KeyPair keyPair;

    private final CaptchaService captchaService;

    private final IUserService userService;

    private final ISmsService smsService;

    @Value("${captcha.type}")
    private String captchaType;

    @ApiOperation("获取系统配置的验证码类型")
    @GetMapping("/captcha/type")
    public Result captchaType() {
        return Result.data(captchaType);
    }

    @ApiOperation("生成滑动验证码")
    @PostMapping("/captcha")
    public Result captcha(@RequestBody CaptchaVO captchaVO) {
        ResponseModel responseModel = captchaService.get(captchaVO);
        return Result.data(responseModel);
    }

    @ApiOperation("滑动验证码验证")
    @PostMapping("/captcha/check")
    public Result captchaCheck(@RequestBody CaptchaVO captchaVO) {
        ResponseModel responseModel = captchaService.check(captchaVO);
        return Result.data(responseModel);
    }

    @ApiOperation("生成图片验证码")
    @RequestMapping("/captcha/image")
    public Result captchaImage() {
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 5);
        String captchaCode = specCaptcha.text().toLowerCase();
        String captchaKey = UUID.randomUUID().toString();
        // 存入redis并设置过期时间为5分钟
        redisTemplate.opsForValue().set(CaptchaConstant.IMAGE_CAPTCHA_KEY + captchaKey, captchaCode, GitEggConstant.Number.FIVE,
            TimeUnit.MINUTES);
        ImageCaptcha imageCaptcha = new ImageCaptcha();
        imageCaptcha.setCaptchaKey(captchaKey);
        imageCaptcha.setCaptchaImage(specCaptcha.toBase64());
        // 将key和base64返回给前端
        return Result.data(imageCaptcha);
    }

    @ApiOperation("OAuth2生成token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "grant_type", defaultValue = "password", value = "授权模式", required = true),
            @ApiImplicitParam(name = "client_id", defaultValue = "client", value = "Oauth2客户端ID", required = true),
            @ApiImplicitParam(name = "client_secret", defaultValue = "123456", value = "Oauth2客户端秘钥", required = true),
            @ApiImplicitParam(name = "refresh_token", value = "刷新token"),
            @ApiImplicitParam(name = "username", defaultValue = "admin", value = "登录用户名"),
            @ApiImplicitParam(name = "password", defaultValue = "123456", value = "登录密码"),
            @ApiImplicitParam(name = "phoneNumber", value = "手机号码"),
            @ApiImplicitParam(name = "smsCode", value = "短信模板code"),
            @ApiImplicitParam(name = "code", value = "小程序code/短信验证码"),
            @ApiImplicitParam(name = "encryptedData", value = "包括敏感数据在内的完整用户信息的加密数据"),
            @ApiImplicitParam(name = "iv", value = "加密算法的初始向量"),
    })
    @PostMapping("/token")
    public Result postAccessToken( @ApiIgnore Principal principal, @ApiIgnore @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {

        //先对密码进行处理，取account和md5加密密码
        String username = parameters.get("username");
        String password = parameters.get("password");
        User user = userService.queryUserByAccount(username);
        if (null != user) {
            GitEggUser gitEggUser = new GitEggUser();
            BeanUtil.copyProperties(user, gitEggUser, false);
            if (!StringUtils.isEmpty(gitEggUser.getAccount())) {
                username = gitEggUser.getAccount();
                password = AuthConstant.BCRYPT + gitEggUser.getAccount() + password;
                parameters.put("username", username);
                parameters.put("password", password);
            }
        }

        OAuth2AccessToken oAuth2AccessToken = tokenEndpoint.postAccessToken(principal, parameters).getBody();
        DefaultExpiringOAuth2RefreshToken refreshToken = (DefaultExpiringOAuth2RefreshToken)oAuth2AccessToken.getRefreshToken();
        Oauth2Token oauth2Token = Oauth2Token.builder()
                .token(oAuth2AccessToken.getValue())
                .expiresIn(oAuth2AccessToken.getExpiresIn())
                .exp(oAuth2AccessToken.getExpiration().getTime() / GitEggConstant.Number.THOUSAND)
                .refreshToken(refreshToken.getValue())
                .refreshExpiresIn((int) (refreshToken.getExpiration().getTime() / GitEggConstant.Number.THOUSAND - System.currentTimeMillis() / GitEggConstant.Number.THOUSAND))
                .refreshExp(refreshToken.getExpiration().getTime() / GitEggConstant.Number.THOUSAND)
                .tokenHead(AuthConstant.JWT_TOKEN_PREFIX)
                .build();
        return Result.data(oauth2Token);
    }

    @ApiOperation("发送短信验证码")
    @PostMapping("/sms/captcha/send")
    public Result sendSmsCaptcha(@ApiIgnore @RequestParam Map<String, String> parameters) {
        boolean checkCaptchaResult = CaptchaUtils.checkCaptcha(parameters, redisTemplate, captchaService);
        Result<?> sendResult;
        if (checkCaptchaResult)
        {
            sendResult  = smsService.sendSmsVerificationCode(parameters.get(TokenConstant.SMS_CODE), parameters.get(TokenConstant.PHONE_NUMBER));
        }
        else {
            throw new BusinessException("请通过正确的安全验证，再发送短信验证码");
        }
        return sendResult;
    }

    /**
     * 退出登录需要需要登录的一点思考：
     * 1、如果不需要登录，那么在调用接口的时候就需要把token传过来，且系统不校验token有效性，此时如果系统被攻击，不停的大量发送token，最后会把redis充爆
     * 2、如果调用退出接口必须登录，那么系统会调用token校验有效性，refresh_token通过参数传过来加入黑名单
     * 综上：选择调用退出接口需要登录的方式
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public Result logout(HttpServletRequest request) {

        String token = request.getHeader(AuthConstant.JWT_TOKEN_HEADER);
        String refreshToken = request.getParameter(AuthConstant.REFRESH_TOKEN);
        long currentTimeSeconds = System.currentTimeMillis() / GitEggConstant.Number.THOUSAND;

        // 将token和refresh_token同时加入黑名单
        String[] tokenArray = new String[GitEggConstant.Number.TWO];
        tokenArray[GitEggConstant.Number.ZERO] = token.replace(AuthConstant.JWT_TOKEN_PREFIX, "");
        tokenArray[GitEggConstant.Number.ONE] = refreshToken;
        for (int i = GitEggConstant.Number.ZERO; i < tokenArray.length; i++) {
            String realToken = tokenArray[i];
            JSONObject jsonObject = JwtUtils.decodeJwt(realToken);
            String jti = jsonObject.getAsString(TokenConstant.JTI);
            Long exp = Long.parseLong(jsonObject.getAsString(TokenConstant.EXP));
            if (exp - currentTimeSeconds > GitEggConstant.Number.ZERO) {
                redisTemplate.opsForValue().set(AuthConstant.TOKEN_BLACKLIST + jti, jti, (exp - currentTimeSeconds), TimeUnit.SECONDS);
            }
        }
        return Result.success();
    }

    @GetMapping("/user/info")
    public Result<GitEggUser> currentUser(@ApiIgnore @CurrentUser GitEggUser user) {
        return Result.data(user);
    }

    @GetMapping("/public_key")
    public Map<String, Object> getKey() {
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAKey key = new RSAKey.Builder(publicKey).build();
        return new JWKSet(key).toJSONObject();
    }

}
