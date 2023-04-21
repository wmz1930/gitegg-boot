package com.gitegg.boot.oauth.service;

import cn.hutool.core.bean.BeanUtil;
import com.gitegg.boot.oauth.enums.AuthEnum;
import com.gitegg.boot.oauth.exception.GitEggOAuth2Exception;
import com.gitegg.boot.system.entity.User;
import com.gitegg.boot.system.entity.UserInfo;
import com.gitegg.boot.system.service.IUserService;
import com.gitegg.platform.base.constant.AuthConstant;
import com.gitegg.platform.base.constant.GitEggConstant;
import com.gitegg.platform.base.constant.TokenConstant;
import com.gitegg.platform.base.domain.GitEggUser;
import com.gitegg.platform.base.enums.ResultCodeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.UserDeniedAuthorizationException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *  实现SpringSecurity获取用户信息接口
 *
 * @author gitegg
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GitEggUserDetailsServiceImpl implements UserDetailsService {

    private final IUserService userService;

    private final HttpServletRequest request;

    private final RedisTemplate redisTemplate;

    /**
     * 密码最大尝试次数
     */
    @Value("${system.maxTryTimes}")
    private int maxTryTimes;

    /**
     * 不需要验证码登录的最大尝试次数
     */
    @Value("${system.maxNonCaptchaTimes}")
    private int maxNonCaptchaTimes;

    @Override
    public GitEggUserDetails loadUserByUsername(String username) {

        // 获取登录类型，密码，二维码，验证码
        String authGrantType = request.getParameter(AuthConstant.GRANT_TYPE);

        // 获取客户端id
        String clientId = request.getParameter(AuthConstant.AUTH_CLIENT_ID);

        // 远程调用返回数据
        UserInfo userInfo;

        User user = new User();
        // 通过手机号码登录
        if (!StringUtils.isEmpty(authGrantType) && AuthEnum.SMS_CAPTCHA.code.equals(authGrantType))
        {
            String phone = request.getParameter(TokenConstant.PHONE_NUMBER);
            user.setMobile(phone);
            userInfo = userService.queryUserInfo(user);
        }
        // 通过账号密码登录
        else if(!StringUtils.isEmpty(authGrantType) && AuthEnum.QR.code.equals(authGrantType))
        {
            user.setAccount(username);
            userInfo = userService.queryUserInfo(user);
        }
        // 第三方登录
        else if(!StringUtils.isEmpty(authGrantType) && AuthEnum.SOCIAL.code.equals(authGrantType))
        {
            user.setId(Long.parseLong(username));
            userInfo = userService.queryUserInfo(user);
        }
        else
        {
            user.setAccount(username);
            userInfo = userService.queryUserInfo(user);
        }

        // 判断返回信息
        if (null != userInfo) {
            GitEggUser gitEggUser = new GitEggUser();
            BeanUtil.copyProperties(userInfo, gitEggUser, false);

            // 用户名或密码错误
            if (gitEggUser == null || gitEggUser.getId() == null) {
                throw new UsernameNotFoundException(ResultCodeEnum.INVALID_USERNAME.msg);
            }

            // 没有角色
            if (CollectionUtils.isEmpty(gitEggUser.getRoleIdList())) {
                throw new UserDeniedAuthorizationException(ResultCodeEnum.INVALID_ROLE.msg);
            }

            // 从Redis获取账号密码错误次数
            Object lockTimes = redisTemplate.boundValueOps(AuthConstant.LOCK_ACCOUNT_PREFIX + gitEggUser.getId()).get();

            // 判断账号密码输入错误几次，如果输入错误多次，则锁定账号
            // 输入错误大于配置的次数，必须选择captcha或sms_captcha
            if (null != lockTimes && (int)lockTimes > maxNonCaptchaTimes && ( StringUtils.isEmpty(authGrantType) || (!StringUtils.isEmpty(authGrantType)
                    && !AuthEnum.SMS_CAPTCHA.code.equals(authGrantType) && !AuthEnum.CAPTCHA.code.equals(authGrantType)))) {
                throw new GitEggOAuth2Exception(ResultCodeEnum.INVALID_PASSWORD_CAPTCHA.msg);
            }

            // 判断账号是否被锁定（账户过期，凭证过期等可在此处扩展）
            if(null != lockTimes && (int)lockTimes >= maxTryTimes){
                throw new LockedException(ResultCodeEnum.PASSWORD_TRY_MAX_ERROR.msg);
            }

            // 判断账号是否被禁用
            String userStatus = gitEggUser.getStatus();
            if (String.valueOf(GitEggConstant.DISABLE).equals(userStatus)) {
                throw new DisabledException(ResultCodeEnum.DISABLED_ACCOUNT.msg);
            }

            /**
             * enabled 账户是否被禁用  !String.valueOf(GitEggConstant.DISABLE).equals(gitEggUser.getStatus())
             * AccountNonExpired 账户是否过期  此框架暂时不提供账户过期功能，可根据业务需求在此处扩展
             * AccountNonLocked  账户是否被锁  密码尝试次数过多，则锁定账户
             * CredentialsNonExpired 凭证是否过期
             */
            return new GitEggUserDetails(gitEggUser.getId(), gitEggUser.getTenantId(), gitEggUser.getOauthId(),
                gitEggUser.getNickname(), gitEggUser.getRealName(), gitEggUser.getOrganizationId(),
                gitEggUser.getOrganizationName(),
                    gitEggUser.getOrganizationIds(), gitEggUser.getOrganizationNames(), gitEggUser.getRoleId(), gitEggUser.getRoleIds(), gitEggUser.getRoleName(), gitEggUser.getRoleNames(),
                gitEggUser.getRoleIdList(), gitEggUser.getRoleKeyList(), gitEggUser.getResourceKeyList(),
                gitEggUser.getDataPermissionTypeList(), gitEggUser.getOrganizationIdList(),
                gitEggUser.getAvatar(), gitEggUser.getAccount(), gitEggUser.getPassword(), !String.valueOf(GitEggConstant.DISABLE).equals(gitEggUser.getStatus()), true, true, true,
                this.getPrivileges(gitEggUser.getRoleKeyList(), gitEggUser.getResourceUrlList()));
        } else {
            throw new UsernameNotFoundException("查询账号失败");
        }
    }

    /**
     * 设置SpringSecurity需要的role和resource
     * @param roles
     * @param resources
     * @return
     */
    private final List<GrantedAuthority> getPrivileges(final Collection<String> roles, final Collection<String> resources) {
        final List<GrantedAuthority> authorities = new ArrayList<>();

        for (final String role : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        }

        //不将resource权限加入token，这样会导致请求头很大
//        for (final String resource : resources) {
//            authorities.add(new SimpleGrantedAuthority(resource));
//        }

        return authorities;
    }

}
