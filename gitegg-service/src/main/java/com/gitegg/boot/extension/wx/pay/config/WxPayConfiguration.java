package com.gitegg.boot.extension.wx.pay.config;


import cn.hutool.core.text.StrPool;
import com.gitegg.platform.base.constant.AuthConstant;
import com.gitegg.platform.wechat.pay.config.GitEggWxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@Slf4j
@Configuration
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@EnableConfigurationProperties(WxPayProperties.class)
public class WxPayConfiguration {

    private final WxPayProperties properties;

    @Bean
    public WxPayService wxPayService() {
        List<WxPayProperties.PayConfig> configs = this.properties.getConfigs();
        WxPayService payService = new WxPayServiceImpl();
        if (null != configs)
        {
            payService.setMultiConfig(
                    configs.stream()
                            .map(a -> {
                                GitEggWxPayConfig payConfig = new GitEggWxPayConfig();
                                payConfig.setTenantId(null != a.getTenantId() ? a.getTenantId().toString() : AuthConstant.DEFAULT_TENANT_ID.toString());
                                payConfig.setAppId(StringUtils.trimToNull(a.getAppId()));
                                payConfig.setConfigKey(payConfig.getTenantId() + StrPool.UNDERLINE + payConfig.getAppId());
                                payConfig.setMchId(StringUtils.trimToNull(a.getMchId()));
                                payConfig.setMchKey(StringUtils.trimToNull(a.getMchKey()));
                                payConfig.setSubAppId(StringUtils.trimToNull(a.getSubAppId()));
                                payConfig.setSubMchId(StringUtils.trimToNull(a.getSubMchId()));
                                payConfig.setKeyPath(StringUtils.trimToNull(a.getKeyPath()));
                                //以下是apiv3以及支付分相关
                                payConfig.setServiceId(StringUtils.trimToNull(a.getServiceId()));
                                payConfig.setPayScoreNotifyUrl(StringUtils.trimToNull(a.getPayScoreNotifyUrl()));
                                payConfig.setPrivateKeyPath(StringUtils.trimToNull(a.getPrivateKeyPath()));
                                payConfig.setPrivateCertPath(StringUtils.trimToNull(a.getPrivateCertPath()));
                                payConfig.setCertSerialNo(StringUtils.trimToNull(a.getCertSerialNo()));
                                payConfig.setApiV3Key(StringUtils.trimToNull(a.getApiV3Key()));
                                return payConfig;
                            }).collect(Collectors.toMap( GitEggWxPayConfig::getConfigKey, a -> a, (o, n) -> o)));
        }
        return payService;
    }

}
