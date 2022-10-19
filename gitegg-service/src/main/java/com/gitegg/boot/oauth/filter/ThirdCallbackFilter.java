package com.gitegg.boot.oauth.filter;

import cn.hutool.core.util.StrUtil;
import com.gitegg.platform.base.constant.AuthConstant;
import com.gitegg.platform.base.domain.GitEggUser;
import com.gitegg.platform.base.util.JsonUtils;
import com.nimbusds.jose.JWSObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;

/**
 * @author GitEgg
 * @date 2021/11/14
 */
@Slf4j
@Component
@WebFilter(urlPatterns={"/online/ansheng/callback/pay/notify"})
public class ThirdCallbackFilter extends OncePerRequestFilter implements Ordered {

    /**
     * 是否开启租户模式
     */
    @Value("${tenant.enable}")
    private Boolean enable;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String tenantId = request.getHeader(AuthConstant.TENANT_ID);

        // 考虑到回调的情况，如果tenantId为空，那么自动设置默认的TenantID
        // TODO 新增回调Filter，需要判断回调配置，如果第三方无法设置tenantId时，系统根据配置来设置租户id
        if (StrUtil.isEmpty(tenantId)) {
            tenantId = "0";
        }

        MutableHttpServletRequest mutableRequest = new MutableHttpServletRequest(request);

        // 如果系统配置已开启租户模式，设置tenantId
        if (enable && !StrUtil.isEmpty(tenantId)) {
            mutableRequest.putHeader(AuthConstant.TENANT_ID, tenantId);
        }

        // 设置默认用户
        String token = request.getHeader(AuthConstant.JWT_TOKEN_HEADER);
        if (!StrUtil.isEmpty(token) && token.startsWith(AuthConstant.JWT_TOKEN_PREFIX)) {
            try {
                //从token中解析用户信息并设置到Header中去
                String realToken = token.replace(AuthConstant.JWT_TOKEN_PREFIX, "");
                JWSObject jwsObject = JWSObject.parse(realToken);
                String userStr = jwsObject.getPayload().toString();
                log.info("AuthGlobalFilter.filter() User:{}", userStr);
                mutableRequest.putHeader(AuthConstant.HEADER_USER, URLEncoder.encode(userStr, "UTF-8"));
            } catch (ParseException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        else
        {
            GitEggUser gitEggUser = new GitEggUser();
            gitEggUser.setId(1L);
            try {
                mutableRequest.putHeader(AuthConstant.HEADER_USER, URLEncoder.encode(JsonUtils.objToJson(gitEggUser), "UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        chain.doFilter(mutableRequest, response);
    }

    @Override
    public int getOrder() {
        return -1;
    }

}
