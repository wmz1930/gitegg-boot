package com.gitegg.boot.extension.component;

import cn.binarywang.wx.miniapp.api.WxMaService;
import com.gitegg.boot.extension.justauth.service.IJustAuthConfigService;
import com.gitegg.boot.extension.justauth.service.IJustAuthSourceService;
import com.gitegg.boot.extension.mail.service.IMailChannelService;
import com.gitegg.boot.extension.wx.miniapp.service.IMiniappService;
import com.gitegg.boot.extension.wx.pay.service.IPayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 容器启动完成加载扩展信息数据到缓存
 * @author GitEgg
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Component
public class InitExtensionCacheRunner implements CommandLineRunner {
    
    private final IJustAuthConfigService justAuthConfigService;
    
    private final IJustAuthSourceService justAuthSourceService;

    private final IMailChannelService mailChannelService;

    private final IMiniappService miniappService;

    private final IPayService payService;

    @Override
    public void run(String... args) {

        log.info("InitExtensionCacheRunner running");
    
    
        // 初始化第三方登录主配置
        justAuthConfigService.initJustAuthConfigList();

        // 初始化第三方登录 第三方配置
        justAuthSourceService.initJustAuthSourceList();

        // 初始化邮件配置信息
        mailChannelService.initMailChannelList();

        // 初始化微信配置信息
        miniappService.initMiniappList();

        // 初始化微信支付配置信息
        payService.initWxPayList();

    }
}

